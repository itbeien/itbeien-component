package cn.itbeien.root.saas.ds.transaction;

import cn.itbeien.root.saas.ds.component.DataSourceContextHolder;
import cn.itbeien.root.saas.ds.transaction.enums.DatasourceConstantPropertiKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 动态数据源路由基于Spring AbstractRoutingDataSource实现
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    private static final Logger logger = LoggerFactory.getLogger(DynamicDataSource.class);

    private static DynamicDataSource instance;
    private static Map<Object, Object> dataSourceMap = new ConcurrentHashMap<Object, Object>();
    //保存当前事务下的连接,用于事务处理
    private static ThreadLocal<Map<String, ConnectionWarp>> connectionThreadLocal = new ThreadLocal<>();

    @Override
    public void setTargetDataSources(Map<Object, Object> targetDataSources) {
        super.setTargetDataSources(targetDataSources);
        dataSourceMap.putAll(targetDataSources);
        super.afterPropertiesSet();// 必须添加该句，否则新添加数据源无法识别到
    }

    public Map<Object, Object> getDataSourceMap() {
        return dataSourceMap;
    }

    public static synchronized DynamicDataSource getInstance() {
        if (instance == null) {
            instance = new DynamicDataSource();
        }
        return instance;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String dbType = DataSourceContextHolder.getDBType();
        logger.info("DynamicDataSource determineCurrentLookupKey current datasource : {}", dbType);
        if (dbType == null) {
            dbType = DatasourceConstantPropertiKey.DEFAULT_MASTER.getKey();
            logger.info("DynamicDataSource determineCurrentLookupKey current datasource is null change default : {}", dbType);
        }
        return dbType;
    }

    @Override
    public Connection getConnection() throws SQLException {
        Map<String, ConnectionWarp> stringConnectionMap = connectionThreadLocal.get();
           if(stringConnectionMap == null){
               return determineTargetDataSource().getConnection();
           }else{
               Map<Object,Object> dataSourceMap =  new DynamicDataSource().getDataSourceMap();
               javax.sql.DataSource dataSource = (javax.sql.DataSource )dataSourceMap.get((String) determineCurrentLookupKey());
               ConnectionHolder connectionHolder = TranManager.currentConnectionHolder(dataSource);
               if(connectionHolder.getConnection() == null){
                   return determineTargetDataSource().getConnection();
               }
               return new ConnectionWarp(connectionHolder.getConnection());
           }
    }

    /**
     * 开启事物的时候,把连接放入 线程中,后续crud 都会拿对应的连接操作
     *
     * @param key
     * @param connection
     */
    public void bindConnection(String key, Connection connection) {
        Map<String, ConnectionWarp> connectionMap = connectionThreadLocal.get();
        if (connectionMap == null) {
            connectionMap = new HashMap<>();

        }
        ConnectionWarp connectWarp = new ConnectionWarp(connection);
        connectionMap.put(key, connectWarp);
        connectionThreadLocal.set(connectionMap);
    }



    protected void removeConnectionThreadLocal() {
        connectionThreadLocal.remove();
    }

}