/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.order;

import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.logging.MonitorLogger;
import com.leimingtech.logging.MonitorLoggerFactory;
import com.leimingtech.modules.constant.order.RedisConstants;
import com.leimingtech.modules.dto.order.SaveOrderRedisDTO;
import com.leimingtech.modules.service.order.OrderService;
import com.leimingtech.modules.statuscode.OrderStatusCode;
import com.leimingtech.mq.constant.MqConstant;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 购物车保存订单消费者
 * @Date: 16:52 2019/6/22
 * @Version: V1.0
 */
@Slf4j
@Component
public class ConfirmOrderConsumer {

    private static MonitorLogger mlogger = MonitorLoggerFactory.getMonitorLogger(ConfirmOrderConsumer.class);

    @Autowired

    private OrderService orderService;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 购物车保存订单消费方法
     *
     * @param channel: 消息通道
     * @param message: 消息实体
     * @date 2019/6/22 16:58
     * @author LX lixiangx@leimingtech.com
     **/
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_CONFIRM_ORDER_QUEUE)
    public void confirmOrder(Channel channel, Message message) throws IOException {
        String orderConfirmId = new String(message.getBody());
        log.info("接收到订单异步保存消息，内容为:{}", orderConfirmId);
        try {
            // 异步处理订单保存
            orderService.packOrderFromConsumer(Long.valueOf(orderConfirmId));

            // 日志
            Map<String, String> logMap = new HashMap<>();
            logMap.put("orderConfirmId", orderConfirmId);
            mlogger.info(OrderStatusCode.ORDER_CONFIRM_SVAE_SUCCESS_CODE, OrderStatusCode.ORDER_CONFIRM_SAVE_SUCCESS_MESSAGE, logMap);
        } catch (Exception e) {
            // 异常更新订单保存缓存信息
            SaveOrderRedisDTO saveOrderRedisDTO = new SaveOrderRedisDTO();
            saveOrderRedisDTO.setResultCode(RedisConstants.FAIL);
            saveOrderRedisDTO.setResultMsg("订单保存失败");
            redisUtils.set(RedisConstants.SAVE_ORDER_PREFIX + orderConfirmId, saveOrderRedisDTO, RedisConstants.JEDIS_EXPIRE);
            mlogger.info(OrderStatusCode.ORDER_CONFIRM_SVAE_SUCCESS_CODE, OrderStatusCode.ORDER_CONFIRM_SAVE_SUCCESS_MESSAGE, e);
            log.error("订单生成失败" + e);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        log.info("购物车保存订单处理完毕，手动执行ACK!");
    }
}
