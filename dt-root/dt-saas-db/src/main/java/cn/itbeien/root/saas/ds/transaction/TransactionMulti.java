package cn.itbeien.root.saas.ds.transaction;


import cn.itbeien.root.saas.ds.transaction.enums.Isolation;
import cn.itbeien.root.saas.ds.transaction.enums.Propagation;

import java.lang.annotation.*;

/**
 * 多数据源注解，用于替换Spring的@Transactional注解，后续项目中都使用该注解管理事务
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface TransactionMulti {

    /**数据源名称*/
    String datasource() default "defaultMaster";
    /**传播属性*/
    public Propagation propagation() default Propagation.REQUIRED;

    /**隔离级别*/
    public Isolation isolation() default Isolation.REPEATABLE_READ;

    /**是否为只读事务。*/
    public boolean readOnly() default false;

    /**遇到下列异常回滚事务。*/
    public Class<? extends Throwable>[] rollbackFor() default {Exception.class};

    /**遇到下列异常回滚事务。*/
    public String[] rollbackForClassName() default {};

}
