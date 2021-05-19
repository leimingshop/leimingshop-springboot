/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.aftersale;

import com.leimingtech.modules.aftersale.service.AftersaleReturnService;
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
 * @Author: SWH ab4856812@163.com
 * @Description: 微信退款消费者
 * @Date: 2019年6月24日
 * @Version: V1.0
 */
@Slf4j
@Component
public class WxRefundConsumer {
    @Autowired

    private AftersaleReturnService aftersaleReturnService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_WEIXIN_REFUND_QUEUE)
    public void wxRefund(Channel channel, Message message) throws IOException {
        String h5WxRefundBaseStr = new String(message.getBody());
        if (StringUtils.isBlank(h5WxRefundBaseStr)) {
            return;
        }
        log.info("接收到退款实体数据，内容为：{}", h5WxRefundBaseStr);
        //调用退款服务
        aftersaleReturnService.aftersaleReturn(h5WxRefundBaseStr);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("售后申请保存日志消息消息处理完毕，手动执行ACK!");
    }

}


