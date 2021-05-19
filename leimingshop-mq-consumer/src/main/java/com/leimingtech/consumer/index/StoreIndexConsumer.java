/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index;

import com.alibaba.fastjson.JSONArray;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.index.store.StoreIndexService;
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
 * @Description
 * @Data: 2019/12/5 19:42
 * @Author: chengqian
 * @Version: 1.0
 */
@Component
@Slf4j
public class StoreIndexConsumer {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(StoreIndexConsumer.class);

    @Autowired
    private StoreIndexService storeIndexService;

    /**
     * 店铺索引同步
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE)
    public void storeIndexSync(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到批量同步店铺索引消息，内容为：{}", msgText);

        boolean result = storeIndexService.syncStoreIndex();
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.UPDATE_INDEX_SUCCESS.getCode(), IndexSyncServiceCode.UPDATE_INDEX_SUCCESS.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("批量同步店铺索引消息处理完毕，手动执行ACK!");

    }

    /**
     * 根据店铺ID 更新店铺索引
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORE_INDEX_UPDATE_SINGLE)
    public void storeIndexSyncByStoreId(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到更新店铺索引消息，内容为：{}", msgText);

        Long storeId = JSONArray.parseObject(msgText, Long.class);
        boolean result = storeIndexService.syncStoreIndexByStoreId(storeId);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.UPDATE_GOODS_INDEX_BY_STOREID_ERROR.getCode(), IndexSyncServiceCode.UPDATE_GOODS_INDEX_BY_STOREID_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("更新店铺索引消息处理完毕，手动执行ACK!");

    }

    /**
     * 删除店铺索引
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_DELETE_STORE_INDEX)
    public void deleteIndex(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到删除店铺索引消息，内容为：{}", msgText);

        Long storeId = JSONArray.parseObject(msgText, Long.class);
        boolean result = storeIndexService.deleteStoreIndex(storeId);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getCode(), IndexSyncServiceCode.CREATE_DELETE_INDEX_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("删除店铺索引消息处理完毕，手动执行ACK!");

    }


}
