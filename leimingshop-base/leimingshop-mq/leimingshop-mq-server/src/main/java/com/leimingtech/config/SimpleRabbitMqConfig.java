/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;


/**
 * @Author: LX 17839193044@162.com
 * @Description: RabbitMQ配置参数
 * @Date: 2019/5/8 10:28
 * @Version: V1.0
 */
@Data
@Slf4j
@Component("simpleRabbitMqConfig")
public class SimpleRabbitMqConfig {

    /**
     * 地址
     */
    @Value("${spring.rabbitmq.addresses}")
    private String addresses;

    /**
     * 用户名
     */
    @Value("${spring.rabbitmq.username}")
    private String userName;

    /**
     * 密码
     */
    @Value("${spring.rabbitmq.password}")
    private String password;

    /**
     * 虚拟主机
     */
    @Value("${spring.rabbitmq.virtual-host}")
    private String virtualHost;

    /**
     * 开启发送确认
     */
    @Value("${spring.rabbitmq.publisher-confirms}")
    private Boolean publisherConfirms;

    /**
     * 开启发送失败退回
     */
    @Value("${spring.rabbitmq.publisher-returns}")
    private Boolean publisherReturns;

    /**
     * 开启ACK
     */
    @Value("${spring.rabbitmq.listener.direct.acknowledge-mode}")
    private String directAcknowledgeMode;

    /**
     * 开启ACK
     */
    @Value("${spring.rabbitmq.listener.simple.acknowledge-mode}")
    private String simpleAcknowledgeMode;

    /**
     * 连接超时时间
     */
    @Value("${spring.rabbitmq.connection-timeout}")
    private Integer connectionTimeout;

    /**
     * 通道超时时间
     */
    @Value("${spring.rabbitmq.cache.channel.checkout-timeout}")
    private Integer checkoutTimeout;

    /**
     * 设置通道数目的限制
     */
    @Value("${spring.rabbitmq.cache.channel.size}")
    private Integer channelSize;

    /**
     * 设置缓存connection数量
     */
//    @Value("${spring.rabbitmq.cache.connection.size}")
//    private Integer connectionCacheSize;

    /**
     * 与return机制结合配置次属性
     */
    private Boolean templateMandatory;

    /**
     * Spring启动容器时执行
     */
    @PostConstruct
    private void initialize() {
        log.info("[rabbitmq] addresses: {}, userName: {}, password: {}, virtualHost: {}, publisherConfirms: {}, publisherReturns, " +
                        "templateMandatory: {}, directAcknowledgeMode: {}, simpleAcknowledgeMode: {}, connectionTimeout: {}, " +
                        "checkoutTimeout: {}, channelSize: {}",
                addresses, userName, password, virtualHost, publisherConfirms, publisherReturns,
                templateMandatory, directAcknowledgeMode, simpleAcknowledgeMode, connectionTimeout,
                checkoutTimeout);
    }

    @Override
    public String toString() {
        return String.format("[rabbitmq] addresses: %s, userName: %s, password: %s, virtualHost: %s, publisherConfirms: %s, publisherReturns: %s, " +
                        "templateMandatory: %s, directAcknowledgeMode: %s, simpleAcknowledgeMode: %s, connectionTimeout: %s, " +
                        "checkoutTimeout: %s, channelSize: %s",
                addresses, userName, password, virtualHost, publisherConfirms, publisherReturns,
                templateMandatory, directAcknowledgeMode, simpleAcknowledgeMode, connectionTimeout,
                checkoutTimeout, channelSize);
    }

}
