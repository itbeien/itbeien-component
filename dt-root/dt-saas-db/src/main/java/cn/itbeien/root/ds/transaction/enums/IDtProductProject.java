package cn.itbeien.root.ds.transaction.enums;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * 定义各个产品线的项目组编码的枚举接口类(公司.产品线.项目组.自定义)
 * Copyright© 2025 itbeien
 */
public interface IDtProductProject {

    /**
     * 获取产品线的项目内部编码
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    String getDtProductProjectCode();

    /**
     * 获取产品线项目的内部名称
     * 方法用途: <br>
     * 实现步骤: <br>
     *
     * @return
     */
    String getDtProductProjectName();
}
