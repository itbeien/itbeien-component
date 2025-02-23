package cn.itbeien.root.ds.transaction;

import java.sql.Savepoint;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * 提供管理保存点和连接的方法
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class ConnectionHolder implements SavepointManager, ConnectionManager{
    private int        referenceCount;
    private DataSource dataSource;
    private ConnectionWarp connection;

    ConnectionHolder(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public synchronized void requested() {
        this.referenceCount++;
    }

    @Override
    public synchronized void released() throws SQLException {
        this.referenceCount--;
        if (!this.isOpen() && this.connection != null) {
            try {
                this.savepointCounter = 0;
                this.savepointSupported = null;
                this.connection.close(true);
            } finally {
                this.connection = null;
            }
        }
    }

    @Override
    public synchronized ConnectionWarp getConnection() throws SQLException {
        if (!this.isOpen()) {
            return null;
        }
        if (this.connection == null) {
            ConnectionWarp connectWarp = new ConnectionWarp(this.dataSource.getConnection());
            this.connection = connectWarp;
        }
        return this.connection;
    }

    @Override
    public boolean isOpen() {
        return this.referenceCount != 0;
    }

    /**则表示当前数据库连接是否有被引用。*/
    public DataSource getDataSource() {
        return dataSource;
    }

    /**是否存在事务*/
    public boolean hasTransaction() throws SQLException {
        ConnectionWarp conn = this.getConnection();
        if (conn == null) {
            return false;
        }
        //AutoCommit被标记为 false 表示开启了事务。
        return !conn.getAutoCommit();
    }

    /**设置事务状态*/
    public void setTransaction() throws SQLException {
        ConnectionWarp conn = this.getConnection();
        if (conn != null && conn.getAutoCommit()) {
            conn.setAutoCommit(false);
        }
    }

    /**取消事务状态*/
    public void cancelTransaction() throws SQLException {
        ConnectionWarp conn = this.getConnection();
        if (conn != null && !conn.getAutoCommit()) {
            conn.setAutoCommit(true);
        }
    }

    //---------------------------------------------------------------------------Savepoint
    private ConnectionWarp checkConn(final ConnectionWarp conn) throws SQLException {
        if (conn == null) {
            throw new SQLException("Connection is null.");
        }
        return conn;
    }

    private static final String  SAVEPOINT_NAME_PREFIX = "SAVEPOINT_";
    private              int     savepointCounter      = 0;
    private              Boolean savepointSupported;

    /**使用一个全新的名称创建一个保存点。*/
    @Override
    public Savepoint createSavepoint() throws SQLException {
        ConnectionWarp conn = this.checkConn(this.getConnection());
        this.savepointCounter++;
        return conn.setSavepoint(ConnectionHolder.SAVEPOINT_NAME_PREFIX + this.savepointCounter);
    }

    @Override
    public void rollbackToSavepoint(final Savepoint savepoint) throws SQLException {
        ConnectionWarp conn = this.checkConn(this.getConnection());
        conn.rollback(savepoint);
    }

    @Override
    public void releaseSavepoint(final Savepoint savepoint) throws SQLException {
        ConnectionWarp conn = this.checkConn(this.getConnection());
        conn.releaseSavepoint(savepoint);
    }

    @Override
    public boolean supportSavepoint() throws SQLException {
        ConnectionWarp conn = this.checkConn(this.getConnection());
        if (this.savepointSupported == null) {
            this.savepointSupported = conn.getMetaData().supportsSavepoints();
        }
        return this.savepointSupported;
    }
}
