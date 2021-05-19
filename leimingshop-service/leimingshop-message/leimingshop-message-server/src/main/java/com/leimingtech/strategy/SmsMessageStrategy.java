/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.strategy;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.enums.message.MessageEnum;
import com.leimingtech.message.constant.MessageConstant;
import com.leimingtech.message.dto.MessageDTO;
import com.leimingtech.message.enums.MessageCodeEnum;
import com.leimingtech.message.service.SysMessageService;
import com.leimingtech.message.service.SysSmsService;
import com.leimingtech.service.message.MessageTemplateService;
import com.leimingtech.strategy.dto.SmsMessageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * 短信策略（只发送消息）
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/22 10:41
 **/
@Component("smsMessageStrategy")
public class SmsMessageStrategy extends BaseMessageStrategy {

    @Resource
    private MessageTemplateService messageTemplateService;

    @Resource
    private SysSmsService sysSmsService;

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
        return MessageCodeEnum.TEMPLATECODE_LOGIN_REGISTER.value().equals(code) || MessageCodeEnum.TEMPLATECODE_RESET_PWD_CODE.value().equals(code);
    }

    @Override
    public void handle(MessageDTO baseMessageDTO) {
        String sendResult = sysMessageService.getSendResult(baseMessageDTO.getUniqueMark());
        if (MessageConstant.SEND_RESULT_SUCCESS.equals(sendResult)) {
            return;
        }
        SmsMessageDTO smsMessageDTO = JSONObject.parseObject(baseMessageDTO.getParamJson(), SmsMessageDTO.class);

        Optional.ofNullable(messageTemplateService.getMessageByCode(baseMessageDTO.getMessageCode())).ifPresent(messageTemplateDTO -> {
            // 判断是否需要发送短信
            if (messageTemplateDTO.getIsSendSms() != null && messageTemplateDTO.getIsSendSms() == MessageEnum.SEND_MESSAGE.value()) {
                sysMessageService.sendSuccess(baseMessageDTO.getUniqueMark());
                sysSmsService.send(smsMessageDTO.getMobile(), smsMessageDTO.getParamJson(),
                        messageTemplateDTO.getTempSmsCode());
            }
        });
    }
}
