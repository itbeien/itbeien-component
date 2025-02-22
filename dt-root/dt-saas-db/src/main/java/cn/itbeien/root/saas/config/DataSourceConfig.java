package cn.itbeien.root.saas.config;

import cn.itbeien.root.saas.ds.component.DataSourceContextHolder;
import cn.itbeien.root.saas.ds.component.DataSourceRouteInterceptor;
import cn.itbeien.root.saas.ds.component.DriudDataSourceFactory;
import cn.itbeien.root.saas.ds.transaction.DynamicDataSource;
import cn.itbeien.root.saas.ds.transaction.enums.DatasourceConstantPropertiKey;
import cn.itbeien.root.saas.util.EncryptUtil;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Configuration
public class DataSourceConfig {
    private static final Logger logger = LoggerFactory.getLogger(DataSourceConfig.class);

    @Resource
    private DriudDataSourceFactory driudDataSourceFactory;

    @Resource
    private NacosConfig nacosConfig;


    @Bean
    public DynamicDataSource dynamicDataSource() {
        DynamicDataSource dynamicDataSource = DynamicDataSource.getInstance();
        Map<Object,Object> dataSourceMap = new HashMap<>();
        Map<String, String> properties = new HashMap<>();

        String key = nacosConfig.getKey();//秘钥
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_DRIVERCLASS.getDataSourceKey(),nacosConfig.getDriverClass());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_USER.getDataSourceKey(), nacosConfig.getUserName());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_PASSWORD.getDataSourceKey(), EncryptUtil.DES3DecryptFromStr(nacosConfig.getPassword(),key));
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_DRIVERURL.getDataSourceKey(),nacosConfig.getDriverUrl());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_INITIALSIZE.getDataSourceKey(), nacosConfig.getInitialSize());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXACTIVE.getDataSourceKey(), nacosConfig.getMaxActive());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE.getDataSourceKey(), nacosConfig.getMaxPoolPreparedStatementPerConnectionSize());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MAXWAIT.getDataSourceKey(),nacosConfig.getMaxWait());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MINEVICTABLEIDLETIMEMILLIS.getDataSourceKey(),nacosConfig.getMinEvictableIdleTimeMillis());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_MINIDLE.getDataSourceKey(),nacosConfig.getMinIdle());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_POOLPREPAREDSTATEMENTS.getDataSourceKey(),nacosConfig.getPoolPreparedStatements());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TESTONBORROW.getDataSourceKey(),nacosConfig.getTestOnBorrow());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TESTWHILEIDLE.getDataSourceKey(),nacosConfig.getTestWhileIdle());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_TIMEBETWEENEVICTIONRUNSMILLIS.getDataSourceKey(),nacosConfig.getTimeBetweenEvictionRunsMillis());
        properties.put(DatasourceConstantPropertiKey.MASTER_JDBC_VALIDATIONQUERY.getDataSourceKey(),nacosConfig.getValidationQuery());

        properties.put("init","true");
        DataSource defaultDataSource = driudDataSourceFactory.createDataSource(properties);
        dataSourceMap.put(DatasourceConstantPropertiKey.DEFAULT_MASTER.getKey(), defaultDataSource);

        //判断是否有从库
        if(StringUtils.isNotBlank(nacosConfig.getSlaveDriverClass())) {
            Map<String, String> slaveProperties = new HashMap<>();
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_DRIVERCLASS.getDataSourceKey(), nacosConfig.getSlaveDriverClass());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_USER.getDataSourceKey(), nacosConfig.getSlaveUserName());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_PASSWORD.getDataSourceKey(), EncryptUtil.DES3DecryptFromStr(nacosConfig.getSlavePassword(), key));
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_DRIVERURL.getDataSourceKey(), nacosConfig.getSlaveDriverUrl());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_INITIALSIZE.getDataSourceKey(), nacosConfig.getSlaveInitialSize());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_MAXACTIVE.getDataSourceKey(), nacosConfig.getSlaveMaxActive());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_MAXPOOLPREPAREDSTATEMENTPERCONNECTIONSIZE.getDataSourceKey(), nacosConfig.getSlaveMaxPoolPreparedStatementPerConnectionSize());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_MAXWAIT.getDataSourceKey(), nacosConfig.getSlaveMaxWait());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_MINEVICTABLEIDLETIMEMILLIS.getDataSourceKey(), nacosConfig.getSlaveMinEvictableIdleTimeMillis());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_MINIDLE.getDataSourceKey(), nacosConfig.getSlaveMinIdle());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_POOLPREPAREDSTATEMENTS.getDataSourceKey(), nacosConfig.getSlavePoolPreparedStatements());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_TESTONBORROW.getDataSourceKey(), nacosConfig.getSlaveTestOnBorrow());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_TESTWHILEIDLE.getDataSourceKey(), nacosConfig.getSlaveTestWhileIdle());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_TIMEBETWEENEVICTIONRUNSMILLIS.getDataSourceKey(), nacosConfig.getSlaveTimeBetweenEvictionRunsMillis());
            slaveProperties.put(DatasourceConstantPropertiKey.SLAVE_JDBC_VALIDATIONQUERY.getDataSourceKey(), nacosConfig.getSlaveValidationQuery());
            slaveProperties.put("init", "true");
            DataSource slaveDefaultDataSource = driudDataSourceFactory.createDataSource(slaveProperties);
            dataSourceMap.put(DatasourceConstantPropertiKey.DEFAULT_SLAVE.getKey(),slaveDefaultDataSource);
        }

        dynamicDataSource.setTargetDataSources(dataSourceMap);
        dynamicDataSource.setDefaultTargetDataSource(defaultDataSource);
        DataSourceContextHolder.setDBType(DatasourceConstantPropertiKey.DEFAULT_MASTER.getKey());

        logger.info("create dynamic datasource is success,tartgetDataSources is {}", DataSourceContextHolder.getDBType());
        return dynamicDataSource;

    }

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(
            @Qualifier("dynamicDataSource") DataSource dynamicDataSource)
            throws Exception {
        SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicDataSource);
        Interceptor mybatisInterceptor = new DataSourceRouteInterceptor();
        Interceptor[] plugins =  new Interceptor[]{ mybatisInterceptor};
        sessionFactory.setPlugins(plugins);
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver()
                .getResources("classpath*:cn/itbeien/**/dao/**/*Mapper.xml"));
        sessionFactory.getObject().getConfiguration().setMapUnderscoreToCamelCase(true);
        return sessionFactory.getObject();
    }

    /**
     * 注入 DataSourceTransactionManager 用于事务管理
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        return new DataSourceTransactionManager(dynamicDataSource());
    }



}
