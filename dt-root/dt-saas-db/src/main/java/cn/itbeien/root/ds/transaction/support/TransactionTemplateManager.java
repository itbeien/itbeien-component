package cn.itbeien.root.ds.transaction.support;


import cn.itbeien.root.ds.transaction.TransactionCallback;
import cn.itbeien.root.ds.transaction.TransactionManager;
import cn.itbeien.root.ds.transaction.TransactionStatus;
import cn.itbeien.root.ds.transaction.TransactionTemplate;
import cn.itbeien.root.ds.transaction.enums.Isolation;
import cn.itbeien.root.ds.transaction.enums.Propagation;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TransactionTemplateManager implements TransactionTemplate {
    // 事务管理器
    private TransactionManager transactionManager;

    // 构造函数，传入事务管理器
    public TransactionTemplateManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    // 执行事务回调函数，默认传播行为和隔离级别
    @Override
    public <T> T execute(TransactionCallback<T> callBack) throws Throwable {
        return this.execute(callBack, Propagation.REQUIRED, Isolation.DEFAULT);
    }

    // 执行事务回调函数，指定传播行为，默认隔离级别
    @Override
    public <T> T execute(TransactionCallback<T> callBack, Propagation behavior) throws Throwable {
        return this.execute(callBack, behavior, Isolation.DEFAULT);
    }

    // 执行事务回调函数，指定传播行为和隔离级别
    @Override
    public <T> T execute(TransactionCallback<T> callBack, Propagation behavior, Isolation level) throws Throwable {
        // 获取事务状态
        TransactionStatus tranStatus = null;
        try {
            // 开始事务
            tranStatus = transactionManager.getTransaction(behavior, level);
            // 执行事务回调函数
            return callBack.doTransaction(tranStatus);
        } catch (Throwable e) {
            // 如果事务状态不为空，则设置回滚
            if (tranStatus != null) {
                tranStatus.setRollbackOnly();
            }
            // 抛出异常
            throw e;
        } finally {
            // 如果事务状态不为空且未完成，则提交事务
            if (tranStatus != null && !tranStatus.isCompleted()) {
                transactionManager.commit(tranStatus);
            }
        }
    }

}
