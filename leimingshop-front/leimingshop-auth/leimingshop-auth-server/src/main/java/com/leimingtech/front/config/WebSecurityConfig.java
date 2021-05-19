/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.config;


import com.leimingtech.front.security.password.MyAuthenticationProvider;
import com.leimingtech.front.security.password.UserServiceDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Author tyl
 * @Date 2019/5/31 18:11
 * security 配置
 * @Description
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${oauth.url}")
    private String oauthUrl;

    @Value("${oauth.code}")
    private String oauthCode;

    @Value("${oauth.codeurl}")
    private String codeurl;

    @Value("${oauth.mobileurl}")
    private String mobileurl;

    @Value("${oauth.wechaturl}")
    private String wechaturl;

    @Value("${oauth.weibourl}")
    private String weibourl;

    @Value("${oauth.wechatpcurl}")
    private String wechatpcurl;

    @Value("${oauth.wechatAppUrl}")
    private String wechatAppUrl;

    @Value("${oauth.captchaurl}")
    private String captchaurl;

    @Autowired
    private UserServiceDetailsImpl userDetailsService;
//    @Autowired
//    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
//    @Autowired
//    private AuthenticationFailureHandler authenticationFailureHandler;
//    @Autowired
//    private MyLogoutHandler myLogoutHandler;
//    @Autowired
//    private MyLogoutSuccessHandler myLogoutSumccessHandler;
//    @Autowired
//    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
//    @Autowired
//    private ThirdPartyAuthenticationSecurityConfig thirdPartyAuthenticationSecurityConfig;
//    @Autowired
//    private CaptchaAuthenticationSecurityConfig captchaAuthenticationSecurityConfig;
//    @Autowired
//    private ValidateCodeFilter validateCodeFilter;

    /**
     * AuthenticationManagerBuilder中配置了验证的用户信息源和密码加密的策略
     *
     * @date 2020/6/2 14:02
     * @author lixiangx@leimingtech.com
     **/
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        return new MyAuthenticationProvider();
    }

    /**
     * 配置了验证管理的Bean
     * AuthenticationManager对象在OAuth2认证服务中要使用，提取放入IOC容器中
     *
     * @date 2020/6/2 14:03
     * @author lixiangx@leimingtech.com
     **/
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * 配置安全验证
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        //允许访问/oauth授权接口
//        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
//                .formLogin()
//                //登录需要经过的url请求
//                .loginProcessingUrl(oauthUrl)
//                .successHandler(myAuthenticationSuccessHandler)
//                .failureHandler(authenticationFailureHandler)
//                .and()
//                .logout()
//                .clearAuthentication(false)
//                .logoutUrl("/logout")
//                .logoutSuccessHandler(myLogoutSumccessHandler)
//                .invalidateHttpSession(true)
//                .addLogoutHandler(myLogoutHandler)
//                .deleteCookies()
//                //请求授权
//                .and()
//                .authorizeRequests()
//                //不需要权限认证的url
//                .antMatchers(HttpMethod.OPTIONS, oauthUrl, oauthCode, wechaturl,
//                        weibourl, wechatAppUrl, captchaurl, codeurl, mobileurl, wechatpcurl, "/logout", "/auth/oauth/token").permitAll()
//                //需要身份认证
//                //  .authenticated()
//                .and()
//                //关闭跨站请求防护
//                .csrf().disable()
//                .apply(thirdPartyAuthenticationSecurityConfig)
//                .and().apply(smsCodeAuthenticationSecurityConfig)
//                .and().apply(captchaAuthenticationSecurityConfig);
//        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }

}
