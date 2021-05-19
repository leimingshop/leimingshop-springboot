/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.cms;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.remind.CmsRemindDTO;
import com.leimingtech.modules.dto.remind.CmsRemindMqDTO;
import com.leimingtech.modules.service.remind.CmsRemindService;
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
 * @Description 浏览记录队列
 * @Data: 2020/4/8 18:38
 * @Author: hecheng
 * @Version: 1.0
 */
@Slf4j
@Component
public class MqRemindConsumer {

    @Autowired
    private CmsRemindService cmsRemindService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_CMS_REMIND_MESSAGE_QUEUE)
    public void saveRemind(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);
        CmsRemindMqDTO cmsRemindMqDTO = JSONObject.parseObject(msgText, CmsRemindMqDTO.class);
        JSONArray jsonArray = JSONArray.parseArray(cmsRemindMqDTO.getMemberIdList());
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject json = jsonArray.getJSONObject(i);
            Long memberId = Long.valueOf(json.getString("id"));
            CmsRemindDTO cmsRemindDTO = new CmsRemindDTO();
            if (null != cmsRemindMqDTO.getCreatorId()) {
                cmsRemindDTO.setCreatorId(cmsRemindMqDTO.getCreatorId());
            }
            cmsRemindDTO.setMemberId(memberId);
            cmsRemindDTO.setRemindType(cmsRemindMqDTO.getRemindType());
            cmsRemindDTO.setRemindTypeId(cmsRemindMqDTO.getRemindTypeId());
            cmsRemindService.save(cmsRemindDTO);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("消息处理完毕，手动执行ACK!");
    }
}
