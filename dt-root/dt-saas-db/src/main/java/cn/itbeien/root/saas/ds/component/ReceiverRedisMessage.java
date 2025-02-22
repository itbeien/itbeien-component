package cn.itbeien.root.saas.ds.component;

import cn.itbeien.root.saas.ds.vo.DataSourceTopicMssage;
import cn.itbeien.root.saas.service.SaasModuleServiceImpl;
import com.alibaba.fastjson.JSONObject;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Component
public class ReceiverRedisMessage {

    private static final Logger logger = LoggerFactory.getLogger(ReceiverRedisMessage.class);

    private CountDownLatch latch;

    @Resource
    private DynamicDataSourceManager dynamicDataSourceManager;

    @Autowired
    public ReceiverRedisMessage(CountDownLatch latch) {
        this.latch = latch;
    }

    /**
     * 数据源上架TOPIC消息接收方法
     * @param jsonMsg
     */
    public void dataSourceUpTopicReceive( String jsonMsg) {
        logger.info("begin consume redis mq data...");
        try{
            DataSourceTopicMssage dataSourceTopicMssage = JSONObject.parseObject(jsonMsg, DataSourceTopicMssage.class);
            logger.info("tenantCode {}, databaseId {}",dataSourceTopicMssage.getTenantCode(), dataSourceTopicMssage.getDatabaseId());
            //创建对应数据源
            for(Integer databaseId : dataSourceTopicMssage.getDatabaseId()) {
                dynamicDataSourceManager.createDataSource(dataSourceTopicMssage.getTenantCode(), databaseId);
            }
            logger.info("consume redis mq DATASOURCE_UP_TOPIC data success!");
        }catch(Exception e){
            logger.error("consume redis mq DATASOURCE_UP_TOPIC data fail，fail info:", e);
        }
        latch.countDown();
    }

    /**
     * 数据源下架TOPIC消息接收方法
     * @param jsonMsg
     */
    public void dataSourceDownTopicReceive( String jsonMsg) {
        logger.info("begin consume redis mq data...");
        try{
            DataSourceTopicMssage dataSourceTopicMssage = JSONObject.parseObject(jsonMsg, DataSourceTopicMssage.class);
            logger.info("tenantCode {}, databaseId {}",dataSourceTopicMssage.getTenantCode(),dataSourceTopicMssage.getDatabaseId());
            //删除对应数据源
            dynamicDataSourceManager.closeDataSource(dataSourceTopicMssage.getTenantCode(), dataSourceTopicMssage.getDatabaseId());
            SaasModuleServiceImpl.cleanMap();
            logger.info("consume redis mq DATASOURCE_DOWN_TOPIC data success!");
        }catch(Exception e){
            logger.error("consume redis mq DATASOURCE_DOWN_TOPIC data fail，fail info:", e);
        }
        latch.countDown();
    }

}
