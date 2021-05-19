/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.strategy.impl;

import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.front.strategy.LoginCheckStrategy;
import com.leimingtech.modules.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * pc微信登陆策略
 *
 * @author chengqian
 */
@Slf4j
public class WechatPcStrategy implements LoginCheckStrategy {

    private String wechatPcurl = "/auth/wechat/pc/login";

    @Override
    public Map<String, Object> validate() {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        Map<String, Object> result = new HashMap<>(10);
        String code = httpServletRequest.getParameter("code");
        String mobile = httpServletRequest.getParameter("mobile");
        String validCode = httpServletRequest.getParameter("validCode");
        MemberService memberService = SpringContextUtils.getBean(MemberService.class);

        if (StringUtils.isBlank(mobile)) {
            result = memberService.wechatLogin(code);
        } else {
            result = memberService.wechatBind(code, mobile, validCode);
        }
        return result;
    }

    @Override
    public boolean check(String url) {
        HttpServletRequest httpServletRequest = HttpContextUtils.getHttpServletRequest();
        return url.contains(wechatPcurl) && StringUtils.endsWithIgnoreCase(httpServletRequest.getMethod(), "POST");
    }
}
