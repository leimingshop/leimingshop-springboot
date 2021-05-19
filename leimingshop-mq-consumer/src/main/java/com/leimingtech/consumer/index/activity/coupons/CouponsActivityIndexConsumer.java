/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index.activity.coupons;

import com.leimingtech.modules.index.goods.service.activity.coupons.CouponActivityIndexService;
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
 * @author liuyuanjie
 * @version 1.0
 * @date 2019/12/10
 */
@Component
@Slf4j
public class CouponsActivityIndexConsumer {

    @Autowired
    private CouponActivityIndexService couponActivityIndexService;

    /**
     * 优惠券活动索引同步
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_COUPONS_ACTIVITY_SYNC_ES_QUEUE)
    public void couponsActivitySyncEsQueue(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到优惠券活动索引同步消息，内容为：{}", msgText);

        try {
            couponActivityIndexService.syncCouponsActivity();
            log.info("优惠券活动索引同步成功");
        } catch (Exception e) {
            log.info("优惠券活动索引同步失败:" + e);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("优惠券活动索引同步消息处理完毕，手动执行ACK!");

    }

}
