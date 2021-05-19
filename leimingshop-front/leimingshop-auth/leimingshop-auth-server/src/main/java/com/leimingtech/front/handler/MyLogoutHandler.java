/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.handler;

import com.leimingtech.front.entity.LmshopMember;
import com.leimingtech.modules.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author tyl
 * @Date 2019/6/5 20:24
 * @Description
 **/
@Component
@FrameworkEndpoint
@Slf4j
public class MyLogoutHandler implements LogoutHandler {

    @Autowired
    @Qualifier("consumerTokenServices")
    ConsumerTokenServices consumerTokenServices;

    @Autowired
    private MemberService memberService;


    @Override
    public void logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) {

        if (authentication != null) {
            LmshopMember lmshopMember = (LmshopMember) authentication.getPrincipal();
            memberService.logout(lmshopMember.getId());
        } else {
            log.error("Authentication对象为空，未删除用户友盟token");
        }

        String token = httpServletRequest.getParameter("access_token");
        consumerTokenServices.revokeToken(token);


    }
}
