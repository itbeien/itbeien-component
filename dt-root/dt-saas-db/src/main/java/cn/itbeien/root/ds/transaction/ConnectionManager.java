package cn.itbeien.root.ds.transaction;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 连接管理器
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface ConnectionManager {

    public void requested();

    public void released() throws SQLException;

    public Connection getConnection() throws SQLException;

    public boolean isOpen();
}
