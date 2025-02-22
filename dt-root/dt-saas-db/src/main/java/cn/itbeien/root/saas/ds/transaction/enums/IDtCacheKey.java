package cn.itbeien.root.saas.ds.transaction.enums;


/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public interface IDtCacheKey {

    /**
     * 获取缓存的KEY的前缀编码
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    IDtProductProject getDtProductProject();

    ICacheModuleEnum getCacheModuleEnum();


    /**
     * 缓存对象名称, eg:MERCHANT
     * <p>
     * 保存在缓存中的KEY(getModuleCacheModule.getCacheModule+getCacheObjectName+(cacheObjecthadDynamicId()?id:""))
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    String getCacheObjectName();

    /**
     * 缓存对象是否有动态主键(缓存对象的ID)
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    boolean cacheObjecthadDynamicId();

    /**
     * 单位秒
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    Long getValidTime();
}
