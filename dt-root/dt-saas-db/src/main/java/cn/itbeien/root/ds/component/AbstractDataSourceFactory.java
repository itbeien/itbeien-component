package cn.itbeien.root.ds.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * 抽象的数据源管理
 * 针对每种数据源实现，实现不同的子类
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public abstract class AbstractDataSourceFactory {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDataSourceFactory.class);

	/**
	 * DataSource创建后，初始化方法名
	 */
    protected String initMethod;
    /**
     * DataSource关闭方法名
     */
    protected String closeMethod;


    public String getInitMethod() {
        return initMethod;
    }

    public AbstractDataSourceFactory setInitMethod(String initMethod) {
        this.initMethod = initMethod;
        return this;
    }

    public String getCloseMethod() {
        return closeMethod;
    }

    public AbstractDataSourceFactory setCloseMethod(String closeMethod) {
        this.closeMethod = closeMethod;
        return this;
    }

    /**
     * 针对每种数据源需要不同的创建方式
     * @param properties etcd中配置的数据源参数，key-value
     * @return
     */
    public abstract DataSource createDataSource(Map<String,String> properties) throws Exception;

    public void initDataSource(DataSource ds){
        if(this.initMethod != null){
            invokeMethod( ds,this.initMethod );
        }
    }

    public void closeDataSource(DataSource ds){
        if(this.closeMethod != null){
            invokeMethod( ds,this.closeMethod );
        }
    }

    private void invokeMethod(DataSource ds, String methodName) {
        try {
            Method method = ds.getClass().getMethod(methodName);
            method.invoke(ds);
        } catch (Exception e) {
            logger.error("invoke DataSource method error, method name is {} ", methodName, e);
        }
    }
}
