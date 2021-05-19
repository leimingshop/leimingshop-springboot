/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.sms;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @Author: SWH ab4856812@163.com
 * @Description: 手机验证码重写认证
 * @Date: 2019/8/9 21:57
 * @Version: V1.0
 */
@Slf4j
@Data
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SmsCodeUserServiceDetailsImpl smsCodeUserServiceDetailsImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //这个authentication就是SmsCodeAuthenticationToken
        SmsCodeAuthenticationToken authenticationToken = (SmsCodeAuthenticationToken) authentication;

        //校验手机号
        UserDetails user = smsCodeUserServiceDetailsImpl.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //这时候已经认证成功了
        SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    /**
     * 重写，调用SmsCodeAuthenticationToken
     */
    @Override
    public boolean supports(Class<?> authentication) {
        //该SmsCodeAuthenticationProvider智支持SmsCodeAuthenticationToken的token认证
        return SmsCodeAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
