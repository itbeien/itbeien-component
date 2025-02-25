package cn.itbeien.root.ds.transaction;

import cn.itbeien.root.ds.component.SpringUtils;
import cn.itbeien.root.ds.constant.DbReadWriteModel;
import cn.itbeien.root.ds.transaction.enums.DatasourceConstantPropertiKey;
import cn.itbeien.root.ds.transaction.enums.Isolation;
import cn.itbeien.root.ds.transaction.enums.Propagation;
import cn.itbeien.root.service.ISaasModuleService;
import cn.itbeien.root.util.StringUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

/**
 * 多数据源事务管理器,基于Spring AOP实现
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Aspect
@Component
public class MultiTransactionManager {
    private static final Logger logger = LoggerFactory.getLogger(MultiTransactionManager.class);

    @Pointcut("@annotation(cn.itbeien.root.ds.transaction.TransactionMulti)")
    public void annotationPointcut(){}


    @Around("annotationPointcut()")
    public Object  roundExecute(ProceedingJoinPoint joinpoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinpoint.getSignature();
        Method method = methodSignature.getMethod();
        Map<String, Object> params = getMethodParams(joinpoint);
        TransactionMulti tranInfo = method.getAnnotation(TransactionMulti.class);
        String  datasource = (String)params.get(tranInfo.datasource());
        String dataSourceName = tranInfo.datasource();
        //dynamic-dbTC_KQXXHHS0001READ_WRITE3
        if(!StringUtil.isEmpty(datasource)){
            ISaasModuleService saasModuleServiceImpl = SpringUtils.getBean("saasModuleServiceImpl");
            String bussinessObjCode = datasource.split("_")[0];
            String tenantCode = saasModuleServiceImpl.getTenantByBussinessObjCode(bussinessObjCode);

            if (StringUtil.isBlank(tenantCode)) {
                logger.error("商户编号未找到对应数据源配置, bussinessObjCode: {}", bussinessObjCode);
                throw new Exception("未找到数据源");
            }
            logger.debug("商户编号对应数据源配置, bussinessObjCode: {},tenantCode: {}", bussinessObjCode, tenantCode);

            Integer databaseId = saasModuleServiceImpl.getMasterDBByTenantCode(tenantCode);

            dataSourceName = DatasourceConstantPropertiKey.DATASOURCEFLAG.getKey() + tenantCode + DbReadWriteModel.READ_WRITE + databaseId;
        }

        return openTransaction(joinpoint, dataSourceName, tranInfo);
    }



    /*是否不需要回滚:true表示需要回滚*/
    private boolean executeRollBackFor(TransactionMulti tranAnno, Throwable e) {
        Class<? extends Throwable>[] rollBackType = tranAnno.rollbackFor();
        for (Class<? extends Throwable> cls : rollBackType) {
            if (cls.isInstance(e)) {
                return true;
            }
        }
        String[] rollBackName = tranAnno.rollbackForClassName();
        String errorType = e.getClass().getName();
        for (String name : rollBackName) {
            if (errorType.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 动态的给数据源绑定事务
     * @param joinpoint
     * @param value
     * @param tranInfo
     * @return
     * @throws Throwable
     */
    private Object openTransaction(ProceedingJoinPoint joinpoint,String value,TransactionMulti tranInfo) throws Throwable {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        Map<Object, Object> dataSourceMap = dynamicDataSource.getDataSourceMap();
        javax.sql.DataSource dataSource = (javax.sql.DataSource) dataSourceMap.get(value);
        if (dataSource == null) {
            logger.error("数据源未创建成功,数据源key为：{}",value);
            throw new Exception("未找到数据库异常:"+value);
        }
        //绑定到线程上面 dynamicDataSource.determineCurrentLookupKey()
        ConnectionHolder connectionHolder = TranManager.currentConnectionHolder(dataSource);
        dynamicDataSource.bindConnection(value, connectionHolder.getConnection());

        TransactionManager manager = TranManager.getManager(dataSource);
        Propagation behavior = tranInfo.propagation();
        Isolation level = tranInfo.isolation();
        TransactionStatus tranStatus = manager.getTransaction(behavior, level);

        //1.只读事务
        if (tranInfo.readOnly()) {
           tranStatus.setReadOnly();
        }

        try {
             return joinpoint.proceed();
        } catch (Exception e) {
             if (this.executeRollBackFor(tranInfo, e)) {
                tranStatus.setRollbackOnly();
             }
            throw e;
         }finally {
               if (!tranStatus.isCompleted()) {
                  manager.commit(tranStatus);//提交事务
               }
         }

    }


    public static Map<String, Object> getMethodParams(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
      /*  Object[] paramValues = joinPoint.getArgs();
        String[] paramNames = methodSignature.getParameterNames();*/
        Method method = methodSignature.getMethod();
        Parameter[] parameters = method.getParameters();
        Object[] paramValues = joinPoint.getArgs();
        Map<String, Object> params = new HashMap<String, Object>();
        for (int i = 0; i < paramValues.length; i++) {
            params.put(parameters[i].getName(), paramValues[i]);
        }
        return params;
    }


}
