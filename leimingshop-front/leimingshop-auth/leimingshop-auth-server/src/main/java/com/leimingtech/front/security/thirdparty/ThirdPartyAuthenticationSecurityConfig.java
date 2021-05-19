/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.thirdparty;

import com.leimingtech.front.handler.AuthenticationFailureHandler;
import com.leimingtech.front.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * <第三方、手机号登录>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/24
 */
@Component
public class ThirdPartyAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    ThirdPartyUserServiceDetailsImpl thirdPartyUserServiceDetailsImpl;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Override
    public void configure(HttpSecurity http) throws Exception {
        ThirdPartyAuthenticationFilter thirdPartyAuthenticationFilter = new ThirdPartyAuthenticationFilter();

        //设置AuthenticationManager
        thirdPartyAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));

        //设置成功失败处理器
        thirdPartyAuthenticationFilter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        thirdPartyAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);

        //设置provider
        ThirdPartyAuthenticationProvider thirdPartyAuthenticationProvider = new ThirdPartyAuthenticationProvider();
        thirdPartyAuthenticationProvider.setThirdPartyUserServiceDetailsImpl(thirdPartyUserServiceDetailsImpl);

        http.authenticationProvider(thirdPartyAuthenticationProvider)
                .addFilterAfter(thirdPartyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    }

}
