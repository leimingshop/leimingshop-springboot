/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.sms;

import com.leimingtech.message.sms.SmsConfig;

import java.util.LinkedHashMap;

/**
 * 短信
 */
public abstract class AbstractSmsService {
    /**
     * 短信配置信息
     */
    SmsConfig config;

    /**
     * 发送短信
     *
     * @param mobile 手机号
     * @param params 参数
     */
    public abstract void sendSms(String mobile, LinkedHashMap<String, String> params);

    /**
     * 根据短信模板发送短信
     *
     * @param mobile 手机号
     * @param params 参数
     * @param code   模板参数
     */
    public abstract void sendSms(String mobile, LinkedHashMap<String, String> params, String code);

    /**
     * 发送短信
     *
     * @param mobile   手机号
     * @param params   参数
     * @param signName 短信签名
     * @param template 短信模板
     */
    public abstract void sendSms(String mobile, LinkedHashMap<String, String> params, String signName, String template);

    /**
     * 批量发送短信
     *
     * @param mobileJson   手机号
     * @param paramsJson   参数
     * @param signNameJson 短信签名
     * @param template     短信模板
     */
    public abstract void sendBatchSms(String mobileJson, String paramsJson, String signNameJson, String template);
}
