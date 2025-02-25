package cn.itbeien.example.service;

import cn.itbeien.example.entity.SysUser;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface ITransactionUserService {

    public void insertUser(SysUser user);

    public void insertTenantUser(String spCode,SysUser user);
}
