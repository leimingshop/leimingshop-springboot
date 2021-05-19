/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.sys;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.log.SysLogError;
import com.leimingtech.commons.tools.log.SysLogLogin;
import com.leimingtech.commons.tools.log.SysLogOperation;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.dao.SysLogErrorDao;
import com.leimingtech.dao.SysLogLoginDao;
import com.leimingtech.dao.SysLogOperationDao;
import com.leimingtech.entity.SysLogErrorEntity;
import com.leimingtech.entity.SysLogLoginEntity;
import com.leimingtech.entity.SysLogOperationEntity;
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
 * @Author: LX 17839193044@162.com
 * @Description: 日志队列接收者
 * @Date: 9:17 2019/5/9
 * @Version: V1.0
 */
@Slf4j
@Component
public class MqLogConsumer {

    @Autowired
    private SysLogErrorDao sysLogErrorDao;

    @Autowired
    private SysLogLoginDao sysLogLoginDao;

    @Autowired
    private SysLogOperationDao sysLogOperationDao;

    /**
     * @Author: LX 17839193044@162.com
     * @Description: 接收错误日志信息处理消息
     * @Date: 2019/5/9 9:20
     * @Version: V1.0
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_ADMIN_ERROR_LOG_QUEUE)
    public void receiverErrorLog(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);

        SysLogError sysLogError = JSON.parseObject(msgText, SysLogError.class);
        SysLogErrorEntity entity = ConvertUtils.sourceToTarget(sysLogError, SysLogErrorEntity.class);
        sysLogErrorDao.insert(entity);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }

    /**
     * @Author: kuangweiguo
     * @Description: 接收后台登录日志信息处理消息
     * @Date: 2019/6/27 22:20
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_ADMIN_LOGIN_LOG_QUEUE)
    public void receiverLoginLog(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);

        SysLogLogin sysLogLogin = JSON.parseObject(msgText, SysLogLogin.class);
        SysLogLoginEntity entity = ConvertUtils.sourceToTarget(sysLogLogin, SysLogLoginEntity.class);
        sysLogLoginDao.insert(entity);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }

    /**
     * @Author: kuangweiguo
     * @Description: 接收操作日志信息处理消息
     * @Date: 2019/6/27 22:20
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_ADMIN_OPERATION_LOG_QUEUE)
    public void receiverOperationLog(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);

        SysLogOperation sysLogOperation = JSON.parseObject(msgText, SysLogOperation.class);
        SysLogOperationEntity entity = ConvertUtils.sourceToTarget(sysLogOperation, SysLogOperationEntity.class);
        sysLogOperationDao.insert(entity);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }
}
