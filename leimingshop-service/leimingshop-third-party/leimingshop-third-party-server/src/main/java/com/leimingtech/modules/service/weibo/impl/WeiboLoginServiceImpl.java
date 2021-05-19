/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.weibo.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.leimingtech.config.weibo.WeiboConfig;
import com.leimingtech.modules.service.weibo.WeiboLoginService;
import com.leimingtech.util.common.HttpsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;

/**
 * <新浪微博登陆>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/26
 */
@Slf4j
@Service
public class WeiboLoginServiceImpl implements WeiboLoginService {

    /**
     * 获得新浪登陆授权信息url
     */
    private static final String ACCESS_TOKEN_HTTPS = "https://api.weibo.com/oauth2/access_token?" +
            "client_id=%s" +
            "&client_secret=%s" +
            "&code=%s" +
            "&redirect_uri=%s" +
            "&grant_type=authorization_code";
    /**
     * 获得新浪登陆用户信息
     */
    private static final String USER_MESSAGE_HTTPS = "https://api.weibo.com/2/users/show.json?" +
            "access_token=%s" +
            "&uid=%s";
    @Autowired
    WeiboConfig weiboConfig;

    /**
     * 功能描述:
     * 〈新浪微博登陆获得用户授权信息〉
     *
     * @param code 新浪code
     * @author : 刘远杰
     */
    @Override

    public JSONObject getAccessToken(String code) {
        log.info("获取code为[{}]新浪用户授权信息", code);
        String clientId = weiboConfig.APP_KEY;
        String clientSecret = weiboConfig.APP_SECRET;
        String redirectUri = weiboConfig.REDIRECT_URI;
        String url = String.format(ACCESS_TOKEN_HTTPS, clientId, clientSecret, code, redirectUri);
        String post = HttpUtil.post(url, new HashMap<>());
        log.info("获取新浪授权信息：{}", url);
        JSONObject jsonObject = JSONObject.parseObject(post);
        log.info("获得code：{}新浪用户授权信息为：{}", code, jsonObject);
        return jsonObject;
    }

    /**
     * 功能描述:
     * 〈获得新浪用户信息〉
     *
     * @param accessToken 授权token
     * @param uid         新浪uid
     * @author : 刘远杰
     */

    @Override
    public JSONObject getUserInfo(@RequestParam("accessToken") String accessToken, @RequestParam("uid") String uid) {

        String url = String.format(USER_MESSAGE_HTTPS, accessToken, uid);
        log.info("获取新浪用户信息：{}", url);
        JSONObject jsonObject = HttpsUtil.httpGet(url);
        log.info("获取uid为[{}]新浪用户授权信息为：{}", uid, jsonObject);
        return jsonObject;

    }

}
