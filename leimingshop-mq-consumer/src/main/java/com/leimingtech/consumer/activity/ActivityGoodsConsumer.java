/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.activity;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.EsConflictException;
import com.leimingtech.modules.constant.ElasticSearchConstant;
import com.leimingtech.modules.dto.activity.goods.UpdateActivitySurplusStorageDTO;
import com.leimingtech.modules.goods.activity.SpecActivityVO;
import com.leimingtech.modules.statuscode.ActivityStatusCode;
import com.leimingtech.modules.utils.EsDataUtils;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

/**
 * <活动商品消费者>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/24
 */
@Slf4j
@Component
public class ActivityGoodsConsumer {

    @Autowired
    private EsDataUtils esDataUtils;

    /**
     * 功能描述：
     * <修改商品es活动库存>
     *
     * @param channel
     * @param message
     * @return
     * @date 2020/3/24 9:21
     * @author 刘远杰
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_UPDATE_ES_GOODS_ACTIVITY_SURPLUS_STORAGE_QUEUE)
    public void updateEsGoodsActivitySurplusStorage(Channel channel, Message message) throws IOException {
        try {
            String msgText = new String(message.getBody());
            log.debug("接收到修改商品es活动库存消息，内容为：{}", msgText);

            if (StringUtils.isNotBlank(msgText)) {

                List<UpdateActivitySurplusStorageDTO> updateActivitySurplusStorageList = JSONArray.parseArray(msgText, UpdateActivitySurplusStorageDTO.class);
                if (CollectionUtils.isNotEmpty(updateActivitySurplusStorageList)) {
                    for (UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO : updateActivitySurplusStorageList) {
                        Integer activitySurplusStorage = updateActivitySurplusStorageDTO.getActivitySurplusStorage();

                        // 更新商品es活动库存：查询商品es - 得到商品活动集合 - 移除该活动 - 重新设置该活动 - 更新商品es
                        SpecActivityVO specActivity = null;
                        // 查询商品es，获得活动集合
                        String jsonRs = esDataUtils.getDateById(ElasticSearchConstant.GOODS_SPEC_INDEX, updateActivitySurplusStorageDTO.getSpecId().toString());
                        JSONObject specJson = JSONObject.parseObject(jsonRs);
                        List<SpecActivityVO> specActivityList = JSONArray.parseArray(specJson.get("specActivityList").toString(), SpecActivityVO.class);
                        if (specJson != null) {
                            if (specJson.get("specActivityList") != null) {
                                for (SpecActivityVO specActivityVO : specActivityList) {
                                    if (specActivityVO.getActivityId().equals(updateActivitySurplusStorageDTO.getActivityId()) && specActivityVO.getActivityType().equals(updateActivitySurplusStorageDTO.getActivityType())) {
                                        // 修改剩余活动库存
                                        specActivity = specActivityVO;
                                        specActivity.setActivitySurplusStorage(activitySurplusStorage);
                                        break;
                                    }
                                }
                            }
                            // 更新商品es活动库存 重试3次
                            this.esUpdate(specActivity, updateActivitySurplusStorageDTO);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("修改商品es活动库存消息执行异常" + e);
        }

        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.debug("修改商品es活动库存消息处理完毕，手动执行ACK!");
    }

    /**
     * 功能描述：
     * <更新商品es活动库存 重试三次>
     *
     * @param specActivity
     * @param updateActivitySurplusStorageDTO
     * @return
     * @date 2020/4/14 15:21
     * @author 刘远杰
     **/
    @Retryable(value = EsConflictException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000L, multiplier = 2))
    public void esUpdate(SpecActivityVO specActivity, UpdateActivitySurplusStorageDTO updateActivitySurplusStorageDTO) {
        if (specActivity != null) {
            // 更新es活动
            esDataUtils.updateSubListById(ElasticSearchConstant.GOODS_SPEC_INDEX, updateActivitySurplusStorageDTO.getSpecId().toString(), JSONObject.toJSONString(specActivity), "id", "specActivityList");
            log.info("修改商品es活动库存成功，activityId:{}，activityType:{}，specId:{}", updateActivitySurplusStorageDTO.getActivityId(), updateActivitySurplusStorageDTO.getActivityType(), updateActivitySurplusStorageDTO.getSpecId());
        }
    }

    @Recover
    public void recover(EsConflictException e) {
        log.error("修改商品es活动库存异常：" + e.getMessage());
        throw new ServiceException(ActivityStatusCode.UPDATE_GOODS_SPEC_ES_EXCEPTION);
    }

}
