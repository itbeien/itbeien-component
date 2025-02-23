package cn.itbeien.example.service.impl;

import cn.itbeien.example.dao.master.user.SysUserMapper;
import cn.itbeien.example.entity.SysUser;
import cn.itbeien.example.service.IUserService;
import cn.itbeien.root.ds.transaction.TransactionMulti;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Service
public class UserService implements IUserService{

    @Resource
    private SysUserMapper sysUserMapper;

    @TransactionMulti
    @Override
    public void insertUser(SysUser user) {
        sysUserMapper.insertSelective(user);
    }


    @Override
    public SysUser getUserById(Long id) {
        return this.sysUserMapper.selectByPrimaryKey(id);
    }
}
