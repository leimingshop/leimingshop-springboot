/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wechat;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.config.wechat.WechatAppConfig;
import com.leimingtech.config.wechat.WechatConfig;
import com.leimingtech.util.common.HttpsUtil;
import com.leimingtech.util.wechat.WechatInfoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName WechatServiceImpl
 * @Description 微信service
 * @Author DY
 * @Date 2019/5/25 13:40
 * @Version 1.0
 **/
@Slf4j
@Service

public class WechatLoginServiceImpl implements WechatLoginService {

    /**
     * 微信配置信息
     */
    @Autowired
    WechatConfig wechatConfig;

    /**
     * 微信 app应用配置信息
     */
    @Autowired
    WechatAppConfig wechatAppConfig;

    /**
     * 微信登录
     *
     * @param code
     * @return
     */
    @Override

    public JSONObject wechatLogin(String code) {
        log.debug("------------------收到微信code:{}------------------", code);
        //通过code换取token
        String appId = wechatConfig.getAppid();
        String secret = wechatConfig.getSecret();
        //顺序不能变化
        String url = WechatInfoUtil.getCode(appId, secret, code);
        log.debug("获取token的URL为:{}", url);
        return HttpsUtil.httpGet(url);
    }

    /**
     * app微信登录
     *
     * @param code
     * @return
     */

    @Override
    public JSONObject wechatAppLogin(String code) {
        log.debug("------------------收到app微信code:{}------------------", code);
        //通过code换取token
        String appId = wechatAppConfig.APP_ID;
        String secret = wechatAppConfig.APP_SECRET;
        //顺序不能变化
        String url = WechatInfoUtil.getCode(appId, secret, code);
        log.debug("获取token的URL为:{}", url);
        return HttpsUtil.httpGet(url);
    }

    /**
     * 拉取用户信息(需scope为 snsapi_userinfo)
     * get请求:https协议
     * <p>
     * 通过access_token和openid拉去用户信息
     * <p>
     * 返回: openid,nickname(昵称),sex,province(省份),city,country,headimgurl,privilege(特权),unionid
     *
     * @return
     */

    @Override
    public JSONObject getUserMessage(String accessToken, String openId) {
        //拉去用户信息url
        String url = WechatInfoUtil.getUserMessage(accessToken, openId);
        log.debug("拉取微信用户信息url:{}", url);
        //通过https获得响应
        return HttpsUtil.httpGet(url);
    }

}
