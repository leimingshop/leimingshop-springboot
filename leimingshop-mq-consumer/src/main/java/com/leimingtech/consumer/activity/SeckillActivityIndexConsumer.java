/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.ConvertUtils;
import com.leimingtech.modules.constant.ActivityRedisConstants;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.PageModelDTO;
import com.leimingtech.modules.dto.activity.goods.SpecActivityEsDTO;
import com.leimingtech.modules.dto.cart.CartDTO;
import com.leimingtech.modules.dto.goods.spec.GoodsSpecDTO;
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.seckill.SeckillActivityEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.utils.EsDataUtils;
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
import java.util.*;

/**
 * <秒杀活动索引维护消息队列>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/12/26
 */
@Component
@Slf4j
public class SeckillActivityIndexConsumer {

    private static final String SPEC_ACTIVITY_LIST = "specActivityList";

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ActivityGoodsService activityGoodsService;

    @Autowired
    private GoodsSpecService goodsSpecService;


    /**
     * 秒杀活动索引全量同步（商品es、购物车es、redis）
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_SECKILL_ACTIVITY_SYNC_ES_QUEUE)
    public void seckillActivitySyncEsQueue(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到秒杀活动索引同步消息，内容为：{}", msgText);

        try {
            // 删除商品es秒杀活动
            deleteGoodsEsSeckillActivity();

            // 删除购物车es秒杀活动
            deleteCartEsSeckillActivity();

            // 查询redis秒杀库存价格
            deleteRedisSeckillActivity();

            // 查询平台所有秒杀活动
            List<SpecActivityEsDTO> allSeckillSpecActivityEs = activityGoodsService.getAllSeckillSpecActivity();

            // 更新商品es、购物车es、redis库存价格
            updateSeckillEsAndRedis(allSeckillSpecActivityEs);
        } catch (Exception e) {
            log.info("秒杀活动索引同步失败:" + e);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("秒杀活动索引同步消息处理完毕，手动执行ACK!");

    }

    /**
     * 功能描述：
     * <更新商品es、购物车es、redis库存价格>
     *
     * @param allSeckillSpecActivityEs 秒杀商品
     * @return
     * @date 2020/3/26 18:27
     * @author 刘远杰
     **/
    private void updateSeckillEsAndRedis(List<SpecActivityEsDTO> allSeckillSpecActivityEs) {
        List<Map<String, Object>> updateGoodsDataList = new ArrayList<>();
        allSeckillSpecActivityEs.forEach(specActivityEsDTO -> {
            // 查询商品es
            String specStr = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, specActivityEsDTO.getSpecId().toString());
            JSONObject specJson = JSONObject.parseObject(specStr);

            if (specJson != null) {
                // 更新商品es秒杀活动
                if (specJson.get(SPEC_ACTIVITY_LIST) != null) {
                    // 存在活动 - 移除秒杀 - 新增秒杀
                    List<SpecActivityVO> specActivityList = JSONArray.parseArray(specJson.get("specActivityList").toString(), SpecActivityVO.class);
                    for (SpecActivityVO specActivityVO : specActivityList) {
                        if (specActivityVO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())) {
                            // 移除此活动数据
                            specActivityList.remove(specActivityVO);
                            break;
                        }
                    }
                    specActivityList.add(ConvertUtils.sourceToTarget(specActivityEsDTO, SpecActivityVO.class));
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", specActivityEsDTO.getSpecId());
                    updateData.put("specActivityList", specActivityList);
                    updateGoodsDataList.add(updateData);
                } else {
                    // 不存在活动 - 新增秒杀
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", specActivityEsDTO.getSpecId());
                    updateData.put("specActivityList", Collections.singleton(ConvertUtils.sourceToTarget(specActivityEsDTO, SpecActivityVO.class)));
                    updateGoodsDataList.add(updateData);
                }
            }

            if (SeckillActivityEnum.ACTIVITY_STATE_START.value() == specActivityEsDTO.getActivityState()) {
                // 更新指定规格购物车
                PageModelDTO updatePageMOdelDTO = new PageModelDTO();
                Map<String, Object> equalsSearchCondition1 = updatePageMOdelDTO.getEqualsSearchCondition();
                equalsSearchCondition1.put("specId", specActivityEsDTO.getSpecId());

                // 开始活动更新购物车 redis
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("activityId", specActivityEsDTO.getActivityId());
                updateData.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
                updateData.put("activitySurplusStorage", specActivityEsDTO.getActivitySurplusStorage());
                updateData.put("activityEndDate", specActivityEsDTO.getActivityEndDate());
                updateData.put("restrictionQuantity", specActivityEsDTO.getRestrictionQuantity());
                updateData.put("specSellPrice", specActivityEsDTO.getActivityPrice());

                esDataUtils.updateByQueryDate(updatePageMOdelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                log.info("更新购物车es秒杀活动数据成功，specId:{}", specActivityEsDTO.getSpecId());

                // 保存秒杀redis活动库存
                String storageKey = ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + specActivityEsDTO.getActivityId() + "_" + specActivityEsDTO.getSpecId();
                redisUtils.set(storageKey, specActivityEsDTO.getActivitySurplusStorage(), -1);
                log.info("保存秒杀redis活动库存成功，storageKey:{}", storageKey);

                // 保存秒杀redis活动价格
                String priceKey = ActivityRedisConstants.SECKILL_GOODS_PRICE + specActivityEsDTO.getActivityId() + "_" + specActivityEsDTO.getSpecId();
                redisUtils.set(priceKey, specActivityEsDTO.getActivityPrice().toString(), -1);
                log.info("保存秒杀redis活动价格成功，priceKey:{}", storageKey);
            }
        });
        if (CollectionUtils.isNotEmpty(updateGoodsDataList)) {
            esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(updateGoodsDataList));
            log.info("更新商品es秒杀活动数据成功");
        }
    }

    /**
     * 功能描述：
     * <删除商品es秒杀活动>
     *
     * @date 2020/3/26 16:41
     * @author 刘远杰
     **/
    private void deleteGoodsEsSeckillActivity() {
        // 查询秒杀商品 - 移除秒杀活动 - 更新商品es
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> subEqualParams = new HashMap<>();
        subEqualParams.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
        subEqualsSearchCondition.put("specActivityList", subEqualParams);

        // 查询秒杀商品
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();

        // 移除秒杀活动
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        jsonRsList.forEach(jsonRs -> {
            JSONObject specJson = JSONObject.parseObject(jsonRs);
            if (specJson.get(SPEC_ACTIVITY_LIST) != null) {
                List<SpecActivityVO> specActivityList = JSONArray.parseArray(specJson.get("specActivityList").toString(), SpecActivityVO.class);
                for (SpecActivityVO specActivityVO : specActivityList) {
                    if (specActivityVO.getActivityType().equals(ActivityTypeEnum.SECKILL_ACTIVITY.value())) {
                        // 移除此活动数据
                        specActivityList.remove(specActivityVO);
                        break;
                    }
                }
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("id", specJson.get("id"));
                updateData.put("specActivityList", specActivityList);
                updateDataList.add(updateData);
            }
        });

        if (CollectionUtils.isNotEmpty(updateDataList)) {
            // 更新商品es
            esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(updateDataList));
            log.info("删除商品es秒杀活动数据成功");
        }
    }

    /**
     * 功能描述：
     * <删除购物车es秒杀活动>
     *
     * @date 2020/3/26 16:41
     * @author 刘远杰
     **/
    private void deleteCartEsSeckillActivity() {
        // 查询秒杀商品 - 移除秒杀活动 - 更新商品es
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
        equalsSearchCondition.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());
        pageModelDTO.setCollapseField("specId");

        // 查询秒杀商品购物车 - 按照规格id分组
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();
        List<CartDTO> cartDTOList = JSONArray.parseArray(jsonRsList.toString(), CartDTO.class);

        // 移除秒杀活动
        if (CollectionUtils.isNotEmpty(cartDTOList)) {
            cartDTOList.forEach(cartDTO -> {
                // 更新指定规格id购物车
                PageModelDTO updatePageMOdelDTO = new PageModelDTO();
                Map<String, Object> equalsSearchCondition1 = updatePageMOdelDTO.getEqualsSearchCondition();
                equalsSearchCondition1.put("specId", cartDTO.getSpecId());
                equalsSearchCondition1.put("activityType", ActivityTypeEnum.SECKILL_ACTIVITY.value());

                // 设置更新结果
                GoodsSpecDTO goodsSpecDTO = goodsSpecService.get(cartDTO.getSpecId());
                Map<String, Object> updateData = new HashMap<>();
                updateData.put("activityType", ActivityTypeEnum.NO_ACTIVITY.value());
                if (goodsSpecDTO != null) {
                    updateData.put("specSellPrice", goodsSpecDTO.getSpecSellPrice());
                }
                esDataUtils.updateByQueryDate(updatePageMOdelDTO, ElasticSearchConstant.CART_INDEX, JSONObject.toJSONString(updateData));
                log.info("删除购物车es秒杀活动数据成功，specId:{}", cartDTO.getSpecId());
            });
        }
    }

    /**
     * 功能描述：
     * <删除redis秒杀活动价格库存>
     *
     * @date 2020/3/26 16:41
     * @author 刘远杰
     **/
    private void deleteRedisSeckillActivity() {
        // 删除秒杀库存
        redisUtils.deleteByPattern(ActivityRedisConstants.SECKILL_GOODS_SURPLUS_STORAGE + "*");
        log.info("删除redis秒杀活动库存成功");

        // 删除秒杀价格
        redisUtils.deleteByPattern(ActivityRedisConstants.SECKILL_GOODS_PRICE + "*");
        log.info("删除redis秒杀活动价格成功");
    }


}
