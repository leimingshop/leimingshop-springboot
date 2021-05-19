/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wechat;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;


public interface WechatLoginService {

    /**
     * 微信登录
     *
     * @param code
     * @return
     */

    JSONObject wechatLogin(String code);

    /**
     * 微信登录
     *
     * @param code
     * @return
     */

    JSONObject wechatAppLogin(String code);

    /**
     * 获取用户详情
     *
     * @param accessToken
     * @param openId
     * @return
     */

    JSONObject getUserMessage(@RequestParam("accessToken") String accessToken, @RequestParam("openId") String openId);
}
