/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy.impl;

import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.front.strategy.LoginCheckStrategy;
import com.leimingtech.modules.dto.wechat.WechatAppLoginDTO;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.wechat.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


/**
 * APP微信登陆
 * 代码抽取
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/6/4 11:45
 **/
@Slf4j
public class WechatAppStrategy implements LoginCheckStrategy {

    private String wechatAppUrl = "/auth/wechat/app/login";

    /**
     * 校验APP微信登陆
     *
     * @date 2020/6/2 15:22
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public Map<String, Object> validate() {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        Map<String, Object> resultMap = new HashMap<>(10);
        RedisUtils redisUtils = SpringContextUtils.getBean(RedisUtils.class);

        WechatService wechatService = SpringContextUtils.getBean(WechatService.class);
        String mobile = httpServletRequest.getParameter("mobile");
        String validCode = httpServletRequest.getParameter("validCode");
        WechatAppLoginDTO wechatAppLoginDTO = new WechatAppLoginDTO();
        wechatAppLoginDTO.setAvatar(httpServletRequest.getParameter("iconurl"));
        wechatAppLoginDTO.setMobile(mobile);
        wechatAppLoginDTO.setValidCode(validCode);
        wechatAppLoginDTO.setSex(httpServletRequest.getParameter("gender"));
        wechatAppLoginDTO.setNickName(httpServletRequest.getParameter("screen_name"));
        wechatAppLoginDTO.setOpenId(httpServletRequest.getParameter("openId"));
        wechatAppLoginDTO.setUnionId(httpServletRequest.getParameter("unionId"));
        // 判断是否需要绑定手机号
        if (null == mobile) {
            resultMap = wechatService.wechatAppLogin(wechatAppLoginDTO);
        } else {

            // 1.获取验证码
            Object obj = redisUtils.get(RedisKeys.getMobileBindCaptchaKey(mobile));
            if (obj == null) {
                resultMap.put("code", MemberErrorEnum.E_CAPTCHA_ERROR.code());
                resultMap.put("msg", MemberErrorEnum.E_CAPTCHA_ERROR.value());
                return resultMap;
            }
            String validCodeReids = obj.toString();

            // 2.判断验证码是否正确
            if (!StringUtils.equals(validCode, validCodeReids)) {
                resultMap.put("code", MemberErrorEnum.E_CAPTCHA_ERROR.code());
                resultMap.put("msg", MemberErrorEnum.E_CAPTCHA_ERROR.value());
                return resultMap;
            }
            wechatAppLoginDTO.setMobile(mobile);
            resultMap = wechatService.wechatAppBindMobile(wechatAppLoginDTO);

        }

        int resultCode = (int) resultMap.get("code");
        if (ErrorCode.SUCCESS == resultCode) {
            // 4.用户存在且已绑定手机号，放行登陆操作
            httpServletRequest.setAttribute("mobile", resultMap.get("mobile"));
        } else if (MemberErrorEnum.E_WECHAT_MOBILE_UNBIND.code() == resultCode) {
            resultMap.put("code", MemberErrorEnum.E_WECHAT_MOBILE_UNBIND.code());
            resultMap.put("msg", MemberErrorEnum.E_WECHAT_MOBILE_UNBIND.value());
            return resultMap;
        } else {
            resultMap.put("code", MemberErrorEnum.E_WECHAT_MOBILE_FAIL.code());
            resultMap.put("msg", MemberErrorEnum.E_WECHAT_MOBILE_FAIL.value());
            return resultMap;
        }
        return resultMap;
    }


    /**
     * 校验地址信息是否符合要求
     *
     * @date 2020/6/4 10:57
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean check(String url) {
        return url.contains(wechatAppUrl)
                && StringUtils.endsWithIgnoreCase(HttpContextUtils.getHttpServletRequest().getMethod(), "POST");
    }
}
