package cn.itbeien.root.saas.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author itbeien
 * 项目网站：https://www.itbeien.cn
 * 公众号：贝恩聊架构
 * 全网同名，欢迎小伙伴们关注
 * 知识星球专享-多租户平台-SAAS DB组件
 * Copyright© 2025 itbeien
 */
@Component
public class NacosConfig {
    @Value(value = "${master.jdbc.driverClass}")
    private String driverClass;
    @Value(value = "${master.jdbc.user}")
    private String userName;

    @Value(value = "${master.jdbc.password}")
    private String password;
    @Value(value="${master.jdbc.driverUrl}")
    private String driverUrl;
    @NacosValue(value ="${master.jdbc.initialSize}")
    private String initialSize;
    @NacosValue(value ="${master.jdbc.maxActive}")
    private String maxActive;
    @NacosValue(value ="${master.jdbc.maxPoolPreparedStatementPerConnectionSize}")
    private String maxPoolPreparedStatementPerConnectionSize;
    @NacosValue(value ="${master.jdbc.maxWait}")
    private String maxWait;
    @NacosValue(value ="${master.jdbc.minEvictableIdleTimeMillis}")
    private String minEvictableIdleTimeMillis;
    @NacosValue(value ="${master.jdbc.minIdle}")
    private String minIdle;
    @NacosValue(value = "${master.jdbc.poolPreparedStatements}")
    private String poolPreparedStatements;
    @NacosValue(value = "${master.jdbc.testOnBorrow}")
    private String testOnBorrow;
    @NacosValue(value = "${master.jdbc.testWhileIdle}")
    private String testWhileIdle;
    @NacosValue(value = "${master.jdbc.timeBetweenEvictionRunsMillis}")
    private String timeBetweenEvictionRunsMillis;
    @NacosValue(value = "${master.jdbc.validationQuery}")
    private String  validationQuery;

    @Value(value = "${database.config.encrypt.key}")
    private String key;

    @NacosValue(value ="${slave.jdbc.driverClass}")
    private String slaveDriverClass;

    @NacosValue(value = "${slave.jdbc.user}")
    private String slaveUserName;

    @NacosValue(value = "${slave.jdbc.password}")
    private String slavePassword;
    @NacosValue(value="${slave.jdbc.driverUrl}")
    private String slaveDriverUrl;
    @NacosValue(value ="${slave.jdbc.initialSize}")
    private String slaveInitialSize;
    @NacosValue(value ="${slave.jdbc.maxActive}")
    private String slaveMaxActive;
    @NacosValue(value ="${slave.jdbc.maxPoolPreparedStatementPerConnectionSize}")
    private String slaveMaxPoolPreparedStatementPerConnectionSize;
    @NacosValue(value ="${slave.jdbc.maxWait}")
    private String slaveMaxWait;
    @NacosValue(value ="${slave.jdbc.minEvictableIdleTimeMillis}")
    private String slaveMinEvictableIdleTimeMillis;
    @NacosValue(value ="${slave.jdbc.minIdle}")
    private String slaveMinIdle;
    @NacosValue(value = "${slave.jdbc.poolPreparedStatements}")
    private String slavePoolPreparedStatements;
    @NacosValue(value = "${slave.jdbc.testOnBorrow}")
    private String slaveTestOnBorrow;
    @NacosValue(value = "${slave.jdbc.testWhileIdle}")
    private String slaveTestWhileIdle;
    @NacosValue(value = "${slave.jdbc.timeBetweenEvictionRunsMillis}")
    private String slaveTimeBetweenEvictionRunsMillis;
    @NacosValue(value = "${slave.jdbc.validationQuery}")
    private String  slaveValidationQuery;

    public String getDriverClass() {
        return driverClass;
    }

