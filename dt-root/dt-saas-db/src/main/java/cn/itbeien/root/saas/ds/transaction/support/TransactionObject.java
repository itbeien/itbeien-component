package cn.itbeien.root.saas.ds.transaction.support;


import cn.itbeien.root.saas.ds.transaction.ConnectionHolder;
import cn.itbeien.root.saas.ds.transaction.enums.Isolation;
import cn.itbeien.root.saas.ds.transaction.SavepointManager;
import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TransactionObject {

    private ConnectionHolder holder = null;
    private DataSource dataSource = null;
    private Isolation oriIsolationLevel; //创建事务对象时的隔离级别，当事物结束之后用以恢复隔离级别

    public TransactionObject(final ConnectionHolder holder, final Isolation oriIsolationLevel, final DataSource dataSource) throws SQLException {
        this.holder = holder;
        this.dataSource = dataSource;
        this.oriIsolationLevel = oriIsolationLevel;
    }

    public Isolation getOriIsolationLevel() {
        return this.oriIsolationLevel;
    }

    public ConnectionHolder getHolder() {
        return this.holder;
    }

    public DataSource getDataSource() {
        return this.dataSource;
    }

    public SavepointManager getSavepointManager() {
        return this.getHolder();
    }

    public void rollback() throws SQLException {
        if (this.holder.hasTransaction()) {
            this.holder.getConnection().rollback();//在AutoCommit情况下不执行事务操作（MYSQL强制在auto下执行该方法会引发异常）。
        }
    }

    public void commit() throws SQLException {
        if (this.holder.hasTransaction()) {
            this.holder.getConnection().commit(true);//在AutoCommit情况下不执行事务操作（MYSQL强制在auto下执行该方法会引发异常）。
        }
    }

    public boolean hasTransaction() throws SQLException {
        return this.holder.hasTransaction();
    }

    private boolean recoverMark = false;

    public void beginTransaction() throws SQLException {
        if (!this.holder.hasTransaction()) {
            this.recoverMark = true;
        }
        this.holder.setTransaction();
    }

    public void stopTransaction() throws SQLException {
        if (!this.recoverMark) {
            return;
        }
        this.recoverMark = false;
        this.holder.cancelTransaction();
    }
}
