package cn.itbeien.root.service;

import cn.itbeien.root.dao.master.TenantConfigDao;
import cn.itbeien.root.ds.entity.DatabaseConfig;
import cn.itbeien.root.ds.vo.TenantBussinessObjVo;
import cn.itbeien.root.ds.vo.TenantConfigVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import jakarta.annotation.Resource;

/**
 * saas模块业务实现，用于获取租户数据源信息
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Service
public class SaasModuleServiceImpl implements ISaasModuleService {

	@Resource
	private TenantConfigDao tenantConfigDao;

	private static Map<String,String> tenantCodeMap = new HashMap<>();

	private static Map<String,String> masterTenantCodeMap = new HashMap<>();
	private static Map<String,Integer> masterDBMap = new HashMap<>();


	@Override
	public Map<String, List<Integer>>  loadTenantConfig(String tenantCode) {
		
		Map<String, List<Integer>> tenantDataSource = new HashMap<>();
		
		List<TenantConfigVO>  tenantConfigVOs =   this.tenantConfigDao.loadTenantConfigByTenantCode(tenantCode);
		for(TenantConfigVO tenantConfigVO : tenantConfigVOs){
			doListAddMapLogic(tenantDataSource, tenantConfigVO);
		}
		return tenantDataSource;
	}


	@Override
	public Map<String, List<Integer>> loadTenantDataSource() {
		Map<String, List<Integer>> tenantDataSource = new HashMap<>();
		List<TenantConfigVO>  tenantConfigVOs =   this.tenantConfigDao.loadTenantConfig();
		for(TenantConfigVO tenantConfigVO : tenantConfigVOs){
			
			doListAddMapLogic(tenantDataSource, tenantConfigVO);
		}
		return tenantDataSource;
	}
	
	private void doListAddMapLogic(Map<String, List<Integer>> tenantDataSource, TenantConfigVO tenantConfigVO) {
		
		
		String mapKey = tenantConfigVO.getTenantCode() + tenantConfigVO.getDbModel();
		
		if(tenantDataSource.containsKey(mapKey)){
			tenantDataSource.get(mapKey).add(tenantConfigVO.getDatabaseId());
		} else {
			List<Integer> dbList = new ArrayList<>();
			dbList.add(tenantConfigVO.getDatabaseId());
			tenantDataSource.put(mapKey, dbList);
		}
	}

	@Override
	public String getTenantByBussinessObjCode(String bussinessObjCode) {
		if(!tenantCodeMap.containsKey(bussinessObjCode)){
			List<TenantBussinessObjVo> tenantConfigVOS =  this.tenantConfigDao.loadTenantCode();
			Map<String, String> maps = tenantConfigVOS.stream().collect(Collectors.toMap(TenantBussinessObjVo::getBussinessObjCode, TenantBussinessObjVo::getTenantCode, (key1, key2) -> key2));
			tenantCodeMap.putAll(maps);
		}
		return tenantCodeMap.get(bussinessObjCode);
	}


	@Override
	public Integer getMasterDBByTenantCode(String tenantCode) throws Exception {
		if(!masterDBMap.containsKey(tenantCode)){
			List<TenantConfigVO> tenantConfigVOS =  this.tenantConfigDao.loadMasterTenantCode(tenantCode);
			
			if(tenantConfigVOS.isEmpty() || tenantConfigVOS.size() > 1) {
				throw new Exception("租户的主数据源不存在或者存在多个, tenantCode: " + tenantCode);
			}
			
			TenantConfigVO masterTenantDs = tenantConfigVOS.get(0);
			masterDBMap.put(masterTenantDs.getTenantCode(), masterTenantDs.getDatabaseId());
		}
		return masterDBMap.get(tenantCode);
	}

	
	
	@Override
	public DatabaseConfig loadDatabaseConfig(Integer databaseId) {
		return tenantConfigDao.findDatabaseConfigByDatabaseId(databaseId);
	}

	
	public static void cleanMap() {
		 tenantCodeMap.clear();
		 masterTenantCodeMap.clear();
	}

}
