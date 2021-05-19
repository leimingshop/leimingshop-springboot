/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.front.service.CaptchaService;
import com.leimingtech.front.strategy.LoginCheckStrategy;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * 图形验证码登陆校验 策略
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/16 11:11
 **/
public class CaptchaStrategy implements LoginCheckStrategy {

    private final static String CAPTCHAURL = "/auth/oauth/captcha";

    /**
     * 图形验证码校验
     *
     * @date 2020/6/2 15:22
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public Map<String, Object> validate() {
        Map<String, Object> finalResultMap = new HashMap<>(10);

        // 直接使用ApplicationContext获取SpringBean
        CaptchaService captchaService = SpringContextUtils.getBean(CaptchaService.class);
        MemberService memberService = SpringContextUtils.getBean(MemberService.class);
        RabbitMqSendService rabbitMqSendService = SpringContextUtils.getBean(RabbitMqSendService.class);

        // 获取请求中的uuid和验证码进行校验
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        String uuid = StringUtils.join(httpServletRequest.getParameterMap().get("uuid"), "");
        String captcha = StringUtils.join(httpServletRequest.getParameterMap().get("captcha"), "");
        String username = StringUtils.join(httpServletRequest.getParameterMap().get("mobile"), "");

        if (!captchaService.validate(uuid, captcha)) {
            Optional.ofNullable(memberService.selectMemberByUserName(username)).ifPresent(m -> {
                Map<String, String> map = new HashMap<>(10);
                map.put("id", String.valueOf(m.getId()));
                map.put("username", username);
                String agent = httpServletRequest.getHeader("User-Agent").toLowerCase();
                map.put("userAgent", agent);
                map.put("userIp", IpUtils.getIpAddr(httpServletRequest));
                map.put("loginTime", new Date().toString());
                map.put("status", "1");
                //0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
                map.put("source", httpServletRequest.getHeader("memberSource"));
                map.put("phoneNumber", m.getMemberMobile());
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USERLOGIN_QUEUE, JSON.toJSONString(map));
            });

            finalResultMap.put("code", 400);
            finalResultMap.put("msg", "验证码错误");
            return finalResultMap;
        } else {
            finalResultMap.put("code", 200);
            finalResultMap.put("msg", "验证通过");
        }
        return finalResultMap;
    }

    /**
     * 校验地址信息是否符合要求
     *
     * @date 2020/6/4 10:57
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public boolean check(String url) {
        return url.contains(CAPTCHAURL)
                && StringUtils.endsWithIgnoreCase(HttpContextUtils.getHttpServletRequest().getMethod(), "POST");
    }
}
