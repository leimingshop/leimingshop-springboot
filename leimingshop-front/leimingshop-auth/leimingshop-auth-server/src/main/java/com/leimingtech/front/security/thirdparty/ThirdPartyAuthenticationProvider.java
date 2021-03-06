/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.thirdparty;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * <第三方登录、手机登录>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/24
 */
@Slf4j
@Data
public class ThirdPartyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    ThirdPartyUserServiceDetailsImpl thirdPartyUserServiceDetailsImpl;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        //这个authentication就是WechatAuthenticationProvider
        ThirdPartyAuthenticationToken authenticationToken = (ThirdPartyAuthenticationToken) authentication;

        //校验手机号
        log.info("微信用户登录11111111111111");
        UserDetails user = thirdPartyUserServiceDetailsImpl.loadUserByUsername((String) authenticationToken.getPrincipal());
        if (user == null) {
            throw new InternalAuthenticationServiceException("无法获取用户信息");
        }

        //这时候已经认证成功了
        ThirdPartyAuthenticationToken authenticationResult = new ThirdPartyAuthenticationToken(user, user.getAuthorities());
        authenticationResult.setDetails(authenticationToken.getDetails());

        return authenticationResult;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        //该SmsCodeAuthenticationProvider智支持SmsCodeAuthenticationToken的token认证
        return ThirdPartyAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
