//package com.leimingtech.consumer;
//
//import com.alibaba.fastjson.JSONArray;
//import com.alibaba.fastjson.JSONObject;
//import com.leimingtech.commons.tools.utils.ConvertUtils;
//import com.leimingtech.dto.message.FindMessageTemplateDTO;
//import com.leimingtech.dto.message.MessageTextDTO;
//import com.leimingtech.enums.message.MessageEnum;
//import com.leimingtech.enums.message.MessageTemlateEnum;
//import com.leimingtech.modules.constant.ElasticSearchConstant;
//import com.leimingtech.modules.dto.PageModelDTO;
//import com.leimingtech.modules.dto.cart.CartDTO;
//import com.leimingtech.modules.dto.member.MemberDTO;
//import com.leimingtech.modules.dto.member.MessageMemberReceiverDTO;
//import com.leimingtech.modules.service.member.MemberService;
//import com.leimingtech.modules.utils.EsDataUtils;
//import com.leimingtech.mq.constant.MqConstant;
//import com.leimingtech.service.message.MessageTextService;
//import com.rabbitmq.client.Channel;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.collections4.CollectionUtils;
//import org.springframework.amqp.core.Message;
//import org.springframework.amqp.rabbit.annotation.RabbitHandler;
//import org.springframework.amqp.rabbit.annotation.RabbitListener;
//import org.springframework.beans.factory.annotation.Autowired;

//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 库存不足通知（通知H5用户）
// *
// * @author chengqian
// * @version V1.0
// * @date2020-2-13 09：10
// **/
//@Slf4j
//@Component
//public class MqStorageRemingConsumer {
//
//    @Autowired
//    private MessageTextService messageTextService;
//
//    @Autowired
//    private MemberService memberService;
//
//    @Autowired
//    private EsDataUtils esDataUtils;
//
//
//    /**
//     * 购物车商品库存不足
//     *
//     * @param channel
//     * @param message
//     * @throws IOException
//     */
//    // TODO lixiang 暂时注释发送处  待优化
//    @RabbitHandler
//    @RabbitListener(queues = MqConstant.LEIMINGTECH_STORAGE_REMIND_QUEUE)
//    public void cartGoodsReduceRemind(Channel channel, Message message) throws IOException {
//        String msgText = new String(message.getBody());
//        log.info("库存不足通知消息接收，内容为:{}", msgText);
//
//        Long specId = JSONArray.parseObject(msgText, Long.class);
//
//        // 获取短信开关
//        FindMessageTemplateDTO findMessageTemplateDTO = messageTextService.getMessageNo(MessageEnum.SEND_MODE_SMS_INNER.value(), MessageTemlateEnum.CART_STORAGE_REMIND.value());
//        PageModelDTO pageModelDTO = new PageModelDTO();
//        pageModelDTO.setIsPage(false);
//        Map<String, Object> equalsSearchCondition = pageModelDTO.getEqualsSearchCondition();
//        equalsSearchCondition.put("specId", specId);
//        pageModelDTO.setCollapseField("memberId");
//        String[] strings = new String[2];
//        strings[0] = "memberId";
//        strings[1] = "specName";
//        pageModelDTO.setFetchSourceIncludes(strings);
//
//        esDataUtils.queryData(pageModelDTO, ElasticSearchConstant.CART_INDEX);
//        List<String> jsonRsList = pageModelDTO.getJsonRsList();
//        if (CollectionUtils.isEmpty(jsonRsList)) {
//            return;
//        }
//        List<CartDTO> collect = jsonRsList.stream().map(p -> JSONObject.parseObject(p, CartDTO.class)).collect(Collectors.toList());
//        List<Long> memberList = collect.stream().map(c -> c.getMemberId()).collect(Collectors.toList());
//        // 查询出用户信息
//        List<MemberDTO> memberDTOList = memberService.selectPhoneListById(memberList);
//        if (CollectionUtils.isEmpty(memberDTOList)) {
//            log.info("用户信息为空");
//            return;
//        }
//        if (findMessageTemplateDTO != null && findMessageTemplateDTO.getIsSendInner() == 0) {
//            LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap<>();
//            linkedHashMap.put("goodsName", collect.get(0).getSpecName());
//            MessageTextDTO messageTextDTO = new MessageTextDTO();
//            messageTextDTO.setMessageTitle(findMessageTemplateDTO.getTempTitle());
//            messageTextDTO.setParamsMap(linkedHashMap);
//            messageTextDTO.setMessageContent(findMessageTemplateDTO.getTempInnerContent());
//            messageTextDTO.setMessageType(MessageEnum.MESSAGE_TYPE_PRVT.value());
//            messageTextDTO.setReceiverType(MessageEnum.RECEIVER_TYPE_VIP.value());
//            messageTextDTO.setSendMode(new int[]{1});
//            messageTextDTO.setUserName("运营平台");
//            messageTextDTO.setCreator("运营平台");
//            List<MessageMemberReceiverDTO> memberReceiverDTOS = ConvertUtils.sourceToTarget(memberDTOList, MessageMemberReceiverDTO.class);
//            messageTextDTO.setMemberList(memberReceiverDTOS);
//            messageTextService.saveMessage(messageTextDTO);
//        }
//        // ACK手动确认处理消息
//        channel.basicAck(message.getMessageProperties().
//                getDeliveryTag(), false);
//        log.info("库存不足通知消息处理完毕，手动执行ACK!");
//    }
//}
