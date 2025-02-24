package cn.itbeien.example;

import cn.itbeien.example.dao.master.user.SysUserMapper;
import cn.itbeien.example.dao.master.user.TenantSysUserMapper;
import cn.itbeien.example.entity.SysUser;
import cn.itbeien.root.service.ISaasModuleService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@SpringBootTest
@Slf4j
public class SaasDBTest {

    @Resource
    private ISaasModuleService saasModuleServiceImpl;
    @Resource
    private TenantSysUserMapper tenantSysUserMapper;
    @Resource
    private SysUserMapper sysUserMapper;

    /**
     * 查询租户库数据
     */
    @Test
    public void testTenant(){
        //更具业务商编号查询租户编号
        String tenantCode = saasModuleServiceImpl.getTenantByBussinessObjCode("S0001");

        SysUser sysUser = this.tenantSysUserMapper.selectByPrimaryKey(1l,tenantCode);

        log.info("sysUser:{}",sysUser.toString());
    }

    /**
     * 查询基础库数据
     */
    @Test
    public void test(){

        SysUser sysUser = this.sysUserMapper.selectByPrimaryKey(1l);

        log.info("sysUser:{}",sysUser.toString());
    }
}
