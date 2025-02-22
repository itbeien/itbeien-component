package cn.itbeien.root.saas.ds.component;

import cn.itbeien.root.saas.ds.constant.DbReadWriteModel;

import java.lang.annotation.*;

/**
 * 数据源注解，用于方法或者类上，声明该数据源的读写模式
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSource {
    DbReadWriteModel value();
}
