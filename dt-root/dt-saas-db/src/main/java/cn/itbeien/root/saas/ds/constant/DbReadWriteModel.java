package cn.itbeien.root.saas.ds.constant;

/**
 * 用于声明数据源为主库(读写)还是从库(只读)
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public enum DbReadWriteModel {

	READ, 
	
	READ_WRITE;
}
