/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.mq.service;

import org.springframework.web.bind.annotation.*;

/**
 * @Author: LX 17839193044@162.com
 * @Description: RabbitMQ发送者service
 * @Date: 11:41 2019/5/8
 * @Version: V1.0
 */

public interface RabbitMqSendService {

    /**
     * RabbitMQ发送消息(使用默认的交换机)
     *
     * @param routingKey: 路由键
     * @param message:    消息内容（Json格式）
     * @return Boolean 发送结果
     * @author: LX 17839193044@162.com
     * @date: 2019/5/8 11:42
     * @version: V1.0
     */
    @PostMapping("send/{routingKey}")
    Boolean sendMsg(@PathVariable("routingKey") String routingKey, @RequestParam("message") String message);

    /**
     * RabbitMQ发送消息(指定交换机)
     *
     * @param exchange:   交换机
     * @param routingKey: 路由键
     * @param message:    消息内容
     * @author: LX 17839193044@162.com
     * @date: 2019/5/8 11:45
     * @version: V1.0
     */
    @PostMapping("send/{exchange}/{routingKey}")
    void sendMsg(@PathVariable("exchange") String exchange, @PathVariable("routingKey") String routingKey, @RequestParam("message") String message);

    /**
     * 功能描述:
     * 〈RabbitMQ发送延迟消息〉
     *
     * @param exchange:   交换机
     * @param routingKey: 路由键
     * @param message     消息内容
     * @param delay       延迟时间
     * @author : 刘远杰
     */
    @PostMapping("send/delay/{exchange}/{routingKey}")
    void sendMsg(@PathVariable("exchange") String exchange, @PathVariable("routingKey") String routingKey, @RequestParam("message") String message, @RequestParam("delay") Long delay);

    /**
     * 创建交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否长期有效
     */
    @PostMapping("exchange")
    void addExchange(@RequestParam("exchangeName") String exchangeName,
                     @RequestParam("durable") Boolean durable);

    /**
     * 创建队列
     *
     * @param queueName 队列名称
     * @param durable   声明一个持久队列
     * @param exclusive 声明一个独立队列
     */
    @PostMapping("queue")
    void addQueue(@RequestParam("queueName") String queueName,
                  @RequestParam("durable") Boolean durable,
                  @RequestParam("exclusive") Boolean exclusive);

    /**
     * 消息队列、交换机、路由绑定
     *
     * @param exchangeName 交换机名称
     * @param routingKey:  路由键
     * @param queueName    队列名称
     * @param durable      声明一个持久队列/是否长期有效
     */
    @PostMapping("bind")
    void declareTopicRoutingQueue(@RequestParam("exchangeName") String exchangeName,
                                  @RequestParam("routingKey") String routingKey,
                                  @RequestParam("queueName") String queueName,
                                  @RequestParam("durable") Boolean durable);

    /**
     * 根据队列名称查询队列的数量
     *
     * @param queueName 队列名称
     * @return
     */
    @GetMapping("count/{queueName}")
    Integer findMessageCount(@PathVariable("queueName") String queueName);

    /**
     * 删除消息队列
     *
     * @param queueName 队列名称
     */
    @DeleteMapping("queue/{queueName}")
    void deleteQuery(@PathVariable("queueName") String queueName);

    /**
     * 删除交换机
     *
     * @param exchangeName 交换机名称
     */
    @DeleteMapping("exchange/{exchangeName}")
    void deleteExchangeName(@PathVariable("exchangeName") String exchangeName);
}
