/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.statis;


import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.dto.traffic.PvDetailDTO;
import com.leimingtech.modules.service.traffic.PvDetailStatisService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 保存浏览详情消费者
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Component
@Slf4j
public class PvDetailAddConsumer {

    @Autowired
    private PvDetailStatisService pvDetailStatisService;

    /**
     * 保存浏览详情
     *
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SAVE_PV_QUEUE)
    public void pvDetailAdd(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到保存浏览量的消息，内容为：{}", msgText);
        PvDetailDTO pvDetailDTO = JSON.parseObject(msgText, PvDetailDTO.class);
        pvDetailStatisService.savePageview(pvDetailDTO);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("保存浏览量的消息处理完毕，手动执行ACK!");

    }
}
