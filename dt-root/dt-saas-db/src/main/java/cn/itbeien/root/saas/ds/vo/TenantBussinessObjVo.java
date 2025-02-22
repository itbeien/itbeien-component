package cn.itbeien.root.saas.ds.vo;

/**
 * 用于加载数据库中的数据
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TenantBussinessObjVo {

	private String tenantCode;
	
	private String bussinessObjCode;

	public String getTenantCode() {
		return tenantCode;
	}

	public void setTenantCode(String tenantCode) {
		this.tenantCode = tenantCode;
	}


	public String getBussinessObjCode() {
		return bussinessObjCode;
	}

	public void setBussinessObjCode(String bussinessObjCode) {
		this.bussinessObjCode = bussinessObjCode;
	}

	@Override
	public String toString() {
		return "TenantBussinessObjVo [tenantCode=" + tenantCode + ", bussinessObjCode=" + bussinessObjCode + "]";
	}
	
	
}
