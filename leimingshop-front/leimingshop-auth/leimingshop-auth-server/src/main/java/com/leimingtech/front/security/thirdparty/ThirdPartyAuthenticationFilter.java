/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.security.thirdparty;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * <微信登录>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/8/24
 */
public class ThirdPartyAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    /**
     * 请求中，参数为unionid
     */
    private String mobileParameter = "mobile";

    /**
     * 是否只处理post请求
     */
    private boolean postOnly = true;

    public ThirdPartyAuthenticationFilter() {
        //要拦截的请求
        super(new AntPathRequestMatcher("/**/login", HttpMethod.POST.name()));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if (this.postOnly && !request.getMethod().equals(HttpMethod.POST.name())) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        } else {
            String mobile = StringUtils.trim(this.obtainMobile(request));
            //把unionid传进WechatAuthenticationToken
            ThirdPartyAuthenticationToken authRequest = new ThirdPartyAuthenticationToken(mobile);
            this.setDetails(request, authRequest);
            //调用AuthenticationManager
            return this.getAuthenticationManager().authenticate(authRequest);
        }
    }

    /**
     * 获取手机号
     *
     * @param request request
     * @return String
     */
    private String obtainMobile(HttpServletRequest request) {
        return (String) request.getAttribute(this.mobileParameter);
    }

    private void setDetails(HttpServletRequest request, ThirdPartyAuthenticationToken authRequest) {
        authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
    }

    public void setMobileParameter(String wechatParameter) {
        Assert.hasText(wechatParameter, "Mobile parameter must not be empty or null");
        this.mobileParameter = wechatParameter;
    }


    public void setPostOnly(boolean postOnly) {
        this.postOnly = postOnly;
    }

    public final String getUsernameParameter() {
        return this.mobileParameter;
    }

}
