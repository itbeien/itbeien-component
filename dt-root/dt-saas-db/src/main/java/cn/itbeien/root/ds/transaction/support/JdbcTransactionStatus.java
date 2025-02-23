package cn.itbeien.root.ds.transaction.support;

import cn.itbeien.root.ds.transaction.TransactionStatus;
import cn.itbeien.root.ds.transaction.enums.Isolation;
import cn.itbeien.root.ds.transaction.enums.Propagation;

import cn.itbeien.root.ds.transaction.SavepointManager;

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
public class JdbcTransactionStatus implements TransactionStatus {
    private Savepoint savepoint     = null;  //事务保存点
    private TransactionObject tranConn      = null;  //当前事务使用的数据库连接
    private TransactionObject suspendConn   = null;  //当前事务之前挂起的上一个数据库事务
    private Propagation behavior      = null;  //传播属性
    private Isolation level         = null;  //隔离级别
    private boolean           completed     = false; //完成（true表示完成）
    private boolean           rollbackOnly  = false; //要求回滚（true表示回滚）
    private boolean           newConnection = false; //是否使用了一个全新的数据库连接开启事务（true表示新连接）
    private boolean           readOnly      = false; //只读模式（true表示只读）

    public JdbcTransactionStatus(final Propagation behavior, final Isolation level) {
        this.behavior = behavior;
        this.level = level;
    }

    private SavepointManager getSavepointManager() {
        return this.tranConn.getSavepointManager();
    }

    public void markHeldSavepoint() throws SQLException {
        if (this.hasSavepoint()) {
            throw new SQLException("TransactionStatus has Savepoint");
        }
        if (!this.getSavepointManager().supportSavepoint()) {
            throw new SQLException("SavepointManager does not support Savepoint.");
        }
        this.savepoint = this.getSavepointManager().createSavepoint();
    }

    public void releaseHeldSavepoint() throws SQLException {
        if (!this.hasSavepoint()) {
            throw new SQLException("TransactionStatus has not Savepoint");
        }
        if (!this.getSavepointManager().supportSavepoint()) {
            throw new SQLException("SavepointManager does not support Savepoint.");
        }
        this.getSavepointManager().releaseSavepoint(this.savepoint);
    }

    public void rollbackToHeldSavepoint() throws SQLException {
        if (!this.hasSavepoint()) {
            throw new SQLException("TransactionStatus has not Savepoint");
        }
        if (!this.getSavepointManager().supportSavepoint()) {
            throw new SQLException("SavepointManager does not support Savepoint.");
        }
        this.getSavepointManager().rollbackToSavepoint(this.savepoint);
    }

    /*设置完成状态*/
    void setCompleted() {
        this.completed = true;
    }

    /*标记使用的是全新连接*/
    void markNewConnection() {
        this.newConnection = true;
    }

    TransactionObject getTranConn() {
        return this.tranConn;
    }

    void setTranConn(final TransactionObject tranConn) {
        this.tranConn = tranConn;
    }

    TransactionObject getSuspendConn() {
        return this.suspendConn;
    }

    void setSuspendConn(final TransactionObject suspendConn) {
        this.suspendConn = suspendConn;
    }

    @Override
    public Propagation getTransactionBehavior() {
        return this.behavior;
    }

    @Override
    public Isolation getIsolationLevel() {
        return this.level;
    }

    @Override
    public boolean isCompleted() {
        return this.completed;
    }

    @Override
    public boolean isRollbackOnly() {
        return this.rollbackOnly;
    }

    @Override
    public boolean isReadOnly() {
        return this.readOnly;
    }

    @Override
    public boolean isNewConnection() {
        return this.newConnection;
    }

    @Override
    public boolean isSuspend() {
        return this.suspendConn != null;
    }

    @Override
    public boolean hasSavepoint() {
        return this.savepoint != null;
    }

    @Override
    public void setRollbackOnly() throws SQLException {
        if (this.isCompleted()) {
            throw new SQLException("Transaction is already completed.");
        }
        this.rollbackOnly = true;
    }

    @Override
    public void setReadOnly() throws SQLException {
        if (this.isCompleted()) {
            throw new SQLException("Transaction is already completed.");
        }
        this.readOnly = true;
    }
}
