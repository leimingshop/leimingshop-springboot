/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.aftersale;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.aftersale.dto.AftersaleLogSaveDTO;
import com.leimingtech.modules.aftersale.service.AftersaleLogService;
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
 * @Author: SWH ab4856812@163.com
 * @Description:售后申请保存日志消息
 * @Date: 2019/6/19 15:58
 * @Version: V1.0
 */
@Slf4j
@Component
public class ApplySaveConsumer {

    @Autowired

    private AftersaleLogService aftersaleLogService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_APPLY_SAVE_QUEUE)
    public void resetPasswd(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到售后申请保存日志消息，内容为：{}", msgText);

        //解析消息
        JSONObject jsonObject = JSON.parseObject(msgText);
        if (jsonObject == null) {
            return;
        }
        AftersaleLogSaveDTO aftersaleLogSaveDTO = JSON.toJavaObject(jsonObject, AftersaleLogSaveDTO.class);
        aftersaleLogService.save(aftersaleLogSaveDTO);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("售后申请保存日志消息消息处理完毕，手动执行ACK!");
    }
}


