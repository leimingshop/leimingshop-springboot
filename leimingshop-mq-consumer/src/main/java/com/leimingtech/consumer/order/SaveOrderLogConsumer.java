/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.order;

import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.dto.order.OrderLogDTO;
import com.leimingtech.modules.service.order.OrderLogService;
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
 * 功能描述：
 * <保存订单日志>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/20 9:13
 **/
@Slf4j
@Component
public class SaveOrderLogConsumer {

    @Autowired

    private OrderLogService orderLogService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SAVE_ORDERLOG_QUEUE)
    public void upload(Channel channel, Message message) throws IOException {

        try {
            String msgText = new String(message.getBody());
            log.info("接收到日志消息，内容为：{}", msgText);

            if (StringUtils.isBlank(msgText)) {
                return;
            }
            OrderLogDTO orderLogDTO = JSON.parseObject(msgText, OrderLogDTO.class);

            orderLogService.saveOrderLog(orderLogDTO);


            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            log.info("消息处理完毕，手动执行ACK!");
        } catch (Exception e) {
            log.error("订单日志保存异常： {}", e);

            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

            log.info("消息处理完毕，手动执行ACK!");
        }


    }

}
