/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.statis;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.leimingtech.commons.tools.utils.DateUtils;
import com.leimingtech.modules.constants.CollectionName;
import com.leimingtech.modules.dto.activity.ActivityPvDTO;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 优惠券活动PV统计
 *
 * @author chengqian
 * @version V1.0
 * @date2020-2-13 09：10
 **/
@Slf4j
@Component
public class MqCouponsPvConsumer {

    public static final String LIST_STR = "list";

    @Autowired
    private NosqlService nosqlService;


    /**
     * 优惠券活动PV统计
     *
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_COUPONS_PV_QUEUE)
    public void cartGoodsReduceRemind(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("优惠券活动PV统计通知消息接收，内容为:{}", msgText);
        JSONObject params = JSONObject.parseObject(msgText);
        Map<String, Object> jsonObject = (Map<String, Object>) params;
        if (jsonObject == null && jsonObject.get(LIST_STR) == null) {
            return;
        }

        List<ActivityPvDTO> list = JSONArray.parseArray(jsonObject.get("list").toString(), ActivityPvDTO.class);
        String ip = (String) jsonObject.get("ip");
        Date createTime = new Date();
        String format = DateUtils.format(createTime);
        Date parse = DateUtils.parse(format, DateUtils.DATE_PATTERN);
        list.stream().forEach(a -> {
            a.setIpDetail(ip);
            a.setActivityType(1);
            a.setActivityId(a.getId());
            a.setId(IdWorker.getId());
            a.setCreateTime(createTime);
            a.setCreateDayTime(parse);
        });
        nosqlService.saveBatch(list, CollectionName.ACTIVITY_PV_DETAIL);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().

                getDeliveryTag(), false);
        log.info("优惠券活动PV统计通知消息处理完毕，手动执行ACK!");
    }
}
