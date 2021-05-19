/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.goods;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.modules.dto.price.UpdateBatchPriceLog;
import com.leimingtech.modules.dto.stock.UpdateBatchStockLog;
import com.leimingtech.modules.service.price.PriceLogService;
import com.leimingtech.modules.service.stock.StockLogService;
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
 * @ClassName : MqRecordLogConsumer
 * @Description: 价格和库存修改记录保存队列
 * @Author : xuzhch
 * @Date ： 2019/6/15
 * @Version ：v1.0
 */

@Slf4j
@Component
public class MqRecordLogConsumer {

    @Autowired
    private StockLogService stockLogService;

    @Autowired
    private PriceLogService priceLogService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_PRICE_RECORD_QUEUE)
    public void savePriceLog(Channel channel, Message message) throws IOException {
        String data = new String(message.getBody());
        UpdateBatchPriceLog updateBatchPriceLog = JSONArray.parseObject(data, UpdateBatchPriceLog.class);
        priceLogService.insertBatch(updateBatchPriceLog);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().
                getDeliveryTag(), false);
        log.info("规格价格消息处理完毕，手动执行ACK!");
    }


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STOCK_RECORD_QUEUE)
    public void saveStockLog(Channel channel, Message message) throws IOException {

        String data = new String(message.getBody());
        UpdateBatchStockLog updateBatchStockLog = JSONObject.parseObject(data, UpdateBatchStockLog.class);

        stockLogService.insertBatch(updateBatchStockLog);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().
                getDeliveryTag(), false);

        log.info("规格库存日志消息处理完毕，手动执行ACK!");
    }

}
