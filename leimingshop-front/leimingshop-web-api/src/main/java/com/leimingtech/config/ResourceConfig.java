/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.config;

/**
 * @author lixiang
 * @version V1.0
 * @date 2021/1/5 12:49
 **/

import com.leimingtech.front.filter.ValidateCodeFilter;
import com.leimingtech.front.handler.*;
import com.leimingtech.front.security.captcha.CaptchaAuthenticationSecurityConfig;
import com.leimingtech.front.security.password.UserServiceDetailsImpl;
import com.leimingtech.front.security.sms.SmsCodeAuthenticationSecurityConfig;
import com.leimingtech.front.security.thirdparty.ThirdPartyAuthenticationSecurityConfig;
import com.leimingtech.front.token.SingleTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


/**
 * @Author tyl
 * @Date 2019/5/29 16:03
 * @Description 资源服务器配置
 **/
@Configuration
@EnableResourceServer
public class ResourceConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    UserServiceDetailsImpl userDetailsService;
    @Value("${oauth.secret}")
    private String secret;
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
    private ValidateCodeFilter validateCodeFilter;
    @Autowired
    private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;
    @Autowired
    private MyLogoutHandler myLogoutHandler;
    @Autowired
    private MyLogoutSuccessHandler myLogoutSumccessHandler;
    @Autowired
    private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;
    @Autowired
    private ThirdPartyAuthenticationSecurityConfig thirdPartyAuthenticationSecurityConfig;
    @Autowired
    private CaptchaAuthenticationSecurityConfig captchaAuthenticationSecurityConfig;
    @Autowired
    private SingleTokenServices singleTokenServices;

    /**
     * 配置要认证的url信息
     * /** 表示全部
     *
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.addFilterBefore(validateCodeFilter, UsernamePasswordAuthenticationFilter.class)
                .formLogin()
                //登录需要经过的url请求
                .loginProcessingUrl(oauthUrl)
                .successHandler(myAuthenticationSuccessHandler)
                .failureHandler(authenticationFailureHandler)
                .and()
                .logout()
                .clearAuthentication(false)
                .logoutUrl("/auth/logout")
                .logoutSuccessHandler(myLogoutSumccessHandler)
                .invalidateHttpSession(true)
                .addLogoutHandler(myLogoutHandler)
                .deleteCookies()
                //请求授权
                .and()
                .authorizeRequests()
                //不需要权限认证的url
                .antMatchers(HttpMethod.OPTIONS, oauthUrl, oauthCode, wechaturl,
                        weibourl, wechatAppUrl, captchaurl, codeurl, mobileurl, wechatpcurl, "/druid/**", "/swagger-ui.html/**", "/actuator/**", "/doc.html/**",
                        "/swagger-resources/**", "/webjars/**", "/v2/api-docs/**", "/swagger/api-docs/**",
                        "/monitor/**", "/api/**", "/operation/**", "/cart/**", "/goods/**", "/store/**",
                        "/activity/coupon/center", "/activity/coupon/goods/detail", "/member/goods/favorites/isfavorites",
                        "/activity/seckill/home", "/activity/seckill/area/time", "/activity/seckill/soon/goods",
                        "/activity/seckill/current/goods", "/member/register/**", "/member/mobile/code",
                        "/member/bind/email/**", "/payment/pc", "/member/account/mobile/bind/**", "/member/browse/**", "/goodsclass/**").permitAll()
                //需要身份认证
                //  .authenticated()
                .and()
                //关闭跨站请求防护
                .csrf().disable()
                .apply(thirdPartyAuthenticationSecurityConfig)
                .and().apply(smsCodeAuthenticationSecurityConfig)
                .and().apply(captchaAuthenticationSecurityConfig);
        http.sessionManagement().maximumSessions(1).maxSessionsPreventsLogin(true);
    }


    /**
     * jwt认证
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter accessTokeConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(secret);
        return jwtAccessTokenConverter;

    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources
                .tokenStore(new JwtTokenStore(accessTokeConverter()))
                .stateless(true);
        resources.authenticationEntryPoint(new AuthExceptionEntryPoint());
        resources.tokenServices(singleTokenServices)
                .stateless(true);
    }

}

