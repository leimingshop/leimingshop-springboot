/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.cart;

import com.alibaba.fastjson.JSONArray;
import com.leimingtech.modules.dto.cart.UpdateCartStatusDTO;
import com.leimingtech.modules.service.cart.CartService;
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


/**
 * @Description 修改购物车商品状态消费者
 * @Data: 2019/7/24 11:17
 * @Author: chengqian
 * @Version: 1.0
 */
@Slf4j
@Component
public class MqCartStatusConsumer {

    @Autowired
    private CartService cartService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_CART_GOODS_STATUS)
    public void updateCartStatus(Channel channel, Message message) throws IOException {
        String data = new String(message.getBody());
        log.info("修改购物车商品状态消息接收，内容为:{}", data);

        List<UpdateCartStatusDTO> updateCartStatusDTO = JSONArray.parseArray(data, UpdateCartStatusDTO.class);

        cartService.updateCartStatus(updateCartStatusDTO);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().
                getDeliveryTag(), false);
        log.info("修改购物车商品状态消息处理完毕，手动执行ACK!");
    }

    /**
     * 店铺禁用，根据店铺ID 修改购物车商品状态
     *
     * @param channel
     * @param message
     * @throws IOException
     */
    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORE_ID_CART_GOODS_STATUS)
    public void updateCartGoodsStatus(Channel channel, Message message) throws IOException {
        String storeId = new String(message.getBody());
        log.info("修改购物车商品状态消息接收，内容为:{}", storeId);

        cartService.updateCartGoodsStatusByStoreId(storeId);
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().
                getDeliveryTag(), false);
        log.info("修改购物车商品状态消息处理完毕，手动执行ACK!");
    }
}
