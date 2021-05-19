/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.wechat;


import com.leimingtech.modules.dto.wechat.WechatAccessTokenDTO;
import com.leimingtech.modules.dto.wechat.WechatAppLoginDTO;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

/**
 * 微信接口调用服务
 *
 * @author xuzhch
 * @date 2020年9月18日
 */

public interface WechatService {

    /**
     * 微信登录
     *
     * @param code 微信code
     * @param type 登录类型：如果登录类型是微信小程序登录的话，调用小程序的获取openid的接口；h5或者公众号则调其他的；
     * @return 登录结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> wechatLogin(@RequestParam("code") String code, @RequestParam("type") String type);

    /**
     * 获取微信openId和unionId
     *
     * @param code : 微信code
     * @param type : 操作源类型：miniwx：微信小程序 其他为H5或公众号
     * @return 微信openId和unionId wechat open id andunion id
     * @author lixiangx @leimingtech.com
     * @date 2020 /1/15 15:16
     */

    WechatAccessTokenDTO getWechatOpenIdAndunionId(@RequestParam(value = "code") String code,
                                                   @RequestParam("type") String type);

    /**
     * 获取openid
     *
     * @param code 微信code
     * @return OpenID
     * @author xuzhch
     * @date 2020年09月18日
     */

    String getOpenId(String code);

    /**
     * h5微信登录绑定手机号
     *
     * @param code   :   从h5获取到的微信code值
     * @param mobile : 手机号
     * @return Map
     * @author xuzhch
     * @date 2020年09月18日
     */

    Map<String, Object> wechatBindMobile(@RequestParam(value = "code") String code, @RequestParam(value = "mobile") String mobile);

    /**
     * 功能描述：
     * 微信app登录
     *
     * @param wechatAppLoginDTO 微信app登录信息
     * @return map 登录结果
     * @author 宋文豪
     * @date 2020年09月18日
     */

    Map<String, Object> wechatAppLogin(@RequestBody WechatAppLoginDTO wechatAppLoginDTO);

    /**
     * 功能描述：
     * 微信app登录绑定用户信息
     *
     * @param wechatAppLoginDTO 微信app登录信息
     * @return map 绑定结果
     * @author 宋文豪
     * @date 2020年09月18日
     */
    Map<String, Object> wechatAppBindMobile(@RequestBody WechatAppLoginDTO wechatAppLoginDTO);
}
