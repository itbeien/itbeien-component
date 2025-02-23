package cn.itbeien.root.ds.vo;

import java.util.List;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class DataSourceTopicMssage {
    private String tenantCode;
    private List<Integer> databaseId;


    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public List<Integer> getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(List<Integer> databaseId) {
        this.databaseId = databaseId;
    }

}
