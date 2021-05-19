/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.payment;

import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import com.leimingtech.modules.service.payment.OrderPayService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * <异步保存订单支付表>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/14
 */
@Slf4j
@Component
public class SaveOrderPayConsumer {

    @Autowired
    private OrderPayService orderPayService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SAVE_ORDER_PAY_QUEUE)
    public void confirmOrder(Channel channel, Message message) throws IOException {

        String key = null;

        try {

            String msgText = new String(message.getBody());
            log.info("接收到日志消息，内容为：{}", msgText);

            if (StringUtils.isBlank(msgText)) {
                return;
            }
            OrderPayDTO orderPayDTO = JSON.parseObject(msgText, OrderPayDTO.class);

            orderPayService.saveOrderPay(orderPayDTO);

            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            log.info("消息处理完毕，手动执行ACK!");
        } catch (Exception e) {
            log.error("保存订单支付表异常： {}", e);
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            log.info("消息处理完毕，手动执行ACK!");
        }
    }

}
