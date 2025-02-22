package cn.itbeien.root.saas.ds.transaction.enums;


/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public enum CacheModuleEnum implements ICacheModuleEnum {

	SYSTEM_MODUEL("SYSTEM");


	private String moduleKey;

	CacheModuleEnum(String moduleKey) {
		this.moduleKey = moduleKey;
	}

	public String getModuleKey() {
		return this.moduleKey;
	}

}
