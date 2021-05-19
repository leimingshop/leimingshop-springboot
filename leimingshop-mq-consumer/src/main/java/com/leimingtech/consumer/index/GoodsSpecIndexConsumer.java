/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.index.goods.service.GoodsSpecIndexService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 规格索引更新消费者
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/13 11:35
 **/
@Slf4j
@Component
public class GoodsSpecIndexConsumer {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(GoodsIndexConsumer.class);

    @Autowired
    private GoodsSpecIndexService goodsSpecIndexService;

    /**
     * 商品详情同步队列
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_DATE)
    public void goodsSpecIndexSync(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到批量同步商品规格索引消息，内容为：{}", msgText);

        boolean result = goodsSpecIndexService.goodsIndexSpecSync();
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("批量同步商品规格索引消息处理完毕，手动执行ACK!");

    }

    /**
     * 根据商品ID集合更新商品规格索引
     *
     * @date 2020/1/6 14:29
     * @author lixiangx@leimingtech.com
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE)
    public void goodsSpecIndexUpdateByGoods(Channel channel, Message message) throws IOException {
        String goodsIdJson = new String(message.getBody());
        log.info("接收根据商品ID集合更新商品规格索引消息，内容为：{}", goodsIdJson);

        List<Long> goodsIdList = JSONArray.parseArray(goodsIdJson, Long.class);

        // 更新索引
        boolean result = goodsSpecIndexService.goodsSpecIndexSyncByGoodsId(goodsIdList);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("根据商品ID集合更新商品规格索引消息处理完毕，手动执行ACK!");

    }


    /**
     * @Author: SWH ab4856812@163.com
     * @Description: 根据规格id同步商品规格索引
     * @Date: 2019/8/5 21:43
     * @Version: V1.0
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_INDEX_UPDATE_SINGLE)
    public void goodsSpecIndexUpdateBySpecIds(Channel channel, Message message) throws IOException {
        String specIds = new String(message.getBody());
        log.info("接收到消息，内容为：{}", specIds);
        List<Long> specIdList = JSON.parseArray(specIds, Long.class);
        boolean result = goodsSpecIndexService.goodsSpecIndexSyncBySpecsId(specIdList);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("单个商品规格同步索引消息处理完毕，手动执行ACK!");

    }


    /**
     * 只更新规格索引销量字段
     *
     * @date 2020/4/13 11:39
     * @author lixiangx@leimingtech.com
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_SALE_NUM_UPDATE)
    public void updateSaleNum(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("只更新规格索引销量字段消费者接收到消息，内容为：{}", bodyMsg);
        Map<String, Object> map = JSONObject.parseObject(bodyMsg);
        boolean result = goodsSpecIndexService.updateSaleNum(map);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("单个商品规格同步索引消息处理完毕，手动执行ACK!");

    }

    /**
     * 只更新规格索引价格字段
     *
     * @date 2020年5月4日17:06:30
     * @author xuzhch
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_PRICE_INDEX_UPDATE)
    public void updatePrice(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("只更新规格索引价格字段消费者接收到消息，内容为：{}", bodyMsg);
        List<Long> specIds = JSONArray.parseArray(bodyMsg, Long.class);
        if (!BeanUtil.isEmpty(specIds)) {
            boolean result = goodsSpecIndexService.updatePrice(specIds);
            if (result) {
                logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
            } else {
                logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
            }
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info("规格价格商品索引同步处理完毕，手动执行ACK!");

    }

    /**
     * 只更新规格索引库存字段
     *
     * @date 2020年5月4日17:06:30
     * @author xuzhch
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_STORAGE_INDEX_UPDATE)
    public void updateStorage(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("只更新规格索引库存字段消费者接收到消息，内容为：{}", bodyMsg);
        List<Long> specIds = JSONArray.parseArray(bodyMsg, Long.class);
        if (!BeanUtil.isEmpty(specIds)) {
            boolean result = goodsSpecIndexService.updateStorage(specIds);
            if (result) {
                logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
            } else {
                logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
            }
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info("规格库存商品索引同步处理完毕，手动执行ACK!");

    }

    /**
     * 只更新规格索引库存字段
     *
     * @date 2020年5月4日17:06:30
     * @author xuzhch
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_SPEC_SHOW_INDEX_UPDATE)
    public void updateShow(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("只更新规格上下架索引字段消费者接收到消息，内容为：{}", bodyMsg);
        List<Long> specIds = JSONArray.parseArray(bodyMsg, Long.class);
        if (!BeanUtil.isEmpty(specIds)) {
            boolean result = goodsSpecIndexService.updateShow(specIds);
            if (result) {
                logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
            } else {
                logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
            }
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
        log.info("规格上下架索引同步处理完毕，手动执行ACK!");

    }
}
