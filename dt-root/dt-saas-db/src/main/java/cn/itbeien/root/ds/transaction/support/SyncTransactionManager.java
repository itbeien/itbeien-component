package cn.itbeien.root.ds.transaction.support;

import cn.itbeien.root.ds.transaction.TranManager;

import javax.sql.DataSource;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class SyncTransactionManager extends TranManager {
    // 设置同步事务
    public static void setSync(TransactionObject tranConn) {
        // 获取数据源和连接持有者
        currentConnection(tranConn.getDataSource(), tranConn.getHolder());
    }

    // 清除同步事务
    public static void clearSync(DataSource dataSource) {
        // 清除数据源和连接持有者
        currentConnection(dataSource, null);
    }
}
