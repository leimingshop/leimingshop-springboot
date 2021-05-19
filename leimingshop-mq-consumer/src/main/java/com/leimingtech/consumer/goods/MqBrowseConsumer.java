/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.browse.MqGoodsBrowseDTO;
import com.leimingtech.modules.service.browse.GoodsBrowseService;
import com.leimingtech.modules.service.goods.GoodsInfoService;
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
 * @Data: 2019/6/4 9:38
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Component
public class MqBrowseConsumer {

    @Autowired

    private GoodsBrowseService goodsBrowseService;
    @Autowired
    private GoodsInfoService goodsInfoService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_BROWSE_QUEUE)
    public void saveBroese(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);
        //解析消息
        MqGoodsBrowseDTO goodsBrowseDTO = JSONObject.parseObject(msgText, MqGoodsBrowseDTO.class);
        if (goodsBrowseDTO == null) {
            return;
        }
        goodsBrowseService.save(goodsBrowseDTO);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_UPDATE_EVALUATE_COUNT_QUEUE)
    public void updateEvaluateNum(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.debug("接收到日志消息，内容为：{}", msgText);
        //解析消息
        Long goodsId = JSONArray.parseObject(msgText, Long.class);
        if (goodsId == null) {
            return;
        }
        goodsInfoService.updateEvalusteNum(goodsId);

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.debug("消息处理完毕，手动执行ACK!");

    }


}
