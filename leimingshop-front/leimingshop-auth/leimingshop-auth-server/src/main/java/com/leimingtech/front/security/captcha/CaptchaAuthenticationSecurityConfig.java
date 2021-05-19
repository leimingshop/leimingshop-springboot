/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.captcha;

import com.leimingtech.front.handler.AuthenticationFailureHandler;
import com.leimingtech.front.handler.MyAuthenticationSuccessHandler;
import com.leimingtech.front.security.password.MyAuthenticationProvider;
import com.leimingtech.front.security.password.UserServiceDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 图形验证码验证方式
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/20 11:24
 **/
@Component
public class CaptchaAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private UserServiceDetailsImpl userServiceDetailsImpl;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        CaptchaAuthenticationFilter captchaAuthenticationFilter = new CaptchaAuthenticationFilter();

        //设置AuthenticationManager
        captchaAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
        //设置成功失败处理器
        captchaAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        captchaAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        //设置provider
        MyAuthenticationProvider captchaAuthenticationProvider = new MyAuthenticationProvider();
        captchaAuthenticationProvider.setUserServiceDetails(userServiceDetailsImpl);

        http.authenticationProvider(captchaAuthenticationProvider)
                .addFilterAfter(captchaAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
