/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.sys;

import com.alibaba.fastjson.JSON;
import com.leimingtech.dao.uploadrecord.UploadRecordDao;
import com.leimingtech.entity.upload.UploadRecordEntity;
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
 * @Description 文件上传记录队列
 * @Data: 2019/5/24 11:01
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Component
public class MqUploadConsumer {

    @Autowired
    private UploadRecordDao uploadRecordDao;

    /**
     * @param message 消息体
     * @Author: chengQian
     * @Date: 2019/5/24 11:01
     * @Version: V1.0
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_UPLOAD_RECORD_QUEUE)
    public void upload(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);


        if (StringUtils.isBlank(msgText)) {
            return;
        }
        msgText = msgText.replaceAll("%26", "&");
        UploadRecordEntity uploadRecordEntity = JSON.parseObject(msgText, UploadRecordEntity.class);
        uploadRecordDao.insert(uploadRecordEntity);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }
}
