package cn.itbeien.root.saas.ds.component;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Component
public class DriudDataSourceFactory extends AbstractDataSourceFactory {

   private static final Logger logger = LoggerFactory.getLogger(DriudDataSourceFactory.class);

    public DriudDataSourceFactory(){
        this.initMethod = "init";
        this.closeMethod = "close";
    }

    @Override
    public  DataSource createDataSource(Map<String, String> properties) {
        try {
            return DruidDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            logger.error("create driudDataSource error", e);
            return null;
        }
    }
}
