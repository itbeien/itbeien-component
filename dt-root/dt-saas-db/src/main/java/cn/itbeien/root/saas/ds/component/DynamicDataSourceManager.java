package cn.itbeien.root.saas.ds.component;

import cn.itbeien.root.saas.config.NacosConfig;
import cn.itbeien.root.saas.ds.entity.DatabaseConfig;
import cn.itbeien.root.saas.ds.transaction.DynamicDataSource;
import cn.itbeien.root.saas.ds.transaction.enums.DatasourceConstantPropertiKey;
import cn.itbeien.root.saas.service.ISaasModuleService;
import cn.itbeien.root.saas.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Component
public class DynamicDataSourceManager {

    // 注入saasModuleServiceImpl
    @Autowired
    @Qualifier(value="saasModuleServiceImpl")
    private ISaasModuleService saasModuleServiceImpl;

    // 注入DriudDataSourceFactory
    @Autowired
    private DriudDataSourceFactory driudDataSourceFactory;

    // 注入DynDataSourceCache
    @Autowired
    private DynDataSourceCache dynDataSourceCache;

    // 注入NacosConfig
    @Autowired
    private NacosConfig nacosConfig;

    /**
     * 创建租户数据源
     */
    public DynamicDataSource createDataSource(String tenantCode, Integer databaseId){
        // 获取当前数据源
        Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
        
        // 获取动态数据源
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();

        // 加载数据库配置
        DatabaseConfig databaseConfig = this.saasModuleServiceImpl.loadDatabaseConfig(databaseId);
        
        // 数据源标识
        String dbSource = DatasourceConstantPropertiKey.DATASOURCEFLAG.getKey() + tenantCode + databaseConfig.getDbModel() + databaseId;

        // 数据源配置
        Map<String, String> properties = new HashMap<>();
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_DRIVERCLASS.getDataSourceKey(),databaseConfig.getDriverClassName());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_USER.getDataSourceKey(),databaseConfig.getDbUsername());
        String key = nacosConfig.getKey();//秘钥
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_PASSWORD.getDataSourceKey(), EncryptUtil.DES3DecryptFromStr(databaseConfig.getDbPassword(),key));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_DRIVERURL.getDataSourceKey(),databaseConfig.getDriverUrl());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_INITIALSIZE.getDataSourceKey(),String.valueOf(databaseConfig.getInitialSize()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXACTIVE.getDataSourceKey(),String.valueOf(databaseConfig.getMaxActive()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE.getDataSourceKey(),String.valueOf(databaseConfig.getMaxPoolPreparedStatementPerConnectionSize()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXWAIT.getDataSourceKey(),String.valueOf(databaseConfig.getMaxWait()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MINEVICTABLEIDLETIMEMILLIS.getDataSourceKey(),String.valueOf(databaseConfig.getMinEvictableIdleTimeMillis()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MINIDLE.getDataSourceKey(),String.valueOf(databaseConfig.getMinIdle()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_POOLPREPAREDSTATEMENTS.getDataSourceKey(),databaseConfig.getPoolPreparedStatements());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TESTONBORROW.getDataSourceKey(),databaseConfig.getTestOnBorrow());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TESTWHILEIDLE.getDataSourceKey(),databaseConfig.getTestWhileIdle());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TIMEBETWEENEVICTIONRUNSMILLIS.getDataSourceKey(),String.valueOf(databaseConfig.getTimeBetweenEvictionRunsMillis()));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_VALIDATIONQUERY.getDataSourceKey(),databaseConfig.getValidationQuery());

       properties.put("init","true");

        // 创建数据源
        DataSource dataSource = driudDataSourceFactory.createDataSource(properties);
        if(dataSource != null){
            // 将数据源放入数据源集合
            dataSourceMap.put(dbSource, dataSource);
            // 设置动态数据源的目标数据源
            dynamicDataSource.setTargetDataSources(dataSourceMap);
            // 设置默认数据源
            dynamicDataSource.setDefaultTargetDataSource(dataSource);
        }
        return dynamicDataSource;
    }


    /**
     * 关闭租户数据源
     */
    public void closeDataSource(String tenantCode, List<Integer> databaseIds){

        // 遍历数据库ID
        for(Integer databaseId : databaseIds) {
            // 加载数据库配置
            DatabaseConfig databaseConfig = this.saasModuleServiceImpl.loadDatabaseConfig(databaseId);
            // 数据源标识
            String dbSource = DatasourceConstantPropertiKey.DATASOURCEFLAG.getKey() + tenantCode + databaseConfig.getDbModel() + databaseId;
            // 获取当前数据源
            Map<Object, Object> dataSourceMap = DynamicDataSource.getInstance().getDataSourceMap();
            // 获取数据源
            DataSource dataSource = (DataSource) dataSourceMap.get(dbSource);
            if (dataSource != null) {
                // 关闭数据源
                driudDataSourceFactory.closeDataSource(dataSource);
                // 从数据源集合中移除数据源
                dataSourceMap.remove(dbSource);
            }
            //key 租户+dbMode 下架数据源,清除本地数据缓存
            dynDataSourceCache.removeTenantToDBMap(tenantCode + databaseConfig.getDbModel(), databaseId);
        }
    }

}
