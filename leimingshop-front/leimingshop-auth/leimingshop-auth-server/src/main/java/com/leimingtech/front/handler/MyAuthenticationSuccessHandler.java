/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.handler;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.Result;
import com.leimingtech.front.entity.LmshopMember;
import com.leimingtech.modules.dto.member.MemberDTO;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.UnapprovedClientAuthenticationException;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author tyl
 * @description 当用户登录成功之后做的处理
 * @date 2019/5/29  10:06
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Autowired
    private ClientDetailsService clientDetailService;

    @Autowired
    @Qualifier("singleTokenServices")
    private AuthorizationServerTokenServices authorizationServerTokenServices;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RabbitMqSendService rabbitMqSendService;

    @Autowired
    private MemberService mymemberService;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        LmshopMember lmshopMember = (LmshopMember) authentication.getPrincipal();
        String clientId = lmshopMember.getClientId();
        String clientSecret = "secret";
        ClientDetails clientDetails = clientDetailService.loadClientByClientId(clientId);
        if (null == clientDetails) {
            throw new UnapprovedClientAuthenticationException("clientId不存在" + clientId);
        } else if (!StringUtils.equals(clientDetails.getClientSecret(), clientSecret)) {
            throw new UnapprovedClientAuthenticationException("clientSecret不匹配" + clientId);
        }

        // 创建oauth2 token与 refresh_token
        TokenRequest tokenRequest = new TokenRequest(null, clientDetails.getClientId(), clientDetails.getScope(), "custom");
        OAuth2Request oAuth2Request = tokenRequest.createOAuth2Request(clientDetails);
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(oAuth2Request, authentication);
        OAuth2AccessToken accessToken = authorizationServerTokenServices.createAccessToken(oAuth2Authentication);
        String refeshToken = accessToken.getRefreshToken().getValue();
        HashMap<Object, Object> hashMap = new HashMap<>(10);
        hashMap.put("token", accessToken.getValue());
        hashMap.put("refesh_token", refeshToken);

        MemberDTO memberDTO = mymemberService.selectMemberByUserName(lmshopMember.getUsername());
        memberDTO.setToken(RedisKeys.getH5UserKey(accessToken.getValue()));
        redisUtils.set(RedisKeys.getSecurityUserKey(memberDTO.getMemberName()), memberDTO, RedisUtils.NOT_EXPIRE);

        //认证成功，发送mq消息 增加用户登陆日志
        Map<String, String> map = new HashMap<>(10);
        map.put("id", String.valueOf(memberDTO.getId()));
        map.put("username", memberDTO.getMemberName());
        map.put("userAgent", request.getHeader("User-Agent").toLowerCase());
        map.put("userIp", IpUtils.getIpAddr(request));
        map.put("loginTime", new Date().toString());
        map.put("status", "0");
        map.put("phoneNumber", memberDTO.getMemberMobile());
        //0:pc,1:h5,2:android,3:ios,4:微信,5:小程序
        map.put("source", request.getHeader("memberSource"));
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USERLOGIN_QUEUE, JSON.toJSONString(map));

        // 发送MQ消息  维护用户登陆信息
        Map<String, Object> memberMap = new HashMap<>(10);
        memberMap.put("memberId", memberDTO.getId());
        memberMap.put("memberName", memberDTO.getMemberName());
        memberMap.put("lastLoginDate", memberDTO.getMemberLoginTime());
        memberMap.put("lastLoginIp", memberDTO.getMemberLoginIp());
        memberMap.put("memberLoginIp", IpUtils.getIpAddr(request));
        memberMap.put("memberLoginTime", new Date());
        memberMap.put("deviceToken", request.getHeader("deviceToken"));
        memberMap.put("umengSource", request.getHeader("umengSource"));
        rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USER_LOGIN_MESSAGE_QUEUE, JSON.toJSONString(memberMap));

        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(new Result<>().ok(hashMap, "登录成功")));
    }

}
