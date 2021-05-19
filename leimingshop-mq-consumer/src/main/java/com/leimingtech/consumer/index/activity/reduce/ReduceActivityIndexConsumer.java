/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index.activity.reduce;

import com.leimingtech.modules.index.goods.service.activity.reduce.ReduceActivityIndexService;
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
 * <满减活动索引维护消息队列>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/26
 */
@Component
@Slf4j
public class ReduceActivityIndexConsumer {

    @Autowired
    private ReduceActivityIndexService reduceActivityIndexService;

    /**
     * 满减活动索引同步
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_REDUCE_ACTIVITY_SYNC_ES_QUEUE)
    public void reduceActivitySyncEsQueue(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到满减活动索引同步消息，内容为：{}", msgText);

        try {
            reduceActivityIndexService.syncReduceActivity();
            log.info("满减活动索引同步成功");
        } catch (Exception e) {
            log.info("满减活动索引同步失败:" + e);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("满减活动索引同步消息处理完毕，手动执行ACK!");

    }

}
