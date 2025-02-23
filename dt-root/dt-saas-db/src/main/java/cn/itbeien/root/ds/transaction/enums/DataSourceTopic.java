package cn.itbeien.root.ds.transaction.enums;


/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public enum DataSourceTopic implements IDtCacheKey {

    DATASOURCE_UP_TOPIC(CacheModuleEnum.SYSTEM_MODUEL, "DATASOURCE_UP_TOPIC", true,2592000L),
    DATASOURCE_DOWN_TOPIC(CacheModuleEnum.SYSTEM_MODUEL, "DATASOURCE_DOWN_TOPIC",true,2592000L),
    DATASOURCE_DELETE_TOPIC(CacheModuleEnum.SYSTEM_MODUEL, "DATASOURCE_DELETE_TOPIC",true,2592000L),
    ;


    private String cacheObjectName;


    private CacheModuleEnum moduleKey;


    private boolean cacheObjecthadDynamicId;

    private Long validTime;

    DataSourceTopic(CacheModuleEnum moduleKey, String cacheObjectName, boolean cacheObjecthadDynamicId, Long validTime) {
        this.moduleKey = moduleKey;
        this.cacheObjectName = cacheObjectName;
        this.cacheObjecthadDynamicId = cacheObjecthadDynamicId;
        this.validTime = validTime;
    }



    /**
     * 方法用途: <br>
     * 实现步骤: <br>
     * @return
     */
    @Override
    public String getCacheObjectName() {
        return cacheObjectName;
    }

    /**
     * 方法用途: <br>
     * 实现步骤: <br>
     * @return
     */
    @Override
    public boolean cacheObjecthadDynamicId() {
        return cacheObjecthadDynamicId;
    }

    /**
     * 方法用途: <br>
     * 实现步骤: <br>
     * @return
     */
    @Override
    public Long getValidTime() {
        return validTime;
    }

    @Override
    public IDtProductProject getDtProductProject() {
        return DtProductProjectEnum.DT_BASE_COMPONENT;
    }

    @Override
    public ICacheModuleEnum getCacheModuleEnum() {
        return this.moduleKey;
    }
}
