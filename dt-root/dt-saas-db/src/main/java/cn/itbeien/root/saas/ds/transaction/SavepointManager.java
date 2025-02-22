package cn.itbeien.root.saas.ds.transaction;

import java.sql.SQLException;
import java.sql.Savepoint;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface SavepointManager {
    /**返回 JDBC 驱动是否支持保存点。*/
    public boolean supportSavepoint() throws SQLException;

    /**创建事务的保存点，通过<code>releaseSavepoint</code>方法释放这个保存点。SQLException */
    public Savepoint createSavepoint() throws SQLException;

    /**回滚事务到一个指定的保存点。*/
    public void rollbackToSavepoint(Savepoint savepoint) throws SQLException;

    /**释放某个事务的保存点*/
    public void releaseSavepoint(Savepoint savepoint) throws SQLException;
}
