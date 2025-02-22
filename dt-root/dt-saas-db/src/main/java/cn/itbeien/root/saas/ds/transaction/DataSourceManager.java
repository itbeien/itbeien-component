package cn.itbeien.root.saas.ds.transaction;

import javax.sql.DataSource;
import java.io.Closeable;
import java.lang.reflect.Proxy;
import java.sql.Connection;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class DataSourceManager {
    /**申请连接*/
    public static Connection newConnection(DataSource dataSource) {
        ConnectionHolder holder = newConnectionHolder(dataSource);
        return newProxyConnection(holder);
    }

    /**申请{@link ConnectionHolder}*/
    protected static ConnectionHolder newConnectionHolder(DataSource dataSource) {
        return new ConnectionHolder(dataSource);
    }

    /**获取与本地线程绑定的数据库连接，JDBC 框架会维护这个连接的事务。开发者不必关心该连接的事务管理，以及资源释放操作。*/
    protected static ConnectionProxy newProxyConnection(ConnectionHolder holder) {
        CloseSuppressingInvocationHandler handler = new CloseSuppressingInvocationHandler(holder);
        return (ConnectionProxy) Proxy.newProxyInstance(ConnectionProxy.class.getClassLoader(), new Class[] { ConnectionProxy.class, Closeable.class }, handler);
    }
}
