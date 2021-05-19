/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.umeng;

import com.alibaba.fastjson.JSON;
import com.leimingtech.modules.dto.sendDTO.AppUnicastDTO;
import com.leimingtech.util.push.UmengPush;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Slf4j
@Service

public class UmengPushServiceImpl implements UmengPushService {

    @Override
    public Boolean sendIOSSingle(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception {

        return UmengPush.sendIOSCustomizedcast(appUnicastDTO);
    }


    @Override
    public Boolean sendIOSBroad(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception {
        return UmengPush.sendIOSBroadcast(appUnicastDTO);
    }


    @Override
    public Boolean sendAndroidSingle(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception {
        return UmengPush.sendAndroidUnicast(appUnicastDTO);
    }


    @Override
    public Boolean sendAndroidBroad(@RequestBody AppUnicastDTO appUnicastDTO) throws Exception {
        return UmengPush.sendAndroidBroadcast(appUnicastDTO);
    }


    @Override
    public void sendSingle(@RequestBody List<AppUnicastDTO> appUnicastDTOS) {
        for (AppUnicastDTO appUnicastDTO : appUnicastDTOS) {
            try {
                if (appUnicastDTO.getUmengSource() == 1) {
                    UmengPush.sendAndroidUnicast(appUnicastDTO);
                } else {
                    UmengPush.sendIOSCustomizedcast(appUnicastDTO);
                }
            } catch (Exception e) {
                log.error("友盟消息推送失败，消息内容：{},异常信息{}", JSON.toJSONString(appUnicastDTO), e.getMessage());
            }

        }
    }
}
