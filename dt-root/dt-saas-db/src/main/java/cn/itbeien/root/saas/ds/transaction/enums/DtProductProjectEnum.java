package cn.itbeien.root.saas.ds.transaction.enums;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public enum DtProductProjectEnum implements IDtProductProject {

	DT_TOURISM_MANAGER("DT.MANAGER", "聚合支付-运营管理后台"),

	DT_TOURISM_PAYMENT("DT.PAYMENT", "聚合支付-支付交易系统"),

	DT_TOURISM_GATEWAY("DT.GATEWAY", "聚合支付-网关系统"),

	DT_TOURISM_MERCHANT("DT.MERCHANT", "聚合支付-商户平台"),

	DT_TOURISM_CRM("DT.CRM", "CRM平台"),

	DT_TOURISM_DC("DT.DC", "数据中心"),

	DT_TOURISM_TAX("DT.TAX", "发票服务"),

	DT_TOURISM_FC("DT.FC", "人脸平台"),

	DT_BASE_COMPONENT("DT.COMPONENT", "基础组件"),

	DT_MALL_MANAGER("DT.MM", "商城管理后台")


	;
	/**
	 * 产品线项目组内部编码
	 */
	private String projectCode;

	/**
	 * 产品线项目内部名称
	 */
	private String projectName;

	DtProductProjectEnum(String projectCode, String projectName) {
		this.projectCode = projectCode;
		this.projectName = projectName;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @return
	 */
	public String getDtProductProjectCode() {
		return projectCode;
	}

	/**
	 * 方法用途: <br>
	 * 实现步骤: <br>
	 * @return
	 */
	public String getDtProductProjectName() {
		return projectName;
	}

}
