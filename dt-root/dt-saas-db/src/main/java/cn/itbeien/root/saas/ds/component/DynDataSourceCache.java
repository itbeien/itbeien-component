package cn.itbeien.root.saas.ds.component;

import cn.itbeien.root.saas.ds.constant.DbReadWriteModel;
import cn.itbeien.root.saas.service.ISaasModuleService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 租户数据源缓存
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Component("dynDataSourceCache")
public class DynDataSourceCache {

    @Autowired
    @Qualifier(value="saasModuleServiceImpl")
    private ISaasModuleService saasModuleService;

	//KEY 租户+dbMode , value DatabaseConfig.ID
    private Map<String,  List<Integer>> tenantToDBMap = new ConcurrentHashMap<>();


    public Integer getDataSourceKey(String tenantCode, DbReadWriteModel dbReadWriteModel){

    	final String tenantMapKey = tenantCode + dbReadWriteModel.toString();

        if(this.tenantToDBMap.isEmpty()){
            loadTenantDataSource();
        }

        Integer databaseId = null;
        if(tenantToDBMap.containsKey(tenantMapKey)){
            Integer tenantSize = tenantToDBMap.get(tenantMapKey).size();
            if(tenantSize == 0){
                return null;
            }
            databaseId = tenantToDBMap.get(tenantMapKey).get(randomNumber(tenantToDBMap.get(tenantMapKey).size()));
        } else {

        	Map<String,  List<Integer>> tempMap = this.saasModuleService.loadTenantConfig(tenantCode);
        	tenantToDBMap.putAll(tempMap);
        	if(tenantToDBMap.get(tenantMapKey) == null){
        	    return null;
            }
        	databaseId = tenantToDBMap.get(tenantMapKey).get(randomNumber(tenantToDBMap.get(tenantMapKey).size()));
        }
        return databaseId;
    }

    @PostConstruct
    public void loadTenantDataSource(){
        //加载租户数据源
        tenantToDBMap = this.saasModuleService.loadTenantDataSource();
    }

    private int randomNumber(int max){
        int min = 0;
        Random random = new Random();
        int s = random.nextInt(max)%(max-min+1) + min;
        return s;
    }

    //key 租户+dbMode
    public void removeTenantToDBMap(String key, Integer dataBaseId){
        List<Integer>  dataBaseIds = tenantToDBMap.get(key);
        if(dataBaseIds != null){
            dataBaseIds.remove(dataBaseId);
            if(dataBaseIds.isEmpty()){
                tenantToDBMap.remove(key);
            }else {
                tenantToDBMap.put(key, dataBaseIds);
            }
        }
    }
}
