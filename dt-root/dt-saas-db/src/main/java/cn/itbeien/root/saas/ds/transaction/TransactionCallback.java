package cn.itbeien.root.saas.ds.transaction;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@FunctionalInterface
public interface TransactionCallback<T> {
    /**
     * 执行事务,如需回滚事务,只需要调用 tranStatus 的 setRollbackOnly() 方法即可。
     * 请注意:异常的抛出一会引起事务的回滚。 */
    public T doTransaction(TransactionStatus tranStatus) throws Throwable;
}
