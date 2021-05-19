/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.index;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.constant.IndexContant;
import com.leimingtech.modules.constant.IndexSyncServiceCode;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityDTO;
import com.leimingtech.modules.dto.reduce.ReduceActivityIndexDTO;
import com.leimingtech.modules.dto.reduce.ReduceGoodsDTO;
import com.leimingtech.modules.dto.reduce.ReduceRuleDTO;
import com.leimingtech.modules.enums.reduce.ReduceActivityEnum;
import com.leimingtech.modules.index.goods.service.GoodsIndexService;
import com.leimingtech.modules.service.reduce.ReduceActivityService;
import com.leimingtech.modules.service.reduce.ReduceGoodsService;
import com.leimingtech.modules.service.reduce.ReduceRuleService;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.beans.BeanCopier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author kuangweiguo
 * @version 1.0
 * @date 2019/6/28 5:21 PM
 */
@Component
@Slf4j
public class GoodsIndexConsumer {

    private static MonitorLogger logger = MonitorLoggerFactory.getMonitorLogger(GoodsIndexConsumer.class);

    @Autowired
    private GoodsIndexService goodsIndexService;

    @Autowired
    private EsDataUtils esDataUtils;
    @Autowired
    private ReduceActivityService reduceActivityService;
    @Autowired
    private ReduceGoodsService reduceGoodsService;
    @Autowired
    private ReduceRuleService reduceRuleService;
    @Autowired
    private RedisUtils redisUtils;


