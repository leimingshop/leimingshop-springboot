/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.consumer.sys;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.dto.message.MessageReceiverDTO;
import com.leimingtech.entity.message.MessageTextEntity;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.dto.sendDTO.AppUnicastDTO;
import com.leimingtech.modules.dto.store.StoreDTO;
import com.leimingtech.modules.enums.member.MemberEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.store.StoreService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.service.message.MessageReceiverService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 站内信队列接收者
 * @Author DY
 * @Date 2019/5/22 10:58
 * @Version 1.0
 **/
@Slf4j
@Component
public class MqMessageConsumer {
    @Autowired
    private MessageReceiverService messageReceiverService;
    @Autowired
    private MemberService memberService;
    @Autowired
    private StoreService storeService;


    @RabbitHandler
    @RabbitListener(queues = MqConstant.LEIMINGTECH_MESSAGE_QUEUE)
    public void receiverMessage(Channel channel, Message message) throws IOException {
        String msgText = new String(message.getBody());
        log.info("接收到站内信消息，内容为：{}", msgText);

        //解析消息
        Map<String, Object> map = JSONObject.parseObject(msgText);
        if (BeanUtil.isEmpty(map)) {
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            return;
        }

        List<MemberDTO> memberList = null;
        List<StoreDTO> storeList = null;
        if (map.get("memberList") != null) {
            String memberListStr = map.get("memberList").toString();
            if (StringUtils.isNotBlank(memberListStr)) {
                memberList = JSONArray.parseArray(memberListStr, MemberDTO.class);
            }
        }
        if (map.get("storeList") != null) {
            String storeListStr = map.get("storeList").toString();
            if (StringUtils.isNotBlank(storeListStr)) {
                storeList = JSONArray.parseArray(storeListStr, StoreDTO.class);
            }
        }
        if (CollectionUtils.isEmpty(storeList) && CollectionUtils.isEmpty(memberList)) {
            Integer receiverType = Integer.parseInt(map.get("receiverType").toString());
            if (MessageEnum.RECEIVER_TYPE_ALL.value() == receiverType) {
                // TODO lixiang 一次性查询出所有用户信息   百万级用户直接down机   之后可以考虑取消发送全部用户，修改为发送公告
                //全部会员信息
                memberList = memberService.selectAllMember(null);
                //全部店铺信息
                storeList = storeService.list(new HashMap<>());
            } else if (MessageEnum.RECEIVER_TYPE_VIP.value() == receiverType) {
                Integer roleType = MemberEnum.MEMBER_ROLE_MEMBER.value();
                memberList = memberService.selectAllMember(roleType);
            } else {
                storeList = storeService.list(new HashMap<>());
            }
        }
        //获取发送人Id，名称
        Long userId = null;
        String userName = null;
        if (map.get("userId") != null) {
            userId = Long.valueOf(map.get("userId").toString());
        }
        if (map.get("userName") != null) {
            userName = map.get("userName").toString();
        }

        MessageTextEntity messageTextEntity = JSON.parseObject(map.get("messageTextEntity").toString(), MessageTextEntity.class);
        //构造站内信接收人信息
        List<MessageReceiverDTO> messageReceiverDTOList = new ArrayList<>();
        // TODO: 2020/5/4/004 注释掉友盟消息推送
        if (CollectionUtils.isNotEmpty(memberList)) {
            for (MemberDTO memberDTO : memberList) {
                MessageReceiverDTO messagerece = new MessageReceiverDTO();
                messagerece.setMessageId(messageTextEntity.getId());
                messagerece.setSendId(userId);
                messagerece.setSendName(userName);
                messagerece.setReceiveId(memberDTO.getId());
                messagerece.setReceiveName(memberDTO.getNickName());
                messagerece.setStatus(MessageEnum.MESSAGE_IS_UNREAD.value());
                messageReceiverDTOList.add(messagerece);
            }
        }
        if (CollectionUtils.isNotEmpty(storeList)) {
            for (StoreDTO storeDTO : storeList) {
                MessageReceiverDTO messagerece = new MessageReceiverDTO();
                messagerece.setMessageId(messageTextEntity.getId());
                messagerece.setSendId(userId);
                messagerece.setSendName(userName);
                messagerece.setReceiveId(storeDTO.getId());
                messagerece.setReceiveName(storeDTO.getStoreName());
                messagerece.setStatus(MessageEnum.MESSAGE_IS_UNREAD.value());
                messageReceiverDTOList.add(messagerece);
            }
        }
        if (CollectionUtils.isNotEmpty(messageReceiverDTOList)) {
            messageReceiverService.saveBatch(messageReceiverDTOList);
        }
        // ACK手动确认处理消息
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);

        log.info("站内信消息处理完毕，手动执行ACK!");

    }

    /**
     * 构造友盟推送信息
     *
     * @param messageTextEntity
     * @param memberDTO
     * @author xuzhch
     * @date 2020年4月28日18:30:16
     */
    private AppUnicastDTO umengMessageGenerate(MessageTextEntity messageTextEntity, MemberDTO memberDTO) {
        AppUnicastDTO appUnicastDTO = new AppUnicastDTO();
        Map<String, String> params = new HashMap<>();
        params.put("messageId", String.valueOf(messageTextEntity.getId()));
        appUnicastDTO.setTitle(messageTextEntity.getMessageTitle());
        appUnicastDTO.setText(messageTextEntity.getMessageContent());
        appUnicastDTO.setUserName(memberDTO.getMemberName());
        appUnicastDTO.setDeviceToken(memberDTO.getDeviceToken());
        appUnicastDTO.setUmengSource(memberDTO.getUmengSource());
        appUnicastDTO.setParams(params);
        appUnicastDTO.setMessageType(String.valueOf(messageTextEntity.getMessageType()));
        appUnicastDTO.setMessageId(String.valueOf(messageTextEntity.getId()));
        return appUnicastDTO;
    }
}
