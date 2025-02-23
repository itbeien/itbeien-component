package cn.itbeien.root.config;

import cn.itbeien.root.dao.master.TenantConfigDao;
import cn.itbeien.root.ds.component.DynamicDataSourceManager;
import cn.itbeien.root.ds.transaction.DynamicDataSource;
import cn.itbeien.root.ds.vo.TenantConfigVO;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Configuration
public class TenantDataSourceInit {

    @Resource
    private TenantConfigDao tenantConfigDao;

    @Resource
    private DynamicDataSourceManager dynamicDataSourceManager;

    @Bean
    public DynamicDataSource tenantDataSource() {
        List<TenantConfigVO> tenantConfigVOS = tenantConfigDao.loadTenantConfig();
        DynamicDataSource dynamicDataSource = null;
        for(TenantConfigVO tenantConfig : tenantConfigVOS){
            dynamicDataSource = dynamicDataSourceManager.createDataSource(tenantConfig.getTenantCode(), tenantConfig.getDatabaseId());
        }
        return dynamicDataSource;
    }

}
