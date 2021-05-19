/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wechat;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.IdGenerator;
import com.leimingtech.config.wechat.WXPayConfig;
import com.leimingtech.util.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * 微信签名Service
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/3/17 16:54
 **/
@Slf4j
@Service

public class WxSignatureServiceImpl implements WxSignatureService {

    @Autowired
    private WXPayConfig wxPayConfig;

    @Autowired
    private RedisUtils redisUtils;

    /**
     * 获取微信签名
     *
     * @param url: 获取签名地址的URL
     * @return 微信签名实体
     * @date 2020/3/17 17:00
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public JSONObject getWxSinature(String url) {

        log.info("原始URL: " + url);
        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);

        // 随机串
        String nonceStr = String.valueOf(IdGenerator.defaultSnowflakeId());

        // 只获取域名前面的地址
//        String[] urls = url.split("#");
//        String newUrl = urls[0];
        log.info("随机串：{} 获取签名URL:{}", nonceStr, url);

        // 拼接获取签名的信息 中间用&符号隔开
        String[] signArr = new String[]{"url=" + url,
                "jsapi_ticket=" + getWxTicket(),
                "noncestr=" + nonceStr,
                "timestamp=" + timestamp};
        Arrays.sort(signArr);
        String signStr = StringUtils.join(signArr, "&");
        log.info("signStr:{}", signStr);
        // 使用SHA进行加密（微信要求）
        String resSign = DigestUtils.sha1Hex(signStr);

        log.info("加密后的签名信息：{}", resSign);
        JSONObject respJson = new JSONObject();
        respJson.put("timestamp", timestamp);
        respJson.put("nonceStr", nonceStr);
        respJson.put("signature", resSign);
        log.info("返回的时间戳:[{}]生成签名的随机串:[{}]签名:[{}]", timestamp, nonceStr, resSign);
        return respJson;
    }


    /**
     * 获取微信的票据
     *
     * @return 微信票据ticket
     * @date 2020/3/17 17:36
     * @author lixiangx@leimingtech.com
     **/
    private String getWxTicket() {
        // 从缓存中获取票据信息
        String ticketKey = RedisKeys.getWechatTicketKey() + wxPayConfig.getAPP_ID();
        Object ticket = redisUtils.get(ticketKey);
        if (ticket == null) {
            // 拼接获取ticket url
            String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + getWxAccessToken() + "&type=jsapi";
            JSONObject jsonObject = HttpsUtil.httpGet(url);
            log.info("获取到ticket：{}", jsonObject);

            // 缓存微信ticket两个小时
            redisUtils.set(ticketKey, jsonObject.getString("ticket"), RedisUtils.HOUR_TWO_EXPIRE);
            return jsonObject.getString("ticket");
        }
        return (String.valueOf(ticket));
    }

    /**
     * 获取微信access_token
     *
     * @return 微信access_token
     * @date 2020/3/17 17:38
     * @author lixiangx@leimingtech.com
     **/
    private String getWxAccessToken() {
        String tokenKey = RedisKeys.getWechatTicketKey() + wxPayConfig.getAPP_ID();
        Object wechatTokenCache = redisUtils.get(tokenKey);
        if (wechatTokenCache == null) {

            // 拼接获取微信access_token URL
            String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
                    + wxPayConfig.getAPP_ID() +
                    "&secret=" + wxPayConfig.getAPP_SECRET();
            JSONObject resJson = HttpsUtil.httpGet(url);
            log.info("获取到access_token：{}", resJson);
            String accessToken = resJson.getString("access_token");
            // 缓存微信access_token两个小时
            redisUtils.set(tokenKey, accessToken, RedisUtils.HOUR_TWO_EXPIRE);
            return accessToken;
        }
        return String.valueOf(wechatTokenCache);
    }
}
