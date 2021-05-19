/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.store;

import cn.hutool.core.util.IdUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.dto.goods.GoodsDTO;
import com.leimingtech.modules.dto.goods.price.GoodsSpecStorageUpdateDTO;
import com.leimingtech.modules.dto.warning.StorageWarningDTO;
import com.leimingtech.modules.service.goods.spec.GoodsSpecService;
import com.leimingtech.modules.service.user.StoreUserService;
import com.leimingtech.modules.service.warning.StorageWarningService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: weixianchun
 * @Description: seller日志队列接收者
 * @Date :2019/6/28 13:50
 * @Version V1.0
 **/
@Slf4j
@Component
public class MqStorageWarnConsumer {

    @Autowired
    private GoodsSpecService goodsSpecService;
    @Autowired
    private StorageWarningService storageWarningService;
    @Autowired
    private StoreUserService storeUserService;

    @Resource
    private RabbitMqSendService rabbitMqSendService;
    @Autowired
    private SysMessageService sysMessageService;

    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORAGE_WARNING_QUEUE)
    public void receiverLoginLog(Channel channel, Message message) throws IOException {

        String msgText = new String(message.getBody());
        log.info("接收到日志消息，内容为：{}", msgText);
        List<GoodsSpecStorageUpdateDTO> goodsSpecStorageUpdateDTOList = JSONArray.parseArray(msgText, GoodsSpecStorageUpdateDTO.class);
        for (GoodsSpecStorageUpdateDTO goodsSpecStorageUpdateDTO : goodsSpecStorageUpdateDTOList) {
            // 查询出店铺信息
            GoodsDTO goodsDTO = goodsSpecService.getStoreIdBySpecId(goodsSpecStorageUpdateDTO.getId());
            if (goodsDTO == null) {
                break;
            }
            // 查询出店铺商品预警值
            StorageWarningDTO storageWarningDTO = storageWarningService.get(goodsDTO.getStoreId());
            if (storageWarningDTO == null) {
                break;
            }

            if (storageWarningDTO.getStorage() < goodsSpecStorageUpdateDTO.getSpecStorage()) {
                break;
            }


            Map<String, Object> map = new HashMap<>(16);

            // 库存预警
            // 获取用户信息
            // 创建发送站内信用户信息集合
            Map<Long, String> storeMap = new HashMap<>(5);
            storeMap.put(goodsDTO.getStoreId(), goodsDTO.getStoreName());
            map.put("storeMap", storeMap);

            //设置用户手机号、站内信数据
            String phone = storeUserService.getStorePhone(goodsDTO.getStoreId());
            map.put("mobile", phone);

            // 创建站内信所需参数Map
            Map<String, String> paramsMap = new HashMap<>(10);
            paramsMap.put("storeName", goodsDTO.getStoreName());
            paramsMap.put("goodsName", goodsDTO.getGoodsName());
            paramsMap.put("goodsId", goodsSpecStorageUpdateDTO.getGoodsId().toString());
            map.put("paramMap", paramsMap);

            // 创建消息实体
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageCode(MessageCodeEnum.STORAGE_WARNING.value());
            messageDTO.setSendName("系统");
            messageDTO.setParamJson(JSON.toJSONString(map));
            messageDTO.setUniqueMark(IdUtil.fastSimpleUUID());
            sysMessageService.save(messageDTO);
            rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_SEND_MESSAGE_QUEUE, JSON.toJSONString(messageDTO));
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("消息处理完毕，手动执行ACK!");

    }


}