    public void setDriverClass(String driverClass) {
        this.driverClass = driverClass;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverUrl() {
        return driverUrl;
    }

    public void setDriverUrl(String driverUrl) {
        this.driverUrl = driverUrl;
    }

    public String getInitialSize() {
        return initialSize;
    }

    public void setInitialSize(String initialSize) {
        this.initialSize = initialSize;
    }

    public String getMaxActive() {
        return maxActive;
    }

    public void setMaxActive(String maxActive) {
        this.maxActive = maxActive;
    }

    public String getMaxPoolPreparedStatementPerConnectionSize() {
        return maxPoolPreparedStatementPerConnectionSize;
    }

    public void setMaxPoolPreparedStatementPerConnectionSize(String maxPoolPreparedStatementPerConnectionSize) {
        this.maxPoolPreparedStatementPerConnectionSize = maxPoolPreparedStatementPerConnectionSize;
    }

    public String getMaxWait() {
        return maxWait;
    }

    public void setMaxWait(String maxWait) {
        this.maxWait = maxWait;
    }

    public String getMinEvictableIdleTimeMillis() {
        return minEvictableIdleTimeMillis;
    }

    public void setMinEvictableIdleTimeMillis(String minEvictableIdleTimeMillis) {
        this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
    }

    public String getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(String minIdle) {
        this.minIdle = minIdle;
    }

    public String getPoolPreparedStatements() {
        return poolPreparedStatements;
    }

    public void setPoolPreparedStatements(String poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public String getTestOnBorrow() {
        return testOnBorrow;
    }

    public void setTestOnBorrow(String testOnBorrow) {
        this.testOnBorrow = testOnBorrow;
    }

    public String getTestWhileIdle() {
        return testWhileIdle;
    }

    public void setTestWhileIdle(String testWhileIdle) {
        this.testWhileIdle = testWhileIdle;
    }

    public String getTimeBetweenEvictionRunsMillis() {
        return timeBetweenEvictionRunsMillis;
    }

    public void setTimeBetweenEvictionRunsMillis(String timeBetweenEvictionRunsMillis) {
        this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
    }

    public String getValidationQuery() {
        return validationQuery;
    }

    public void setValidationQuery(String validationQuery) {
        this.validationQuery = validationQuery;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSlaveDriverClass() {
        return slaveDriverClass;
    }

    public void setSlaveDriverClass(String slaveDriverClass) {
        this.slaveDriverClass = slaveDriverClass;
    }

    public String getSlaveUserName() {
        return slaveUserName;
    }

    public void setSlaveUserName(String slaveUserName) {
        this.slaveUserName = slaveUserName;
    }

    public String getSlavePassword() {
        return slavePassword;
    }

    public void setSlavePassword(String slavePassword) {
        this.slavePassword = slavePassword;
    }

    public String getSlaveDriverUrl() {
        return slaveDriverUrl;
    }

    public void setSlaveDriverUrl(String slaveDriverUrl) {
        this.slaveDriverUrl = slaveDriverUrl;
    }

    public String getSlaveInitialSize() {
        return slaveInitialSize;
    }

    public void setSlaveInitialSize(String slaveInitialSize) {
        this.slaveInitialSize = slaveInitialSize;
    }

    public String getSlaveMaxActive() {
        return slaveMaxActive;
    }

    public void setSlaveMaxActive(String slaveMaxActive) {
        this.slaveMaxActive = slaveMaxActive;
    }

    public String getSlaveMaxPoolPreparedStatementPerConnectionSize() {
        return slaveMaxPoolPreparedStatementPerConnectionSize;
    }

    public void setSlaveMaxPoolPreparedStatementPerConnectionSize(String slaveMaxPoolPreparedStatementPerConnectionSize) {
        this.slaveMaxPoolPreparedStatementPerConnectionSize = slaveMaxPoolPreparedStatementPerConnectionSize;
    }

    public String getSlaveMaxWait() {
        return slaveMaxWait;
    }

    public void setSlaveMaxWait(String slaveMaxWait) {
        this.slaveMaxWait = slaveMaxWait;
    }

    public String getSlaveMinEvictableIdleTimeMillis() {
        return slaveMinEvictableIdleTimeMillis;
    }

    public void setSlaveMinEvictableIdleTimeMillis(String slaveMinEvictableIdleTimeMillis) {
        this.slaveMinEvictableIdleTimeMillis = slaveMinEvictableIdleTimeMillis;
    }

    public String getSlaveMinIdle() {
        return slaveMinIdle;
    }

    public void setSlaveMinIdle(String slaveMinIdle) {
        this.slaveMinIdle = slaveMinIdle;
    }

    public String getSlavePoolPreparedStatements() {
        return slavePoolPreparedStatements;
    }

    public void setSlavePoolPreparedStatements(String slavePoolPreparedStatements) {
        this.slavePoolPreparedStatements = slavePoolPreparedStatements;
    }

    public String getSlaveTestOnBorrow() {
        return slaveTestOnBorrow;
    }

    public void setSlaveTestOnBorrow(String slaveTestOnBorrow) {
        this.slaveTestOnBorrow = slaveTestOnBorrow;
    }

    public String getSlaveTestWhileIdle() {
        return slaveTestWhileIdle;
    }

    public void setSlaveTestWhileIdle(String slaveTestWhileIdle) {
        this.slaveTestWhileIdle = slaveTestWhileIdle;
    }

    public String getSlaveTimeBetweenEvictionRunsMillis() {
        return slaveTimeBetweenEvictionRunsMillis;
    }

    public void setSlaveTimeBetweenEvictionRunsMillis(String slaveTimeBetweenEvictionRunsMillis) {
        this.slaveTimeBetweenEvictionRunsMillis = slaveTimeBetweenEvictionRunsMillis;
    }

    public String getSlaveValidationQuery() {
        return slaveValidationQuery;
    }

    public void setSlaveValidationQuery(String slaveValidationQuery) {
        this.slaveValidationQuery = slaveValidationQuery;
    }
}
