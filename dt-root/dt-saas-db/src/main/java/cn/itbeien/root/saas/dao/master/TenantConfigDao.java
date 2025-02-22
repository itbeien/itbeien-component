package cn.itbeien.root.saas.dao.master;

import java.util.List;

import cn.itbeien.root.saas.ds.constant.DbReadWriteModel;
import cn.itbeien.root.saas.ds.entity.DatabaseConfig;
import org.apache.ibatis.annotations.Param;

import cn.itbeien.root.saas.ds.component.DataSource;
import cn.itbeien.root.saas.ds.vo.TenantBussinessObjVo;
import cn.itbeien.root.saas.ds.vo.TenantConfigVO;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@DataSource(DbReadWriteModel.READ_WRITE)
public interface TenantConfigDao {

	public List<TenantConfigVO> loadTenantConfig();

	public List<TenantConfigVO> loadTenantConfigByTenantCode(@Param("tCode")String tenantCode);

    public DatabaseConfig findDatabaseConfigByDatabaseId(@Param("id") Integer id);

    public List<TenantBussinessObjVo> loadTenantCode();

    public List<TenantConfigVO> loadMasterTenantCode(@Param("tCode")String tenantCode);

}
