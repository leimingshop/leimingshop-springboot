/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service;

import com.leimingtech.modules.dto.WxOauth2ResDTO;
import com.leimingtech.modules.dto.WxUserDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 对接微信公众号服务层
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/1/19 11:20
 **/

public interface WechatMpService {

    /**
     * 微信公众号-推送消息
     *
     * @param paramsJson : 参数JSON
     * @param templateId : 模板ID
     * @param url        :        点击跳转地址
     * @param openId     用户标识
     * @return 消息ID
     * @author lixiangx @leimingtech.com
     * @date 2020 /1/19 11:27
     */

    String sendTemplateMsg(@RequestParam("paramsJson") String paramsJson,
                           @RequestParam("templateId") String templateId,
                           @RequestParam("url") String url,
                           @RequestParam("openId") String openId);

    /**
     * 微信登录
     *
     * @param code 微信code码
     * @return WxOauth2ResDTO 登录结果
     * @author xuzhch
     * @date 2020年09月18日
     */

    WxOauth2ResDTO wechatLogin(String code);

    /**
     * 获取微信用户信息
     *
     * @param wxOauth2ResDto 微信token信息
     * @return 用户信息
     * @author xuzhch
     * @date 2020年09月18日
     */

    WxUserDto getUserMessage(@RequestBody WxOauth2ResDTO wxOauth2ResDto);
}
