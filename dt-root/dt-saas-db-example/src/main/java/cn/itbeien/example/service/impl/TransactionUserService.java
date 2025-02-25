package cn.itbeien.example.service.impl;

import cn.itbeien.example.dao.master.user.SysUserMapper;
import cn.itbeien.example.dao.master.user.TenantSysUserMapper;
import cn.itbeien.example.entity.SysUser;
import cn.itbeien.root.ds.transaction.TransactionMulti;
import cn.itbeien.root.service.ISaasModuleService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import cn.itbeien.example.service.ITransactionUserService;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Service
public class TransactionUserService implements ITransactionUserService{
    /**
     * 租户库数据持久层
     */
    @Resource
    private TenantSysUserMapper tenantSysUserMapper;
    /**
     * 基础库数据持久层
     */
    @Resource
    private SysUserMapper sysUserMapper;
    /**
     * 用于获取租户相关信息
     */
    @Resource
    private ISaasModuleService saasModuleServiceImpl;

    /**
     * 基础库使用多数据源事务
     * @param user
     */
    @Override
    @TransactionMulti
    public void insertUser(SysUser user) {
        this.sysUserMapper.insertSelective(user);
        //模拟异常,测试事务回滚
        int i= 1/0;
    }
    /**
     * 租户库使用多数据源事务
     * @param user
     */
    @Override
    @TransactionMulti(datasource = "spCode")
    public void insertTenantUser(String spCode,SysUser user) {
        //获取租户编码
        String tenantCode = saasModuleServiceImpl.getTenantByBussinessObjCode(spCode);
        this.tenantSysUserMapper.insertSelective(user,tenantCode);
        //模拟异常，测试事务回滚
        int i= 1/0;
    }
}
