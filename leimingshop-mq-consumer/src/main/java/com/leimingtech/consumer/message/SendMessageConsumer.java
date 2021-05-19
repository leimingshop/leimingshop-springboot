/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.message;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.strategy.MessageContext;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 按照短信模板发送消息消费者
 *
 * @author lixiang
 * @date 2020/4/9 15:52
 **/
@Slf4j
@Component
public class SendMessageConsumer {

    @Autowired
    private MessageContext messageContext;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE)
    public void sendMessage(Channel channel, Message message) throws Exception {
        log.info("接受短信模板发送消息");

        String msgText = new String(message.getBody());
        log.info("接收到按照短信模板发送消息的消息，内容为：{}", msgText);

        MessageDTO baseMessageDTO = JSONObject.parseObject(msgText, MessageDTO.class);

        // 使用不同的策略进行执行
        messageContext.execute(baseMessageDTO);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("按照短信模板发送消息处理完毕，手动执行ACK!");
    }
}
