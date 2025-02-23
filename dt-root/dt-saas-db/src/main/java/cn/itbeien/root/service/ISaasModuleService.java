package cn.itbeien.root.service;

import cn.itbeien.root.ds.entity.DatabaseConfig;

import java.util.List;
import java.util.Map;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface ISaasModuleService {
	public Map<String, List<Integer>>  loadTenantConfig(String tenantCode);
	
	public DatabaseConfig loadDatabaseConfig(Integer databaseId);
	
	public Map<String, List<Integer>> loadTenantDataSource();

	public String getTenantByBussinessObjCode(String bussinessObjCode);

	public Integer getMasterDBByTenantCode(String tenantCode) throws Exception;
}
