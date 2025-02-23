package cn.itbeien.root.ds.transaction;


import cn.itbeien.root.ds.transaction.enums.Isolation;
import cn.itbeien.root.ds.transaction.enums.Propagation;

import java.sql.SQLException;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface TransactionManager {
    /**开启事务，使用默认事务隔离级别。
     * @see Propagation
     * @see TransactionManager#getTransaction(Propagation, Isolation)*/
    public TransactionStatus getTransaction(Propagation behavior) throws SQLException;

    /**开启事务
     * @see Propagation
     * @see java.sql.Connection#setTransactionIsolation(int)*/
    public TransactionStatus getTransaction(Propagation behavior, Isolation level) throws SQLException;

    /**递交事务
     * <p>如果递交的事务并不处于事务堆栈顶端，会同时递交该事务的后面其它事务。*/
    public void commit(TransactionStatus status) throws SQLException;

    /**回滚事务*/
    public void rollBack(TransactionStatus status) throws SQLException;

    /**是否存在未处理完的事务（包括被挂起的事务）。*/
    public boolean hasTransaction();

    /**测试事务状态是否位于栈顶。*/
    public boolean isTopTransaction(TransactionStatus status);
}
