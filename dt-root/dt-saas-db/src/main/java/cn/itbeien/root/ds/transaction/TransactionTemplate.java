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
public interface TransactionTemplate {
    /**
     * 开始执行一个事务。
     * @param callBack 调用方法执行事务。
     * @return 返回 {@link TransactionCallback} 接口执行的返回值。
     * @throws SQLException 执行期间发生SQL异常
     */
    public <T> T execute(TransactionCallback<T> callBack) throws Throwable;

    /**
     * 开始执行一个事务。
     * @param callBack 调用方法执行事务。
     * @param behavior 传播属性
     * @return 返回 {@link TransactionCallback} 接口执行的返回值。
     * @throws SQLException 执行期间发生SQL异常
     */
    public <T> T execute(TransactionCallback<T> callBack, Propagation behavior) throws Throwable;

    /**
     * 开始执行一个事务。
     * @param callBack 调用方法执行事务。
     * @param behavior 传播属性
     * @param level 事务隔离级别
     * @return 返回 {@link TransactionCallback} 接口执行的返回值。
     * @throws SQLException 执行期间发生SQL异常
     */
    public <T> T execute(TransactionCallback<T> callBack, Propagation behavior, Isolation level) throws Throwable;
}
