package cn.itbeien.root.ds.vo;

/**
 * 用于加载数据库中的数据
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
public class TenantConfigVO {

    private String tenantCode;

    private Integer databaseId;

    private String dbModel;

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public Integer getDatabaseId() {
        return databaseId;
    }

    public void setDatabaseId(Integer databaseId) {
        this.databaseId = databaseId;
    }

    public String getDbModel() {
        return dbModel;
    }

    public void setDbModel(String dbModel) {
        this.dbModel = dbModel;
    }

    @Override
    public String toString() {
        return "TenantConfigVO{" +
                "tenantCode='" + tenantCode + '\'' +
                ", databaseId=" + databaseId +
                ", dbModel='" + dbModel + '\'' +
                '}';
    }
}
