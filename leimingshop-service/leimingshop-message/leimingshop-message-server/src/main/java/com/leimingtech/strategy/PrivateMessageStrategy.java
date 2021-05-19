/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.leimingtech.dto.message.MessageTextDTO;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.message.constant.MessageConstant;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.modules.dto.member.MessageMemberReceiverDTO;
import com.leimingtech.modules.service.NosqlService;
import com.leimingtech.service.message.MessageTemplateService;
import com.leimingtech.service.message.MessageTextService;
import com.leimingtech.strategy.dto.PrivateMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

/**
 * 站内信策略（消息只发送站内信）
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 10:41
 **/
@Component("privateMessageStrategy")
public class PrivateMessageStrategy extends BaseMessageStrategy {

    @Resource
    private MessageTemplateService messageTemplateService;

    @Resource
    private MessageTextService messageTextService;

    @Resource
    private NosqlService nosqlService;
    @Autowired
    private SysMessageService sysMessageService;

    /**
     * 检查该类型是否支持策略
     *
     * @param code 消息类型
     * @return 结果
     * @date 2020/4/22 16:30
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean isSupport(String code) {
        return MessageCodeEnum.MESSAGE_TYPE_PRVT.value().equals(code) || MessageCodeEnum.MESSAGE_TYPE_SYSINFO.value().equals(code);
    }

    @Override
    public void handle(MessageDTO baseMessageDTO) {
        String sendResult = sysMessageService.getSendResult(baseMessageDTO.getUniqueMark());
        if (MessageConstant.SEND_RESULT_SUCCESS.equals(sendResult)) {
            return;
        }
        PrivateMessageDTO privateMessageDTO = JSONObject.parseObject(baseMessageDTO.getParamJson(), PrivateMessageDTO.class);

        Map<Long, String> memberList = privateMessageDTO.getMemberMap();

        Optional.ofNullable(messageTemplateService.getMessageByCode(baseMessageDTO.getMessageCode())).ifPresent(messageTemplateDTO -> {
            // 判断是否需要发送站内信
            if (messageTemplateDTO.getIsSendInner() != null && messageTemplateDTO.getIsSendInner() == MessageEnum.SEND_MESSAGE.value()) {
                sysMessageService.sendSuccess(baseMessageDTO.getUniqueMark());
                MessageTextDTO messageTextDTO = new MessageTextDTO();
                messageTextDTO.setMessageTitle(messageTemplateDTO.getTempTitle());
                messageTextDTO.setMessageContent(messageTemplateDTO.getTempInnerContent());
                messageTextDTO.setMessageType(MessageEnum.MESSAGE_TYPE_PRVT.value());
                messageTextDTO.setReceiverType(MessageEnum.RECEIVER_TYPE_VIP.value());
                messageTextDTO.setSendMode(new int[]{0});
                messageTextDTO.setParamsMap(privateMessageDTO.getParamMap());
                messageTextDTO.setUserName(baseMessageDTO.getSendName());

                ArrayList<MessageMemberReceiverDTO> list = Lists.newArrayList();
                memberList.keySet().forEach(key -> {
                    MessageMemberReceiverDTO memberReceiverDTO = new MessageMemberReceiverDTO();
                    memberReceiverDTO.setId(key);
                    memberReceiverDTO.setNickName(memberList.get(key));
                    list.add(memberReceiverDTO);
                });

                messageTextDTO.setMemberList(list);

                // todo lx 保存站内信（待优化）
                messageTextService.saveMessage(messageTextDTO);

            }
        });
    }
}
