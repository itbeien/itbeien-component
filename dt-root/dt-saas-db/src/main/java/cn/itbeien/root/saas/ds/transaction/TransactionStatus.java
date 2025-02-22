package cn.itbeien.root.saas.ds.transaction;


import cn.itbeien.root.saas.ds.transaction.enums.Isolation;
import cn.itbeien.root.saas.ds.transaction.enums.Propagation;

import java.sql.SQLException;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface TransactionStatus {
    /**获取事务使用的传播行为*/
    public Propagation getTransactionBehavior();

    /**获取事务的隔离级别*/
    public Isolation getIsolationLevel();

    /**事务是否已经完成。
     * <p>当事务已经递交或者被回滚就标志着已完成。*/
    public boolean isCompleted();

    /**是否已被标记为回滚，如果返回值为 true 则在commit 时会回滚该事务。*/
    public boolean isRollbackOnly();

    /**是否为只读模式。*/
    public boolean isReadOnly();

    /**是否使用了一个全新的数据库连接开启事务*/
    public boolean isNewConnection();

    /**测试该事务是否被挂起*/
    public boolean isSuspend();

    /**表示事务是否携带了一个保存点，嵌套事务通常会创建一个保存点作为嵌套事务与上一层事务的分界点。
     * <p>注意：如果事务中包含保存点，则在递交事务时只处理这个保存点。*/
    public boolean hasSavepoint();

    /**设置事务状态为回滚，作为替代抛出异常进而触发回滚操作。
     * <p>只有当isCompleted 为 false 时设置该方法才会有效。 */
    public void setRollbackOnly() throws SQLException;

    /**设置事务状态为只读，当执行事务commit操作时，回滚该事务。
     * <p>只有当isCompleted 为 false 时设置该方法才会有效。*/
    public void setReadOnly() throws SQLException;
}
