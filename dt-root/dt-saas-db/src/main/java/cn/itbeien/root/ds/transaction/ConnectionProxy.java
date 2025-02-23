package cn.itbeien.root.ds.transaction;

import javax.sql.DataSource;
import java.io.Closeable;
import java.sql.Connection;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface ConnectionProxy extends Connection, Closeable {
    /**
     * Return the target Connection of this proxy. <p>This will typically be the native driver Connection or a wrapper from a connection pool.
     * @return the underlying Connection (never <code>null</code>)
     */
    public Connection getTargetConnection();

    /**获取目标使用的数据源*/
    public DataSource getTargetSource();
}