    /**
     * 批量同步商品索引（商品搜索结果集）
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_DATE)
    public void goodsIndexSync(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到批量同步商品索引消息，内容为：{}", msgText);
        // 定时更新商品es,删除先同步时间
        if (msgText.equals(IndexContant.TIME_GOODS_INDEX)) {
            String lastSyncTimeKey = IndexContant.REDIS_PREFIX + IndexContant.GOODS_INDEX_LAST_SYNC_TIME;
            redisUtils.delete(lastSyncTimeKey);
        }

        boolean result = goodsIndexService.goodsIndexSync();
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("批量同步商品索引消息处理完毕，手动执行ACK!");

    }


    /**
     * 批量更新商品索引
     *
     * @date 2019/11/19 18:12
     * @author lixiangx@leimingtech.com
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INDEX_BATCH_UPDATE)
    public void batchGoodsIndexSync(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到批量同步商品索引消息，内容为：{}", msgText);

        List<Long> goodsIdList = JSON.parseArray(msgText, Long.class);
        boolean result = goodsIndexService.goodsIndexBatchSync(goodsIdList);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("批量同步商品索引消息处理完毕，手动执行ACK!");

    }


    /**
     * 商品同步索引（单个）
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INDEX_UPDATE_SINGLE)
    public void goodsIndexUpdateByGoods(Channel channel, Message message) throws IOException {
        Long goodsId = Long.valueOf(new String(message.getBody()));
        log.info("接收到单个商品同步索引消息，内容为：{}", goodsId);

        boolean result = goodsIndexService.goodsIndexSyncByGoodsId(goodsId);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("单个商品同步消息处理完毕，手动执行ACK!");

    }

    /**
     * 单独更新商品销量
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INFO_SALE_NUM_UPDATE)
    public void updateGoodsInfoSaleNum(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("只更新商品索引销量字段消费者接收到消息，内容为：{}", bodyMsg);
        Map<String, Object> map = JSONObject.parseObject(bodyMsg);
        boolean result = goodsIndexService.updateSaleNum(map);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SPEC_SYNC_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("只更新商品索引销量字段处理完毕，手动执行ACK!");

    }

    /**
     * 更新商品索引运费模板
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_FREIGHT_TEMPLATE_UPDATE)
    public void updateFreightTemplate(Channel channel, Message message) throws IOException {
        String bodyMsg = new String(message.getBody());
        log.info("接收到更新商品运费模板消息，内容为：{}", bodyMsg);
        Map<String, Object> map = JSONObject.parseObject(bodyMsg);
        Long oldFreightTemplateId = Long.parseLong(map.get("oldFreightTemplateId").toString());
        Long newFreightTemplateId = Long.parseLong(map.get("newFreightTemplateId").toString());
        boolean result = goodsIndexService.updateFreightTemplateId(oldFreightTemplateId, newFreightTemplateId);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("接收到更新商品运费模板消息处理完毕，手动执行ACK!");

    }


    /**
     * 根据店铺更新商品索引
     *
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORE_GOODS_INDEX_UPDATE)
    public void goodsIndexByStoreId(Channel channel, Message message) throws IOException {

        String storeId = new String(message.getBody());
        log.info("接收到店铺Id消息，准备更新商品索引，内容为：{}", storeId);
        boolean result = goodsIndexService.goodsIndexSyncByStoreId(storeId);
        if (result) {
            logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
        } else {
            logger.error(IndexSyncServiceCode.UPDATE_GOODS_INDEX_BY_STOREID_ERROR.getCode(), IndexSyncServiceCode.UPDATE_GOODS_INDEX_BY_STOREID_ERROR.getMessage());
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("根据店铺ID更新商品索引消息处理完毕，手动执行ACK!");

    }

    /**
     * 商品添加满减标签
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INDEX_ADD_REDUCE_TAG)
    public void addGoodsReduceTag(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到商品es添加满减标签消息，内容为：{}", msgText);
        Integer reduceFlag = ReduceActivityEnum.REDUCE_FLAG_YES.value();

        try {
            List<Long> activityIds = JSONArray.parseArray(msgText, Long.class);

            this.updateGoodsReduceFlag(reduceFlag, activityIds);

            log.info("商品es添加满减标签消息处理完毕，手动执行ACK!");
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("商品es添加满减标签消息执行异常");

            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }

    /**
     * 商品移除满减标签
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INDEX_REMOVE_REDUCE_TAG)
    public void removeGoodsReduceTag(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到商品es移除满减标签消息，内容为：{}", msgText);
        Integer reduceFlag = ReduceActivityEnum.REDUCE_FLAG_NO.value();

        try {
            List<Long> activityIds = JSONArray.parseArray(msgText, Long.class);

            this.updateGoodsReduceFlag(reduceFlag, activityIds);

            log.info("商品es移除满减标签消息处理完毕，手动执行ACK!");
            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.info("商品es移除满减标签消息执行异常");

            // ACK手动确认处理消息
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        }
    }


    /**
     * 修改商品ES中的商品评价数量
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_ES_EVALUATE_UPDATE)
    public void updateGoodsEsEva(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到更新商品评价数量，内容为：{}", msgText);
        Map<String, Object> map = JSONObject.parseObject(msgText);
        goodsIndexService.updateGoodsEvaluate(map);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 功能描述：
     * <更新商品es满减标签>
     *
     * @param reduceFlag  更新后状态
     * @param activityIds 活动id集合
     * @return
     * @date 2020/2/27 15:07
     * @author 刘远杰
     **/
    private void updateGoodsReduceFlag(Integer reduceFlag, List<Long> activityIds) {
        List<ReduceActivityDTO> dtoList = reduceActivityService.getByIds(activityIds);
        // 更新商品es满减标签
        List<ReduceActivityIndexDTO> reduceActivityIndexDTOList = new ArrayList<>();
        dtoList.forEach(reduceActivity -> {
            ReduceActivityIndexDTO reduceActivityIndexDTO = new ReduceActivityIndexDTO();
            BeanCopier.create(ReduceActivityDTO.class, ReduceActivityIndexDTO.class, false)
                    .copy(reduceActivity, reduceActivityIndexDTO, null);

            // 获取活动关联商品类型
            List<ReduceGoodsDTO> reduceGoodsDTOList = reduceGoodsService.getByActivityId(reduceActivity.getId());
            reduceActivityIndexDTO.setGoodsList(reduceGoodsDTOList);
            // 获取活动关联商品类型
            reduceActivityIndexDTO.setRuleType(reduceActivity.getRuleType());
            List<ReduceRuleDTO> ruleList = reduceRuleService.getByActivityId(reduceActivity.getId());
            reduceActivityIndexDTO.setRuleList(ruleList);
            reduceActivityIndexDTOList.add(reduceActivityIndexDTO);
        });

        Set<Long> goodsIds = new HashSet<>();
        // 遍历活动集合，查询对应商品id
        for (ReduceActivityIndexDTO reduceActivityIndexDTO : reduceActivityIndexDTOList) {
            PageModelDTO pageModelDTO = new PageModelDTO();
            pageModelDTO.setIsPage(false);
            Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
            equalsSearchCondition.put("storeId", reduceActivityIndexDTO.getStoreId());
            // 获取不同条件商品id
            if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_ALL.value() == reduceActivityIndexDTO.getActivityGoodsScope()) {
                // 店铺活动，更新店铺所有商品es满减活动标识
                equalsSearchCondition.put("storeId", reduceActivityIndexDTO.getStoreId());
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_CLASS.value() == reduceActivityIndexDTO.getActivityGoodsScope()) {
                // 指定品牌，更新店铺指定品牌商品es满减活动标识
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                List<Long> relationIds = reduceActivityIndexDTO.getGoodsList().stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                inSearchCondition.put("secondStoreClassId", relationIds);
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_GOODS.value() == reduceActivityIndexDTO.getActivityGoodsScope()) {
                // 指定商品，更新店铺指定id商品es满减活动标识
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                List<Long> relationIds = reduceActivityIndexDTO.getGoodsList().stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                inSearchCondition.put("id", relationIds);
            } else if (ReduceActivityEnum.ACTIVITY_GOODS_SCOPE_BRAND.value() == reduceActivityIndexDTO.getActivityGoodsScope()) {
                // 指定品牌，更新店铺指定品牌商品es满减活动标识
                Map<String, List> inSearchCondition = pageModelDTO.getInSearchCondition();
                List<Long> relationIds = reduceActivityIndexDTO.getGoodsList().stream().map(ReduceGoodsDTO::getRelationId).collect(Collectors.toList());
                inSearchCondition.put("brandId", relationIds);
            }
            esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_INDEX);
            List<String> jsonRsList = pageModelDTO.getJsonRsList();
            if (CollectionUtils.isNotEmpty(jsonRsList)) {
                for (String jsonRs : jsonRsList) {
                    JSONObject jsonObject = JSONObject.parseObject(jsonRs);
                    Long goodsId = null;
                    Long storeId = null;
                    Long brandId = null;
                    Long secondStoreClassId = null;
                    if (jsonObject.get("id") != null) {
                        goodsId = Long.parseLong(jsonObject.get("id").toString());
                    }
                    if (jsonObject.get("storeId") != null) {
                        storeId = Long.parseLong(jsonObject.get("storeId").toString());
                    }
                    if (jsonObject.get("secondStoreClassId") != null) {
                        secondStoreClassId = Long.parseLong(jsonObject.get("secondStoreClassId").toString());
                    }
                    if (reduceFlag == ReduceActivityEnum.REDUCE_FLAG_NO.value()) {
                        // 活动结束需判断是否存在其他满减活动
                        List<ReduceActivityIndexDTO> goodsAllActivity = reduceActivityService.getGoodsAllActivity(goodsId, storeId, brandId, secondStoreClassId);
                        if (CollectionUtils.isEmpty(goodsAllActivity)) {
                            goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                        }
                    } else {
                        goodsIds.add(Long.parseLong(jsonObject.get("id").toString()));
                    }
                }
            }
        }
        List<Map<String, Object>> goodsEsUpdateList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(goodsIds)) {
            for (Long goodsId : goodsIds) {
                Map<String, Object> goodsEsUpdate = new HashMap<>();
                goodsEsUpdate.put("id", goodsId);
                goodsEsUpdate.put("reduceFlag", reduceFlag);
                goodsEsUpdateList.add(goodsEsUpdate);
            }
            esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_INDEX, "id", JSONArray.toJSONString(goodsEsUpdateList));
        }
    }

    /**
     * 功能描述 增量定时更新 商品的评论数 收藏数 浏览数量
     *
     * @param channel 信道
     * @param message 消息体
     * @return void
     * @author lishuo
     * @date 9/7/2020
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GOODS_INFO_INDEX_UPDATE_DATE)
    public void updateGoodsInfo(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到批量同步商品评论等索引消息，内容为：{}", msgText);
        // 定时更新商品评论收藏es,删除上次同步时间
        if (msgText.equals(IndexContant.TIME_GOODS_INDEX)) {
            boolean result = goodsIndexService.goodsInfoIndexSync();
            if (result) {
                logger.info(IndexSyncServiceCode.SUCCESSFUL_OK.getCode(), IndexSyncServiceCode.SUCCESSFUL_OK.getMessage());
            } else {
                logger.error(IndexSyncServiceCode.INDEX_SYNC_ERROR.getCode(), IndexSyncServiceCode.INDEX_SYNC_ERROR.getMessage());
            }
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("批量同步商品评论等索引消息处理完毕，手动执行ACK!");
    }

}
