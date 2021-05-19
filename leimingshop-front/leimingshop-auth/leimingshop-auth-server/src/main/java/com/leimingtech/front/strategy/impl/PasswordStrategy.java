/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy.impl;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
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
 * 用户名密码滑动验证码登陆
 * 代码抽取
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/6/4 11:31
 **/
public class PasswordStrategy implements LoginCheckStrategy {

    private static String CODE_URL = "/auth/oauth/gettoken";

    /**
     * 校验用户名密码滑动验证码登陆
     *
     * @date 2020/6/2 15:22
     * @author lixiangx@leimingtech.com
     **/
    @Override
    public Map<String, Object> validate() {
        Map<String, Object> finalResultMap = new HashMap<>(10);

        CaptchaService captchaService = SpringContextUtils.getBean(CaptchaService.class);
        MemberService memberService = SpringContextUtils.getBean(MemberService.class);
        RabbitMqSendService rabbitMqSendService = SpringContextUtils.getBean(RabbitMqSendService.class);

        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        String token = httpServletRequest.getParameter("token");
        String x = httpServletRequest.getParameter("X");
        String y = httpServletRequest.getParameter("Y");
        String username = StringUtils.join(httpServletRequest.getParameterMap().get("username"), "");

        Map<String, Object> resultMap = captchaService.validateSlideCode(token, Integer.parseInt(x), Integer.parseInt(y));

        if (ErrorCode.SUCCESS != (int) resultMap.get(Constant.CODE)) {

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
            finalResultMap.put("msg", "验证失败");
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
        return url.contains(CODE_URL)
                && StringUtils.endsWithIgnoreCase(HttpContextUtils.getHttpServletRequest().getMethod(), "POST");
    }
}
