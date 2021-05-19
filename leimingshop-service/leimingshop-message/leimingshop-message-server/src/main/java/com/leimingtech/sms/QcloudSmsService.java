/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.sms;

import cn.hutool.core.map.MapUtil;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.CustomException;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.message.enums.PlatformEnum;
import com.leimingtech.message.exception.ModuleErrorCode;
import com.leimingtech.message.sms.SmsConfig;
import com.leimingtech.service.impl.SysSmsServiceImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * 腾讯云短信服务
 */
public class QcloudSmsService extends AbstractSmsService {
    public QcloudSmsService(SmsConfig config) {
        this.config = config;
    }

    @Override
    public void sendSms(String mobile, LinkedHashMap<String, String> params) {
        this.sendSms(mobile, params, config.getQcloudSignName(), config.getQcloudTemplateId());
    }

    @Override
    public void sendSms(String mobile, LinkedHashMap<String, String> params, String code) {
        this.sendSms(mobile, params, config.getQcloudSignName(), config.getQcloudTemplateId());
    }

    @Override
    public void sendSms(String mobile, LinkedHashMap<String, String> params, String signName, String template) {
        SmsSingleSender sender = new SmsSingleSender(config.getQcloudAppId(), config.getQcloudAppKey());

        //短信参数
        ArrayList<String> paramsList = new ArrayList<>();
        if (MapUtil.isNotEmpty(params)) {
            for (String value : params.values()) {
                paramsList.add(value);
            }
        }
        SmsSingleSenderResult result;
        try {
            result = sender.sendWithParam("86", mobile, Integer.parseInt(template), paramsList, signName, null, null);
        } catch (Exception e) {
            throw new CustomException(ModuleErrorCode.SEND_SMS_ERROR, e, "");
        }

        int status = Constant.SUCCESS;
        if (result.result != 0) {
            status = Constant.FAIL;
        }

        //保存短信记录
        SysSmsServiceImpl sysSmsService = SpringContextUtils.getBean(SysSmsServiceImpl.class);
        sysSmsService.save(PlatformEnum.QCLOUD.value(), mobile, params, status);

        if (status == Constant.FAIL) {
            throw new CustomException(ModuleErrorCode.SEND_SMS_ERROR, result.errMsg);
        }
    }

    @Override
    public void sendBatchSms(String mobileJson, String paramsJson, String signNameJson, String template) {
        new SmsSingleSender(config.getQcloudAppId(), config.getQcloudAppKey());

        //短信参数
        /*ArrayList<String> paramsList = new ArrayList<>();
        if (MapUtil.isNotEmpty(paramsJson)) {
            for (String value : paramsJson.values()) {
                paramsList.add(value);
            }
        }*/
        //SmsSingleSenderResult result;
       /* try {
            result = sender.sendWithParam("86", mobileJson, Integer.parseInt(template), paramsJson, signNameJson, null, null);
        } catch (Exception e) {
            throw new CustomException(ModuleErrorCode.SEND_SMS_ERROR, e, "");
        }*/

        /*int status = Constant.SUCCESS;
        if (result.result != 0) {
            status = Constant.FAIL;
        }*/

       /* //保存短信记录
        SysSmsService sysSmsService = SpringContextUtils.getBean(SysSmsService.class);
        sysSmsService.save(PlatformEnum.QCLOUD.value(), mobileJson, paramsJson, status);

        if (status == Constant.FAIL) {
            throw new CustomException(ModuleErrorCode.SEND_SMS_ERROR, result.errMsg);
        }*/
    }
}
