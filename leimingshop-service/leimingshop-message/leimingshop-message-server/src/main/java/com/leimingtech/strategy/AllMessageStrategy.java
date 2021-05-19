/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.leimingtech.dto.message.FindMessageTemplateDTO;
import com.leimingtech.dto.message.MessageTextDTO;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.message.constant.MessageConstant;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.message.service.SysSmsService;
import com.leimingtech.modules.dto.member.MemberUmengTokenInfo;
import com.leimingtech.modules.dto.member.MessageMemberReceiverDTO;
import com.leimingtech.modules.dto.sendDTO.AppUnicastDTO;
import com.leimingtech.modules.dto.store.MessageStoreReceiverDTO;
import com.leimingtech.modules.service.WechatMpService;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.modules.service.umeng.UmengPushService;
import com.leimingtech.service.message.MessageTemplateService;
import com.leimingtech.service.message.MessageTextService;
import com.leimingtech.strategy.dto.AllMessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * 全部消息推送策略（所有消息发送）
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 10:41
 **/
@Component("allMessageStrategy")
@Slf4j
public class AllMessageStrategy extends BaseMessageStrategy {

    @Resource
    private MessageTemplateService messageTemplateService;

    @Resource
    private WechatMpService wechatMpService;

    @Resource
    private SysSmsService sysSmsService;

    @Resource
    private MessageTextService messageTextService;

    @Resource
    private UmengPushService umengPushService;

