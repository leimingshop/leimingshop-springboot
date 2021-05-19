/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.goods;

import com.alibaba.fastjson.JSONArray;
import com.leimingtech.modules.dto.goods.record.GoodsRecordDTO;
import com.leimingtech.modules.service.goods.record.GoodsRecordService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName MqGoodsShowConsumer
 * @Description 商品上下架规格记录mq
 * @Author DY
 * @Date 2019/6/26 13:44
 * @Version 1.0
 **/
@Slf4j
@Component
public class MqGoodsShowConsumer {

    @Autowired
    private GoodsRecordService goodsRecordService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SHOW_QUEUE)
    public void saveGoodsShow(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);
        //解析消息
        List<GoodsRecordDTO> goodsRecordDTOList = JSONArray.parseArray(msgText, GoodsRecordDTO.class);
        if (CollectionUtils.isNotEmpty(goodsRecordDTOList)) {
            for (GoodsRecordDTO recordDTO : goodsRecordDTOList) {
                goodsRecordService.save(recordDTO);
            }
        } else {
            return;
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");
    }
}
