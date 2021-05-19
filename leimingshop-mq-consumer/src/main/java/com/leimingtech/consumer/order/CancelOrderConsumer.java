/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.order;

import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName CancelOrderConsumer
 * @Description 取消订单
 * @Author DY
 * @Date 2019/8/10 16:21
 * @Version 1.0
 **/
@Slf4j
@Component
public class CancelOrderConsumer {


    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderPayService orderPayService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_CANCEL_ORDERLOG_QUEUE)
    public void cancelOrder(Channel channel, Message message) throws IOException {
        try {
            String orderSn = new String(message.getBody());
            log.info("接收到取消订单日志消息,订单号{}", orderSn);
            List<Long> orderSnList = JSON.parseArray(orderSn, long.class);
            orderService.orderCanceledTiming(orderSnList);
            orderPayService.cancelOrderPayByOrderSnList(orderSnList);
            log.info("异步处理订单取消成功，orderSnList:{}", orderSnList);
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("订单取消异常： {}", e);

            // ACK手动确认处理消息
            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);

            log.info("消息处理完毕，手动执行ACK!");
        }
    }
}
