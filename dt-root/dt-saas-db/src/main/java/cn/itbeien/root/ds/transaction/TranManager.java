package cn.itbeien.root.ds.transaction;


import cn.itbeien.root.ds.transaction.support.JdbcTransactionManager;

import javax.sql.DataSource;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TranManager extends DataSourceManager {
    private final static ThreadLocal<ConcurrentMap<DataSource, JdbcTransactionManager>> managerMap = ThreadLocal.withInitial(ConcurrentHashMap::new);
    private final static ThreadLocal<ConcurrentMap<DataSource, ConnectionHolder>>       currentMap = ThreadLocal.withInitial(ConcurrentHashMap::new);

    public static ConnectionHolder currentConnectionHolder(DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        ConcurrentMap<DataSource, ConnectionHolder> localMap = currentMap.get();
        ConnectionHolder holder = localMap.get(dataSource);
        if (holder == null) {
            holder = localMap.putIfAbsent(dataSource, newConnectionHolder(dataSource));
            holder = localMap.get(dataSource);
        }
        return holder;
    }


    /**改变当前{@link ConnectionHolder}*/
    protected static void currentConnection(DataSource dataSource, ConnectionHolder holder) {
        ConcurrentMap<DataSource, ConnectionHolder> localMap = currentMap.get();
        if (holder == null) {
            localMap.remove(dataSource);
        } else {
            localMap.put(dataSource, holder);
        }
    }

    /**获取事务管理器*/
    private static synchronized JdbcTransactionManager getTransactionManager(final DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        ConcurrentMap<DataSource, JdbcTransactionManager> localMap = managerMap.get();
        JdbcTransactionManager manager = localMap.get(dataSource);
        if (manager == null) {
            manager = localMap.putIfAbsent(dataSource, new JdbcTransactionManager(dataSource) {
            });
            manager = localMap.get(dataSource);
        }
        return manager;
    }

    /**获取{@link TransactionManager}*/
    public static synchronized TransactionManager getManager(DataSource dataSource) {
        return getTransactionManager(dataSource);
    }

}
