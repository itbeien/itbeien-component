package cn.itbeien.root.saas.ds.transaction.support;

import cn.itbeien.root.saas.ds.transaction.*;
import cn.itbeien.root.saas.ds.transaction.enums.Isolation;
import cn.itbeien.root.saas.ds.transaction.enums.Propagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Objects;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class JdbcTransactionManager implements TransactionManager {

    protected Logger logger              = LoggerFactory.getLogger(getClass());
    private LinkedList<JdbcTransactionStatus> tStatusStack        = new LinkedList<>();
    private DataSource dataSource          = null;
    private   TransactionTemplateManager        transactionTemplate = null;

    protected JdbcTransactionManager(final DataSource dataSource) {
        Objects.requireNonNull(dataSource);
        this.dataSource = dataSource;
        this.transactionTemplate = new TransactionTemplateManager(this);
    }

    /**获取当前事务管理器管理的数据源对象。*/
    public DataSource getDataSource() {
        return this.dataSource;
    }

    /**是否存在未处理完的事务（包括被挂起的事务）。*/
    @Override
    public boolean hasTransaction() {
        return !this.tStatusStack.isEmpty();
    }

    /**测试事务状态是否位于栈顶。*/
    @Override
    public boolean isTopTransaction(final TransactionStatus status) {
        if (this.tStatusStack.isEmpty()) {
            return false;
        }
        return this.tStatusStack.peek() == status;
    }

    /**开启事务*/
    @Override
    public final TransactionStatus getTransaction(final Propagation behavior) throws SQLException {
        return this.getTransaction(behavior, Isolation.READ_UNCOMMITTED);
    }

    /**开启事务*/
    @Override
    public final TransactionStatus getTransaction(final Propagation behavior, final Isolation level) throws SQLException {
        Objects.requireNonNull(behavior);
        Objects.requireNonNull(level);
        //1.获取连接
        JdbcTransactionStatus defStatus = new JdbcTransactionStatus(behavior, level);
        defStatus.setTranConn(this.doGetConnection(defStatus));
        this.tStatusStack.addFirst(defStatus);/*入栈*/

        if (this.isExistingTransaction(defStatus)) {
            /*REQUIRES_NEW：独立事务*/
            if (behavior == Propagation.REQUIRES_NEW) {
                this.suspend(defStatus);/*挂起当前事务*/
                this.doBegin(defStatus);/*开启新事务*/
            }
            /*NESTED：嵌套事务*/
            if (behavior == Propagation.NESTED) {
                defStatus.markHeldSavepoint();/*设置保存点*/
            }
            /*NOT_SUPPORTED：非事务方式*/
            if (behavior == Propagation.NOT_SUPPORTED) {
                this.suspend(defStatus);/*挂起事务*/
            }
            /*NEVER：排除事务*/
            if (behavior == Propagation.NEVER) {
                this.cleanupAfterCompletion(defStatus);
                throw new SQLException("Existing transaction found for transaction marked with propagation 'never'");
            }
            return defStatus;
        }

        /*REQUIRED：加入已有事务*/
        if (behavior == Propagation.REQUIRED ||
                /*REQUIRES_NEW：独立事务*/
                behavior == Propagation.REQUIRES_NEW ||
                /*NESTED：嵌套事务*/
                behavior == Propagation.NESTED) {
            this.doBegin(defStatus);/*开启新事务*/
        }
        /*MANDATORY：强制要求事务*/
        if (behavior == Propagation.MANDATORY) {
            this.cleanupAfterCompletion(defStatus);
            throw new SQLException("No existing transaction found for transaction marked with propagation 'mandatory'");
        }
        return defStatus;
    }

    /**判断连接对象是否处于事务中，该方法会用于评估事务传播属性的处理方式。 */
    private boolean isExistingTransaction(final JdbcTransactionStatus defStatus) throws SQLException {
        return defStatus.getTranConn().hasTransaction();
    }

    /**初始化一个新的连接，并开启事务。*/
    protected void doBegin(final JdbcTransactionStatus defStatus) throws SQLException {
        TransactionObject tranConn = defStatus.getTranConn();
        tranConn.beginTransaction();
    }

    /**递交事务*/
    @Override
    public final void commit(final TransactionStatus status) throws SQLException {
        JdbcTransactionStatus defStatus = (JdbcTransactionStatus) status;
        /*已完毕，不需要处理*/
        if (defStatus.isCompleted()) {
            throw new SQLException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }
        /*回滚情况*/
        if (defStatus.isReadOnly() || defStatus.isRollbackOnly()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Transactional code has requested rollback");
            }
            this.rollBack(defStatus);
            return;
        }

        try {
            this.prepareCommit(defStatus);
            /*如果包含保存点，在递交事务时只处理保存点*/
            if (defStatus.hasSavepoint()) {
                defStatus.releaseHeldSavepoint();
            } else if (defStatus.isNewConnection()) {
                this.doCommit(defStatus);
            }
        } catch (SQLException ex) {
            this.doRollback(defStatus);/*失败，回滚*/
            throw ex;
        } finally {
            this.cleanupAfterCompletion(defStatus);
        }
    }

    /**递交前的预处理*/
    private void prepareCommit(final JdbcTransactionStatus defStatus) throws SQLException {
        /*首先预处理的事务必须存在于管理器的事务栈内某一位置中，否则要处理的事务并非来源于该事务管理器。*/
        if (!this.tStatusStack.contains(defStatus)) {
            throw new SQLException("This transaction is not derived from this Manager.");
        }

        TransactionStatus inStackStatus = null;
        while ((inStackStatus = this.tStatusStack.peek()) != defStatus) {
            this.commit(inStackStatus);
        }
    }

    /**处理当前底层数据库连接的事务递交操作。*/
    protected void doCommit(final JdbcTransactionStatus defStatus) throws SQLException {
        TransactionObject tranObject = defStatus.getTranConn();
        tranObject.commit();
    }

    /**回滚事务*/
    @Override
    public final void rollBack(final TransactionStatus status) throws SQLException {
        JdbcTransactionStatus defStatus = (JdbcTransactionStatus) status;
        /*已完毕，不需要处理*/
        if (defStatus.isCompleted()) {
            throw new SQLException("Transaction is already completed - do not call commit or rollback more than once per transaction");
        }

        try {
            this.prepareRollback(defStatus);
            /*如果包含保存点，在递交事务时只处理保存点*/
            if (defStatus.hasSavepoint()) {
                defStatus.rollbackToHeldSavepoint();
            } else if (defStatus.isNewConnection()) {
                this.doRollback(defStatus);
            }
        } catch (SQLException ex) {
            this.doRollback(defStatus);
            throw ex;
        } finally {
            this.cleanupAfterCompletion(defStatus);
        }
    }

    /**回滚前的预处理*/
    private void prepareRollback(final JdbcTransactionStatus defStatus) throws SQLException {
        /*首先预处理的事务必须存在于管理器的事务栈内某一位置中，否则要处理的事务并非来源于该事务管理器。*/
        if (!this.tStatusStack.contains(defStatus)) {
            throw new SQLException("This transaction is not derived from this Manager.");
        }

        TransactionStatus inStackStatus = null;
        while ((inStackStatus = this.tStatusStack.peek()) != defStatus) {
            this.rollBack(inStackStatus);
        }
    }

    /**处理当前底层数据库连接的事务回滚操作。*/
    protected void doRollback(final JdbcTransactionStatus defStatus) throws SQLException {
        TransactionObject tranObject = defStatus.getTranConn();
        tranObject.rollback();
    }

    /**挂起事务。*/
    protected final void suspend(final JdbcTransactionStatus defStatus) throws SQLException {
        /*事务已经被挂起*/
        if (defStatus.isSuspend()) {
            throw new SQLException("the Transaction has Suspend.");
        }
        /*挂起事务*/
        TransactionObject tranConn = defStatus.getTranConn();
        defStatus.setSuspendConn(tranConn);/*挂起*/
        SyncTransactionManager.clearSync(this.getDataSource());/*清除线程上的同步事务*/
        defStatus.setTranConn(this.doGetConnection(defStatus));/*重新申请数据库连接*/
    }

    /**恢复被挂起的事务。*/
    protected final void resume(final JdbcTransactionStatus defStatus) throws SQLException {
        if (!defStatus.isCompleted()) {
            throw new SQLException("the Transaction has not completed.");
        }
        if (!defStatus.isSuspend()) {
            throw new SQLException("the Transaction has not Suspend.");
        }
        /*恢复挂起的事务*/
        if (defStatus.isSuspend()) {
            TransactionObject tranConn = defStatus.getSuspendConn();/*取得挂起的数据库连接*/
            SyncTransactionManager.setSync(tranConn);/*设置线程的数据库连接*/
            defStatus.setTranConn(tranConn);
            defStatus.setSuspendConn(null);
            tranConn.getHolder().released();
        }
    }


    /**commit,rollback。之后的清理工作，同时也负责恢复事务和操作事务堆栈。*/
    private void cleanupAfterCompletion(final JdbcTransactionStatus defStatus) throws SQLException {
        /*标记完成*/
        defStatus.setCompleted();
        /*释放资源*/
        /*恢复当时的隔离级别*/
        TransactionObject tranObj = defStatus.getTranConn();
        //Isolation transactionIsolation = tranObj.getOriIsolationLevel();
        tranObj.getHolder().released();//ref--
        tranObj.stopTransaction();
        /*恢复挂起的事务*/
        if (defStatus.isSuspend()) {
            this.resume(defStatus);
        }
        /*清理defStatus*/
        this.tStatusStack.removeFirst();

        defStatus.setTranConn(null);
        defStatus.setSuspendConn(null);
    }

    /**获取数据库连接（线程绑定的）*/
    protected TransactionObject doGetConnection(final JdbcTransactionStatus defStatus) throws SQLException {
        ConnectionHolder holder = TranManager.currentConnectionHolder(dataSource);
        if (!holder.isOpen() || !holder.hasTransaction()) {
            defStatus.markNewConnection();/*新事物，新连接*/
        }
        holder.requested();//ref++

        int isolationLevel = holder.getConnection().getTransactionIsolation();
        int  isolation = defStatus.getIsolationLevel().getValue();
        if(isolationLevel!= isolation){
            isolationLevel = isolation;
            holder.getConnection().setTransactionIsolation(isolationLevel);
        }
        return new TransactionObject(holder, Isolation.valueOf(isolationLevel), this.getDataSource());
    }

    public TransactionTemplate getTransactionTemplate() {
        return this.transactionTemplate;
    }
}
