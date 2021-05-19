/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy.impl;

import com.alibaba.fastjson.JSONObject;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.front.strategy.LoginCheckStrategy;
import com.leimingtech.modules.dto.weibo.WeiboAccessTokenDTO;
import com.leimingtech.modules.enums.member.MemberErrorEnum;
import com.leimingtech.modules.service.weibo.WeiboService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * H5微博登陆策略
 *
 * @author chengqian
 */
@Slf4j
public class WeiboH5Strategy implements LoginCheckStrategy {

    private String weibourl = "/auth/weibo/login";

    @Override
    public Map<String, Object> validate() {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        Map<String, Object> resultMap = new HashMap<>(10);
        RedisUtils redisUtils = SpringContextUtils.getBean(RedisUtils.class);
        // 1.拦截微博登陆操作url，进行登录授权操作
        // 2.获取请求参数微博code
        WeiboService weiboService = SpringContextUtils.getBean(WeiboService.class);
        String code = httpServletRequest.getParameter("code");
        String mobile = httpServletRequest.getParameter("mobile");
        String validCode = httpServletRequest.getParameter("validCode");

        // 如果是微信登录的话，只有code有值，如果是微信登录后绑定手机号的话，手机号和验证码都有值
        if (mobile == null) {
            // 3.根据微信code调用微信授权接口，获得unionid,判断数据库是否存在用户，返回信息确定直接登陆或者是进入绑定手机页面
            resultMap = weiboService.weiboLogin(code);
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

            // 绑定手机号
            resultMap = weiboService.weiboBindMobile(code, mobile);

        }

        int resultCode = (int) resultMap.get("code");
        if (ErrorCode.SUCCESS == resultCode) {
            // 4.用户存在且已绑定手机号，放行登陆操作
            httpServletRequest.setAttribute("mobile", resultMap.get("mobile"));
        } else if (MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.code() == resultCode) {
            resultMap.put("code", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.code());
            resultMap.put("msg", MemberErrorEnum.E_WEIBO_MOBILE_UNBIND.value());

            // 5.放行到手机绑定页面，缓存用户信息用于新增用户或修改老用户微信绑定信息
            String jsonStr = JSONObject.toJSONString(resultMap.get("data"));
            WeiboAccessTokenDTO weiboAccessTokenDTO = JSONObject.parseObject(jsonStr, WeiboAccessTokenDTO.class);
            // 用户信息缓存5分钟，用户可以进行手机绑定，超时提示微信授权失败
            redisUtils.set(RedisKeys.getWeiboLoginKey(code), weiboAccessTokenDTO, 60 * 5);
            log.info("微博登陆绑定手机号缓存授权信息,key:{},dto:{}", RedisKeys.getWeiboLoginKey(code), weiboAccessTokenDTO);
            return resultMap;
        } else {
            resultMap.put("code", MemberErrorEnum.E_WEIBO_MOBILE_FAIL.code());
            resultMap.put("msg", MemberErrorEnum.E_WEIBO_MOBILE_FAIL.value());
            return resultMap;
        }
        return resultMap;
    }

    @Override
    public boolean check(String url) {
        return url.contains(weibourl) &&
                StringUtils.endsWithIgnoreCase(HttpContextUtils.getHttpServletRequest().getMethod(), "POST");
    }
}