    @Resource
    private MemberService memberService;

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
        return !MessageCodeEnum.MESSAGE_TYPE_PRVT.value().equals(code) && !MessageCodeEnum.MESSAGE_TYPE_SYSINFO.value().equals(code)
                && !MessageCodeEnum.TEMPLATECODE_LOGIN_REGISTER.value().equals(code) && !MessageCodeEnum.TEMPLATECODE_RESET_PWD_CODE.value().equals(code);
    }

    @Override
    public void handle(MessageDTO baseMessageDTO) {
        AllMessageDTO allMessageDTO = JSONObject.parseObject(baseMessageDTO.getParamJson(), AllMessageDTO.class);
        String sendResult = sysMessageService.getSendResult(baseMessageDTO.getUniqueMark());
        if (StringUtils.isBlank(sendResult) || MessageConstant.SEND_RESULT_SUCCESS.equals(sendResult)) {
            return;
        }
        FindMessageTemplateDTO messageByCode = messageTemplateService.getMessageByCode(baseMessageDTO.getMessageCode());
        Optional.ofNullable(messageByCode).ifPresent(messageTemplateDTO -> {
            // 判断是否需要发送站内信
            if (messageTemplateDTO.getIsSendInner() != null && messageTemplateDTO.getIsSendInner() == MessageEnum.SEND_MESSAGE.value()) {
                sysMessageService.sendSuccess(baseMessageDTO.getUniqueMark());
                this.sendInnerMessage(allMessageDTO, baseMessageDTO, messageTemplateDTO);
            }
            // 判断是否需要发送短信
            if (messageTemplateDTO.getIsSendSms() != null && messageTemplateDTO.getIsSendSms() == MessageEnum.SEND_MESSAGE.value()) {
                this.sendSmsMessage(allMessageDTO, messageTemplateDTO.getTempSmsCode());
            }
            // 判断是否需要发送微信公众号推送
            if (null != allMessageDTO.getWechatParamsJson() && messageTemplateDTO.getIsSendWechat() != null && messageTemplateDTO.getIsSendWechat() == MessageEnum.SEND_MESSAGE.value()) {
                wechatMpService.sendTemplateMsg(allMessageDTO.getWechatParamsJson(),
                        messageTemplateDTO.getWechatTemplateId(), allMessageDTO.getWehcatClickUrl(),
                        allMessageDTO.getWechatOpenId());
            }
            // 判断是否需要发送App推送
            if (messageTemplateDTO.getIsSendUmeng() != null && messageTemplateDTO.getIsSendUmeng() == MessageEnum.SEND_MESSAGE.value()) {
                sendUmengPush(allMessageDTO, baseMessageDTO, messageTemplateDTO);
            }
        });
    }

    private void sendSmsMessage(AllMessageDTO allMessageDTO, String tempSmsCode) {
        sysSmsService.send(allMessageDTO.getMobile(), JSON.toJSONString(allMessageDTO.getParamMap()), tempSmsCode);
    }

    /**
     * Umeng推送
     *
     * @param allMessageDTO      消息对象
     * @param baseMessageDTO     基础信息
     * @param messageTemplateDTO 模板信息
     * @author xuzhch
     * @date 2020年4月24日09:29:53
     */
    private void sendUmengPush(AllMessageDTO allMessageDTO, MessageDTO baseMessageDTO,
                               FindMessageTemplateDTO messageTemplateDTO) {
        Set<Long> members = allMessageDTO.getMemberMap().keySet();
        List<Long> memberIds = new ArrayList<>(members);
        List<MemberUmengTokenInfo> tokenInfos = memberService.selectUmengTokenByIds(memberIds);
        Map<String, String> paramMap = allMessageDTO.getParamMap();
        List<AppUnicastDTO> appUnicastDtos = new ArrayList<>();
        for (MemberUmengTokenInfo tokenInfo : tokenInfos) {
            AppUnicastDTO appUnicastDTO = new AppUnicastDTO();
            appUnicastDTO.setDeviceToken(tokenInfo.getDeviceToken());
            appUnicastDTO.setUmengSource(tokenInfo.getUmengSource());
            appUnicastDTO.setUserName(tokenInfo.getMemberName());
            appUnicastDTO.setMessageType(baseMessageDTO.getMessageCode());
            appUnicastDTO.setTitle(messageTemplateDTO.getUmengTitle());
            appUnicastDTO.setText(replaceParams(messageTemplateDTO.getTempUmengContent(), paramMap));
            appUnicastDTO.setParams(paramMap);
            appUnicastDtos.add(appUnicastDTO);
        }
        umengPushService.sendSingle(appUnicastDtos);
    }

    /**
     * @param text:      待替换文本
     * @param paramsMap: 替换数据的Map
     * @return 替换后的数据
     * @date 2020/4/9 17:53
     * @author lixiangx@leimingtech.com
     **/
    private String replaceParams(String text, Map<String, String> paramsMap) {
        if (paramsMap.get(MessageConstant.MAP_PARAMS_SELLER_NAME) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_SELLER_NAME,
                    paramsMap.get(MessageConstant.MAP_PARAMS_SELLER_NAME));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_ORDER_SN) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_ORDER_SN,
                    paramsMap.get(MessageConstant.MAP_PARAMS_ORDER_SN));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_USERNAME) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_USERNAME,
                    paramsMap.get(MessageConstant.MAP_PARAMS_USERNAME));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_GOODS_NAME) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_GOODS_NAME,
                    paramsMap.get(MessageConstant.MAP_PARAMS_GOODS_NAME));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_NUMBER) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_NUMBER,
                    paramsMap.get(MessageConstant.MAP_PARAMS_NUMBER));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_CODE) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_CODE, paramsMap.get(MessageConstant.MAP_PARAMS_CODE));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_CAUSE) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_CAUSE,
                    paramsMap.get(MessageConstant.MAP_PARAMS_CAUSE));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_GOODS_ID) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_GOODS_ID,
                    paramsMap.get(MessageConstant.MAP_PARAMS_GOODS_ID));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_STORE_NAME) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_STORE_NAME,
                    paramsMap.get(MessageConstant.MAP_PARAMS_STORE_NAME));
        }
        if (paramsMap.get(MessageConstant.MAP_PARAMS_SHORT_URL) != null) {
            text = text.replace(MessageConstant.MESSAGE_TEMPLATE_SHORT_URL,
                    paramsMap.get(MessageConstant.MAP_PARAMS_SHORT_URL));
        }
        return text;
    }

    /**
     * 发送站内信
     *
     * @param allMessageDTO:      消息参数实体
     * @param messageTemplateDTO: 消息模板
     * @date 2020/4/9 18:16
     * @author lixiangx@leimingtech.com
     **/
    private void sendInnerMessage(AllMessageDTO allMessageDTO, MessageDTO baseMessageDTO,
                                  FindMessageTemplateDTO messageTemplateDTO) {
        Map<Long, String> memberMap = allMessageDTO.getMemberMap();
        Map<Long, String> storeMap = allMessageDTO.getStoreMap();
        MessageTextDTO messageTextDTO = new MessageTextDTO();
        messageTextDTO.setMessageTitle(messageTemplateDTO.getTempTitle());
        messageTextDTO.setMessageContent(messageTemplateDTO.getTempInnerContent());
        messageTextDTO.setMessageType(MessageEnum.MESSAGE_TYPE_PRVT.value());
        messageTextDTO.setReceiverType(MessageEnum.RECEIVER_TYPE_VIP.value());
        messageTextDTO.setSendMode(new int[]{0});
        messageTextDTO.setParamsMap(allMessageDTO.getParamMap());
        messageTextDTO.setUserName(baseMessageDTO.getSendName());

        if (!BeanUtil.isEmpty(memberMap)) {
            ArrayList<MessageMemberReceiverDTO> list = Lists.newArrayList();
            memberMap.keySet().forEach(key -> {
                MessageMemberReceiverDTO memberReceiverDTO = new MessageMemberReceiverDTO();
                memberReceiverDTO.setId(key);
                memberReceiverDTO.setNickName(memberMap.get(key));
                list.add(memberReceiverDTO);
            });
            messageTextDTO.setMemberList(list);
        }
        if (!BeanUtil.isEmpty(storeMap)) {
            List<MessageStoreReceiverDTO> list = Lists.newArrayList();
            storeMap.keySet().forEach(key -> {
                MessageStoreReceiverDTO storeReceiverDTO = new MessageStoreReceiverDTO();
                storeReceiverDTO.setId(key);
                storeReceiverDTO.setStoreName(storeMap.get(key));
                list.add(storeReceiverDTO);
            });
            messageTextDTO.setStoreList(list);
        }

        // todo lx 保存站内信（待优化）
        messageTextService.saveMessage(messageTextDTO);
    }
}
