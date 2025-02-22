package cn.itbeien.root.saas.config;


import cn.itbeien.root.saas.ds.component.ReceiverRedisMessage;
import cn.itbeien.root.saas.ds.transaction.enums.DataSourceTopic;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import java.util.concurrent.CountDownLatch;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Configuration
public class RedisMessageListener {

    /**
     * Redis消息监听器容器
     * @param connectionFactory
     * @param dataSourceUpListenerAdapter 数据源上架、下架，删除消息订阅处理器
     * @return
     */
    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory, @Autowired @Qualifier("dataSourceUpListenerAdapter")MessageListenerAdapter dataSourceUpListenerAdapter,  @Autowired @Qualifier("dataSourceListenerDownAdapter")MessageListenerAdapter dataSourceListenerDownAdapter){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        //监听数据源上架主题并绑定消息订阅处理器
        container.addMessageListener(dataSourceUpListenerAdapter, new PatternTopic(DataSourceTopic.DATASOURCE_UP_TOPIC.getCacheObjectName()));
        container.addMessageListener(dataSourceListenerDownAdapter, new PatternTopic(DataSourceTopic.DATASOURCE_DOWN_TOPIC.getCacheObjectName()));
        return container;
    }




    /**
     * 数据源上架消息订阅处理器,并指定处理方法
     * @param receiverRedisMessage
     * @return
     */
    @Bean
    MessageListenerAdapter dataSourceUpListenerAdapter(ReceiverRedisMessage receiverRedisMessage) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiverRedisMessage, "dataSourceUpTopicReceive");
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = this.jacksonSerializer();
        messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
        return messageListenerAdapter;
    }

    /**
     * 数据源下架消息订阅处理器,并指定处理方法
     * @param receiverRedisMessage
     * @return
     */
    @Bean
    MessageListenerAdapter dataSourceListenerDownAdapter(ReceiverRedisMessage receiverRedisMessage) {
        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(receiverRedisMessage, "dataSourceDownTopicReceive");
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = this.jacksonSerializer();
        messageListenerAdapter.setSerializer(jackson2JsonRedisSerializer);
        return messageListenerAdapter;
    }

    @Bean
    ReceiverRedisMessage receiverRedisMessage(CountDownLatch latch) {
         return new ReceiverRedisMessage(latch);
    }

    @Bean
    CountDownLatch latch() {
        return new CountDownLatch(1);
    }

    private Jackson2JsonRedisSerializer jacksonSerializer(){
        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
        return jackson2JsonRedisSerializer;
    }
}
