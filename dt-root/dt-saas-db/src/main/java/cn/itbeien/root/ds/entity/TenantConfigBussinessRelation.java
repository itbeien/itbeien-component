package cn.itbeien.root.ds.entity;

import java.io.Serializable;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TenantConfigBussinessRelation implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5726766183090240735L;

	private Integer id;
	
	private Integer tenantConfigId;
	
	private String bussinessCode;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTenantConfigId() {
		return tenantConfigId;
	}

	public void setTenantConfigId(Integer tenantConfigId) {
		this.tenantConfigId = tenantConfigId;
	}

	public String getBussinessCode() {
		return bussinessCode;
	}

	public void setBussinessCode(String bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	@Override
	public String toString() {
		return "TenantConfigBussinessRelation{" +
				"id=" + id +
				", tenantConfigId=" + tenantConfigId +
				", bussinessCode='" + bussinessCode + '\'' +
				'}';
	}
}
