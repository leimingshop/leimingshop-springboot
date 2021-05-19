/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.store;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.log.StoreLogLogin;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.dao.loginlog.StoreLoginLogDao;
import com.leimingtech.modules.dto.store.UpdateStoreStatusDTO;
import com.leimingtech.modules.entity.loginlog.StoreLoginLogEntity;
import com.leimingtech.modules.service.auditlog.AuditLogService;
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
 * @Author: weixianchun
 * @Description: seller日志队列接收者
 * @Date :2019/6/28 13:50
 * @Version V1.0
 **/
@Slf4j
@Component
public class MqStoreLogConsumer {

    @Autowired
    private StoreLoginLogDao storeLoginLogDao;

    @Autowired

    private AuditLogService auditLogService;

    /**
     * @Author: weixianchun
     * @Description: 登录日志
     * @Date :2019/6/28 14:27
     * @Param channel
     * @Param message
     * @Version V1.0
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SELLER_LOGIN_LOG_QUEUE)
    public void receiverLoginLog(Channel channel, Message message) throws IOException {

        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);
        StoreLogLogin logLogin = JSON.parseObject(msgText, StoreLogLogin.class);
        StoreLoginLogEntity storeLoginLogEntity = ConvertUtils.sourceToTarget(logLogin, StoreLoginLogEntity.class);
        storeLoginLogDao.insert(storeLoginLogEntity);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SAVE_SOTRE_AUDIT_LOG_QUEUE)
    public void saveAuditLog(Channel channel, Message message) throws IOException {

        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);

        UpdateStoreStatusDTO updateStoreStatusDTO = JSON.parseObject(msgText, UpdateStoreStatusDTO.class);
        auditLogService.saveLog(updateStoreStatusDTO);


        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }
}
