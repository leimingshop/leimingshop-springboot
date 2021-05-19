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
import com.leimingtech.modules.enums.ActivityTypeEnum;
import com.leimingtech.modules.enums.group.GroupsEnum;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.service.activity.goods.ActivityGoodsService;
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
 * <拼团活动索引维护消息队列>
 *
 * @since 1.0 2019/12/26
 */
@Component
@Slf4j
public class GroupActivityIndexConsumer {

    @Autowired
    private EsDataUtils esDataUtils;

    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ActivityGoodsService activityGoodsService;


    /**
     * 拼团活动索引全量同步（商品es、redis）
     *
     * @param channel: 信道
     * @param message: 消息体
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_GROUP_ACTIVITY_SYNC_ES_QUEUE)
    public void groupActivitySyncEsQueue(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到拼团活动索引同步消息，内容为：{}", msgText);

        try {
            // 删除商品es拼团活动
            deleteGoodsEsGroupActivity();

            // 查询redis拼团库存价格
            deleteRedisGroupActivity();

            // 查询平台所有拼团活动
            List<SpecActivityEsDTO> allGroupSpecActivityEs = activityGoodsService.getAllGroupSpecActivity();

            // 更新商品es、redis库存价格
            updateGroupEsAndRedis(allGroupSpecActivityEs);
        } catch (Exception e) {
            log.info("拼团活动索引同步失败:" + e);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("拼团活动索引同步消息处理完毕，手动执行ACK!");

    }

    /**
     * 功能描述：
     * <更新商品es、购物车es、redis库存价格>
     *
     * @param allGroupSpecActivityEs 拼团商品
     * @return
     **/
    private void updateGroupEsAndRedis(List<SpecActivityEsDTO> allGroupSpecActivityEs) {
        List<Map<String, Object>> updateGoodsDataList = new ArrayList<>();
        allGroupSpecActivityEs.forEach(specActivityEsDTO -> {
            // 查询商品es
            String specStr = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, specActivityEsDTO.getSpecId().toString());
            JSONObject specJson = JSONObject.parseObject(specStr);

            // 更新商品es拼团活动
            if (specJson != null) {
                if (specJson.get("specActivityList") != null) {
                    // 存在活动 - 拼团秒杀 - 新增拼团
                    List<SpecActivityVO> specActivityList = JSONArray.parseArray(specJson.get("specActivityList").toString(), SpecActivityVO.class);
                    for (SpecActivityVO specActivityVO : specActivityList) {
                        if (specActivityVO.getActivityType().equals(ActivityTypeEnum.GROUP_ACTIVITY.value())) {
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
                    // 不存在活动 - 新增拼团
                    Map<String, Object> updateData = new HashMap<>();
                    updateData.put("id", specActivityEsDTO.getSpecId());
                    updateData.put("specActivityList", Collections.singleton(ConvertUtils.sourceToTarget(specActivityEsDTO, SpecActivityVO.class)));
                    updateGoodsDataList.add(updateData);
                }
            }

            if (GroupsEnum.ACTIVITY_STATUS_ONGOING.value() == specActivityEsDTO.getActivityState()) {

                // 保存拼团redis活动库存
                String storageKey = ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + specActivityEsDTO.getActivityId() + "_" + specActivityEsDTO.getSpecId();
                redisUtils.set(storageKey, specActivityEsDTO.getActivitySurplusStorage(), -1);
                log.info("保存拼团redis活动库存成功，storageKey:{}", storageKey);

                // 保存拼团redis活动价格
                String priceKey = ActivityRedisConstants.GROUP_GOODS_PRICE + specActivityEsDTO.getActivityId() + "_" + specActivityEsDTO.getSpecId();
                redisUtils.set(priceKey, specActivityEsDTO.getActivityPrice().toString(), -1);
                log.info("保存拼团redis活动价格成功，priceKey:{}", storageKey);
            }
        });
        if (CollectionUtils.isNotEmpty(updateGoodsDataList)) {
            esDataUtils.updateDataBatch(ElasticSearchConstant.GOODS_SPEC_INDEX, "id", JSONArray.toJSONString(updateGoodsDataList));
            log.info("更新商品es拼团活动数据成功");
        }
    }

    /**
     * 功能描述：
     * <删除商品es拼团活动>
     **/
    private void deleteGoodsEsGroupActivity() {
        // 查询拼团商品 - 移除拼团活动 - 更新商品es
        PageModelDTO pageModelDTO = new PageModelDTO();
        pageModelDTO.setIsPage(false);
        Map<String, Map<String, Object>> subEqualsSearchCondition = pageModelDTO.getSubEqualsSearchCondition();
        Map<String, Object> subEqualParams = new HashMap<>();
        subEqualParams.put("activityType", ActivityTypeEnum.GROUP_ACTIVITY.value());
        subEqualsSearchCondition.put("specActivityList", subEqualParams);

        // 查询拼团商品
        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.GOODS_SPEC_INDEX);
        List<String> jsonRsList = pageModelDTO.getJsonRsList();

        // 移除拼团活动
        List<Map<String, Object>> updateDataList = new ArrayList<>();
        jsonRsList.forEach(jsonRs -> {
            JSONObject specJson = JSONObject.parseObject(jsonRs);
            if (specJson.get("specActivityList") != null) {
                List<SpecActivityVO> specActivityList = JSONArray.parseArray(specJson.get("specActivityList").toString(), SpecActivityVO.class);
                for (SpecActivityVO specActivityVO : specActivityList) {
                    if (specActivityVO.getActivityType().equals(ActivityTypeEnum.GROUP_ACTIVITY.value())) {
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
            log.info("删除商品es拼团活动数据成功");
        }
    }


    /**
     * 功能描述：
     * <删除redis拼团活动价格库存>
     **/
    private void deleteRedisGroupActivity() {
        // 删除拼团库存
        redisUtils.deleteByPattern(ActivityRedisConstants.GROUP_GOODS_SURPLUS_STORAGE + "*");
        log.info("删除redis拼团活动库存成功");

        // 删除拼团价格
        redisUtils.deleteByPattern(ActivityRedisConstants.GROUP_GOODS_PRICE + "*");
        log.info("删除redis拼团活动价格成功");
    }


}
