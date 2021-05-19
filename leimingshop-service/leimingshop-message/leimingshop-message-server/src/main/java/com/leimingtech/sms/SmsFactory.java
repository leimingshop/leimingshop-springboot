/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.sms;

import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.message.enums.PlatformEnum;
import com.leimingtech.message.sms.SmsConfig;
import com.leimingtech.message.utils.ModuleConstant;
import com.leimingtech.remote.ParamsRemoteService;

/**
 * 短信Factory
 */
public class SmsFactory {

    private static ParamsRemoteService paramsRemoteService;

    static {
        SmsFactory.paramsRemoteService = SpringContextUtils.getBean(ParamsRemoteService.class);
    }

    private SmsFactory() {
    }

    public static AbstractSmsService build() {
        //获取短信配置信息
        SmsConfig config = paramsRemoteService.getValueObject(ModuleConstant.SMS_CONFIG_KEY, SmsConfig.class);

        if (config.getPlatform() == PlatformEnum.ALIYUN.value()) {
            return new AliyunSmsService(config);
        } else if (config.getPlatform() == PlatformEnum.QCLOUD.value()) {
            return new QcloudSmsService(config);
        }

        return null;
    }
}