/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.password;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.commons.tools.security.password.PasswordUtils;
import com.leimingtech.commons.tools.security.password.RSACoder;
import com.leimingtech.commons.tools.utils.HttpContextUtils;
import com.leimingtech.commons.tools.utils.IpUtils;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import com.leimingtech.exception.ServiceException;
import com.leimingtech.modules.enums.member.LoginEnum;
import com.leimingtech.modules.service.member.MemberService;
import com.leimingtech.mq.constant.MqConstant;
import com.leimingtech.mq.service.RabbitMqSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @Author tyl
 * @Date 2019/5/31 18:11
 * security 自定义密码加密验证
 * @Description
 **/
@Slf4j
public class MyAuthenticationProvider implements AuthenticationProvider {

    private static RabbitMqSendService rabbitMqSendService;
    private static MemberService memberService;
    private static RedisUtils redisUtils;

    static {
        rabbitMqSendService = SpringContextUtils.getBean(RabbitMqSendService.class);
        memberService = SpringContextUtils.getBean(MemberService.class);
        redisUtils = SpringContextUtils.getBean(RedisUtils.class);
    }

    @Autowired
    private UserServiceDetailsImpl userServiceDetailsImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        // 获取登陆用户名
        String username = authentication.getName();
        // 根据用户名获取用户信息
        UserDetails user = userServiceDetailsImpl.loadUserByUsername(username);

        Optional.ofNullable(user).orElseThrow(() -> new ServiceException("账号不存在，请注册新账号"));

        // 使用RSA私钥进行解密
        String password = (String) authentication.getCredentials();
        try {
            password = (RSACoder.decryptByPrivateKey(StringUtils.replace(password, " ", "+")));
        } catch (Exception e) {
            log.error("账户:{},密码解析出现异常:{}", username, password, e);
            throw new ServiceException("用户名或密码错误");
        }

        HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
        if (!PasswordUtils.matches(password, user.getPassword())) {
            // 发送mq消息
            Optional.ofNullable(memberService.selectMemberByUserName(username)).ifPresent(m -> {
                Map<String, String> map = new HashMap<>(10);
                map.put("id", String.valueOf(m.getId()));
                map.put("username", m.getMemberName());
                map.put("userAgent", request.getHeader("User-Agent").toLowerCase());
                map.put("userIp", IpUtils.getIpAddr(request));
                map.put("loginTime", new Date().toString());
                map.put("status", "1");
                //0:PC登录  1:手机  2:其他
                map.put("source", request.getHeader("memberSource"));
                map.put("phoneNumber", m.getMemberMobile());
                rabbitMqSendService.sendMsg(MqConstant.LEIMINGTECH_USERLOGIN_QUEUE, JSON.toJSONString(map));
                // 登录失败存入redis
                addRedis(IpUtils.getIpAddr(request), m.getMemberName(), request.getHeader("memberSource"));
            });
            throw new ServiceException("用户名或密码错误");
        }

        //清除redis登陆错误次数
        redisUtils.delete(RedisKeys.getLoginFailedKey(username, IpUtils.getIpAddr(request), request.getHeader("memberSource")));
        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
    }

    /**
     * 登录失败存入redis
     *
     * @param ipAddr
     * @param memberName
     * @param memberSource
     */
    private void addRedis(String ipAddr, String memberName, String memberSource) {
        String loginFailed = RedisKeys.getLoginFailedKey(memberName, ipAddr, memberSource);

        Integer errorNum = (Integer) redisUtils.get(loginFailed);
        errorNum = errorNum == null ? 1 : errorNum + 1;

        redisUtils.set(loginFailed, errorNum);

        String astrictLoginTimeKey = RedisKeys.getAstrictLoginTimeKey(memberName);

        if (errorNum == LoginEnum.THREE.value()) {
            redisUtils.set(astrictLoginTimeKey, errorNum, RedisUtils.MIN_ONE_EXPIRE);
            throw new ServiceException("账号已经冻结，请60秒以后再试");
        } else if (errorNum == LoginEnum.SIX.value()) {
            redisUtils.set(astrictLoginTimeKey, errorNum, RedisUtils.HOUR_ONE_EXPIRE);
            throw new ServiceException("账号已经冻结，请60分钟以后再试");
        } else if (errorNum == LoginEnum.NINE.value()) {
            redisUtils.set(astrictLoginTimeKey, errorNum, RedisUtils.DEFAULT_EXPIRE);
            throw new ServiceException("账号已经冻结，请24小时以后再试");
        }
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }


    public void setUserServiceDetails(UserServiceDetailsImpl userServiceDetailsImpl) {
        this.userServiceDetailsImpl = userServiceDetailsImpl;
    }
}
