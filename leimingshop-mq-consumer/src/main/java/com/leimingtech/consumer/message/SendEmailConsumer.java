/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.message;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.message.service.SysMailTemplateService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 发送邮件消费者
 *
 * @author xuzhch
 * @date 2020/6/9 15:52
 **/
@Slf4j
@Component
public class SendEmailConsumer {

    @Autowired
    private SysMailTemplateService sysMailTemplateService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SEND_EMAIL_QUEUE)
    public void sendMessage(Channel channel, Message message) throws Exception {

        String msgText = new String(message.getBody());
        log.info("接收到发送邮件的消息，内容为：{}", msgText);

        Map map = JSONObject.parseObject(msgText, Map.class);
        String template = null == map.get("template") ? "" : String.valueOf(map.get("template"));
        String mailTo = null == map.get("mailTo") ? "" : String.valueOf(map.get("mailTo"));
        String mailCc = null == map.get("mailCc") ? "" : String.valueOf(map.get("mailCc"));
        String params = null == map.get("params") ? "" : String.valueOf(map.get("params"));
        if (StringUtils.isBlank(template) || StringUtils.isBlank(mailTo)) {
            log.error("邮件发送失败，缺少邮件模板或者收件人");
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }
        sysMailTemplateService.sendMail(Long.valueOf(template), mailTo, mailCc, params);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("按照短信模板发送消息处理完毕，手动执行ACK!");
    }
}
