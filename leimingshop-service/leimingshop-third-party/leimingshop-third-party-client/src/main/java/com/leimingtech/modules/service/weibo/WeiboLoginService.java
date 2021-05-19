/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.weibo;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/26
 */

public interface WeiboLoginService {

    /**
     * 功能描述:
     * 〈新浪微博登陆获得用户授权信息〉
     *
     * @param code 新浪code
     * @author : 刘远杰
     */

    JSONObject getAccessToken(String code);

    /**
     * 功能描述:
     * 〈获得新浪用户信息〉
     *
     * @param accessToken 授权token
     * @param uid         新浪uid
     * @author : 刘远杰
     */

    JSONObject getUserInfo(@RequestParam("accessToken") String accessToken, @RequestParam("uid") String uid);

}
