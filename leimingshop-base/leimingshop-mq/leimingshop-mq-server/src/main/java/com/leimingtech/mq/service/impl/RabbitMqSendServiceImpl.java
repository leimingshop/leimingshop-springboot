/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.mq.service.impl;

import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.rabbitmq.client.AMQP;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: LX 17839193044@162.com
 * @Description: RabbitMQ发送者实现类
 * @Date: 11:49 2019/5/8
 * @Version: V1.0
 */
@Service
@Slf4j
public class RabbitMqSendServiceImpl implements RabbitMqSendService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ConnectionFactory connectionFactory;


    /**
     * @param routingKey: 路由键
     * @param message:    消息内容（Json格式）
     * @Author: LX 17839193044@162.com
     * @Description: RabbitMQ发送消息(使用默认的交换机)
     * @Date: 2019/5/8 11:42
     * @Version: V1.0
     */
    @Override
    @PostMapping("send/{routingKey}")
    public Boolean sendMsg(@PathVariable("routingKey") String routingKey, @RequestParam("message") String message) {
        rabbitTemplate.convertAndSend(routingKey, message);
        return true;
    }


    /**
     * @param exchange:   交换机
     * @param routingKey: 路由键
     * @param message:    消息内容
     * @Author: LX 17839193044@162.com
     * @Description: RabbitMQ发送消息(指定交换机)
     * @Date: 2019/5/8 11:45
     * @Version: V1.0
     */
    @Override
    @PostMapping("send/{exchange}/{routingKey}")
    public void sendMsg(@PathVariable("exchange") String exchange, @PathVariable("routingKey") String routingKey, @RequestParam("message") String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }

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
    @Override
    @PostMapping("send/delay/{exchange}/{routingKey}")
    public void sendMsg(@PathVariable("exchange") String exchange, @PathVariable("routingKey") String routingKey, @RequestParam("message") String message, @RequestParam("delay") Long delay) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, message1 -> {
            message1.getMessageProperties().setHeader("x-delay", delay);
            return message1;
        });
    }

    /**
     * 创建交换机
     *
     * @param exchangeName 交换机名称
     * @param durable      是否长期有效
     */
    @Override
    @PostMapping("exchange")
    public void addExchange(@RequestParam("exchangeName") String exchangeName,
                            @RequestParam("durable") Boolean durable) {
        RabbitAdmin rabbitAdmin = getRabbitAdmin();
        TopicExchange topicExchange = new TopicExchange(exchangeName, durable, MqConstant.AUTO_DELETE);
        rabbitAdmin.declareExchange(topicExchange);
    }

    /**
     * 创建队列
     *
     * @param queueName 队列名称
     * @param durable   是否持久化
     * @param exclusive 声明一个独立队列
     */
    @Override
    @PostMapping("queue")
    public void addQueue(@RequestParam("queueName") String queueName,
                         @RequestParam("durable") Boolean durable,
                         @RequestParam("exclusive") Boolean exclusive) {
        RabbitAdmin rabbitAdmin = getRabbitAdmin();
        Queue queue = new Queue(queueName, durable, exclusive, MqConstant.AUTO_DELETE);
        rabbitAdmin.declareQueue(queue);
    }

    /**
     * 消息队列、交换机、路由绑定
     *
     * @param exchangeName 交换机名称
     * @param routingKey:  路由键
     * @param queueName    队列名称
     * @param durable      设置是否持久化
     */
    @Override
    @PostMapping("bind")
    public void declareTopicRoutingQueue(@RequestParam("exchangeName") String exchangeName,
                                         @RequestParam("routingKey") String routingKey,
                                         @RequestParam("queueName") String queueName,
                                         @RequestParam("durable") Boolean durable) {
        //对交换机名称校验 为null则设置为默认
        if (StringUtils.isBlank(exchangeName)) {
            exchangeName = MqConstant.DEFAULT_EXCHANGE_NAME;
        }

        //对路由键校验 为null则设置为默认
        if (StringUtils.isBlank(routingKey)) {
            routingKey = MqConstant.DEFAULT_ROUTING_KEY;
        }

        //对队列名称校验 为null则设置为默认
        if (StringUtils.isBlank(queueName)) {
            routingKey = MqConstant.DEFAULT_QUEUE;
        }

        //对持久化校验 为null则设置为默认
        if (durable == null) {
            durable = MqConstant.DURABLE;
        }

        declareTopicQueue(exchangeName, routingKey, queueName, durable,
                MqConstant.EXCLUSIVE, MqConstant.AUTO_DELETE);
    }

    /**
     * 根据队列名称查询队列的数量
     *
     * @param queueName 队列名称
     * @return
     */
    @Override
    @GetMapping("count/{queueName}")
    public Integer findMessageCount(@PathVariable("queueName") String queueName) {

        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        return rabbitAdmin.getRabbitTemplate().execute(channel -> {
            final AMQP.Queue.DeclareOk ok = channel.queueDeclare(queueName, true, false, false, null);
            return ok.getMessageCount();
        });
    }

    /**
     * 删除消息队列
     *
     * @param queueName 队列名称
     */
    @Override
    @DeleteMapping("queue/{queueName}")
    public void deleteQuery(@PathVariable("queueName") String queueName) {
        RabbitAdmin rabbitAdmin = getRabbitAdmin();
        rabbitAdmin.deleteQueue(queueName);
    }

    /**
     * 删除交换机
     *
     * @param exchangeName 交换机名称
     */
    @Override
    @DeleteMapping("exchange/{exchangeName}")
    public void deleteExchangeName(@PathVariable("exchangeName") String exchangeName) {
        RabbitAdmin rabbitAdmin = getRabbitAdmin();
        rabbitAdmin.deleteExchange(exchangeName);
    }


    /**
     * 初始化rabbitAdmin
     *
     * @return
     */
    private RabbitAdmin getRabbitAdmin() {
        return new RabbitAdmin(connectionFactory);
    }

    /**
     * 消息队列、交换机、路由绑定
     *
     * @param exchangeName 交换机名称
     * @param routingKey:  路由键
     * @param queueName    队列名称
     * @param durable      设置是否持久化
     * @param exclusive    设置是否排他，如果为true，该队列进队首次声明它的连接可见，并在连接断开时自动删除（建议设置为false）
     * @param autoDelete   设置是否自动删除，true自动删除，（建议false）
     *                     前提：至少有一个消费者连接到这个队列，之后所有与这个队列连接的消费者都断开时才会自动删除
     */
    private void declareTopicQueue(String exchangeName, String routingKey,
                                   String queueName, Boolean durable,
                                   Boolean exclusive, Boolean autoDelete) {
        RabbitAdmin rabbitAdmin = getRabbitAdmin();
        TopicExchange topicExchange = new TopicExchange(exchangeName, durable, autoDelete);
        rabbitAdmin.declareExchange(topicExchange);
        Queue queue = new Queue(queueName, durable, exclusive, autoDelete);
        rabbitAdmin.declareQueue(queue);
        rabbitAdmin.declareBinding(BindingBuilder.bind(queue).to(topicExchange).with(routingKey));
    }
}
