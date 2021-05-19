/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.push;

import com.leimingtech.config.UmengConfig;
import com.leimingtech.modules.dto.sendDTO.AppUnicastDTO;
import com.leimingtech.util.push.android.AndroidBroadcast;
import com.leimingtech.util.push.android.AndroidUnicast;
import com.leimingtech.util.push.ios.IOSBroadcast;
import com.leimingtech.util.push.ios.IOSUnicast;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;


/**
 * 友盟推送工具类
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/4/20 16:57
 * @update 增加注释，方便理解
 **/
@Service
public class UmengPush {
    private static UmengConfig umengConfig;
    private static PushClient client;

    @Resource
    private PushClient pushClient;

    @Resource
    private UmengConfig myUmengConfig;

    /**
     * @return
     * @throws Exception
     * @Description IOS 单推
     * @author zhangsenhao
     * @Copyrights 2016年10月17日下午4:57:54 handongkeji All rights reserved.
     */
    public static Boolean sendIOSCustomizedcast(AppUnicastDTO appUnicastDTO) throws Exception {
        IOSUnicast iosCast = new IOSUnicast(umengConfig.IOS_APPKEY, umengConfig.IOS_APP_MASTER_SECRET);
        iosCast.setDeviceToken(appUnicastDTO.getDeviceToken());
        Map<String, String> alertJson = new HashMap<String, String>();
        alertJson.put("title", appUnicastDTO.getTitle());
        String text = "";
        if (StringUtils.isNotBlank(appUnicastDTO.getText())) {
            text = getHtmlSplit(appUnicastDTO.getText());
        }
        alertJson.put("subtitle", text);
        setIOSSendValue(appUnicastDTO, iosCast, alertJson);
        Boolean flag = client.send(iosCast);
        return flag;
    }

    /**
     * @return
     * @throws Exception
     * @Description IOS 全推
     */
    public static Boolean sendIOSBroadcast(AppUnicastDTO appUnicastDTO) throws Exception {
        IOSBroadcast iosCast = new IOSBroadcast(umengConfig.IOS_APPKEY, umengConfig.IOS_APP_MASTER_SECRET);
        iosCast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
        Map<String, String> alertJson = new HashMap<String, String>();
        alertJson.put("title", appUnicastDTO.getTitle());
        String text = "";
        if (StringUtils.isNotBlank(appUnicastDTO.getText())) {
            text = getHtmlSplit(appUnicastDTO.getText());
        }
        alertJson.put("body", text);
        setIOSSendValue(appUnicastDTO, iosCast, alertJson);
        Boolean flag = false;
        flag = client.send(iosCast);
        return flag;
    }

    /**
     * android单推
     *
     * @param androidDTO
     * @return 推送结果
     * @throws Exception
     */
    public static Boolean sendAndroidUnicast(AppUnicastDTO androidDTO) throws Exception {
        AndroidUnicast unicast = new AndroidUnicast(umengConfig.ANDROID_APPKEY, umengConfig.ANDROID_APP_MASTER_SECRET);
        // Set your device token  Ao5DQrHUGJP1itCaip-PXUA8eltjhaPFxRZKHOwoRVcH
        unicast.setDeviceToken(androidDTO.getDeviceToken());
        unicast.setProductionMode();
        String text = "";
        if (StringUtils.isNotBlank(androidDTO.getText())) {
            text = getHtmlSplit(androidDTO.getText());
        }
        setAndroidSendValue(androidDTO, unicast, text);
        Boolean flag = client.send(unicast);
        return flag;
    }

    /**
     * @return
     * @throws Exception
     * @Description 安卓全推
     * @Site http://www.handongkeji.com
     * @Copyrights 2016年10月17日下午4:58:25 handongkeji All rights reserved.
     */
    public static Boolean sendAndroidBroadcast(AppUnicastDTO androidDTO) throws Exception {
        AndroidBroadcast unicast = new AndroidBroadcast(umengConfig.ANDROID_APPKEY, umengConfig.ANDROID_APP_MASTER_SECRET);
        String text = "";
        if (StringUtils.isNotBlank(androidDTO.getText())) {
            text = getHtmlSplit(androidDTO.getText());
        }
        unicast.setPredefinedKeyValue("timestamp", Integer.toString((int) (System.currentTimeMillis() / 1000)));
        unicast.setProductionMode(true);
        setAndroidSendValue(androidDTO, unicast, text);
        Boolean flag = client.send(unicast);
        return flag;
    }

    private static void setAndroidSendValue(AppUnicastDTO appUnicastDTO, AbstractAndroidNotification unicast, String text) throws Exception {
        Map<String, String> params = appUnicastDTO.getParams();
        unicast.setText(text);
        unicast.setTicker("雷铭");
        unicast.setTitle(appUnicastDTO.getTitle());
        unicast.goCustomAfterOpen("LEIMING");
        unicast.setDisplayType(AbstractAndroidNotification.DisplayType.NOTIFICATION);
        unicast.setExtraField("messageType", appUnicastDTO.getMessageType());
        if (null != params) {
            unicast.setExtraField("messageId", params.get("messageId"));
            unicast.setExtraField("orderSn", params.get("orderSn"));
            unicast.setExtraField("aftersaleSn", params.get("aftersaleSn"));
            unicast.setExtraField("goodsId", params.get("goodsId"));
            unicast.setExtraField("specId", params.get("specId"));
            unicast.setExtraField("storeId", params.get("storeId"));
            unicast.setExtraField("brandId", params.get("brandId"));
            unicast.setExtraField("refundType", params.get("refundType"));
        }
    }

    private static void setIOSSendValue(AppUnicastDTO appUnicastDTO, IOSNotification iosCast, Map<String, String> alertJson) throws Exception {
        Map<String, String> params = appUnicastDTO.getParams();
        iosCast.setPredefinedKeyValue("alert", alertJson);
        iosCast.setBadge(0);
        iosCast.setSound("default");
        iosCast.setProductionMode();
        iosCast.setCustomizedField("messageType", appUnicastDTO.getMessageType());
        if (null != params) {
            iosCast.setCustomizedField("orderId", params.get("orderId"));
            iosCast.setCustomizedField("aftersaleSn", params.get("aftersaleSn"));
            iosCast.setCustomizedField("goodsId", params.get("goodsId"));
            iosCast.setCustomizedField("specId", params.get("specId"));
            iosCast.setCustomizedField("storeId", params.get("storeId"));
            iosCast.setCustomizedField("brandId", params.get("brandId"));
            iosCast.setCustomizedField("messageId", params.get("messageId"));
            iosCast.setCustomizedField("refundType", params.get("refundType"));
        }
    }

    public static String getHtmlSplit(String html) {
        html = html.replaceAll("<(S*?)[^>]*>.*?|<.*? />", "");
        html = html.replaceAll("&nbsp", "");
        html = html.replaceAll("&lt;", "<");
        html = html.replaceAll("&gt;", ">");
        return html;
    }

    @PostConstruct
    public void init() {
        client = pushClient;
        umengConfig = myUmengConfig;
    }
}
