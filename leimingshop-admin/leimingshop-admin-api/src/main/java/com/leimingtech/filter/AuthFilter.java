/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.filter;

import com.leimingtech.auth.service.ResourceService;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.security.user.UserDetail;
import com.leimingtech.commons.tools.utils.SpringContextUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@WebFilter(filterName = "authFilter", urlPatterns = "/*")
@Slf4j
public class AuthFilter implements Filter {
    /**
     * 不拦截的urls
     */
    private static List<String> urls = new ArrayList<String>();

    static {
        urls.add("/*/captcha/**");
        urls.add("/*/doc.html/**");
        urls.add("/*/login/**");
        urls.add("/*/swagger-resources/**");
        urls.add("/*/swagger-ui.html/**");
        urls.add("/*/webjars/**");
        urls.add("/*/v2/api-docs/**");
        urls.add("/*/swagger/api-docs");
        urls.add("/monitor/**");
        urls.add("/monitor/**");
        urls.add("/*/druid/**");
        urls.add("/*/actuator/**");
        urls.add("/*/synonym/es");
    }

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;


        if (httpRequest.getMethod().toLowerCase().equals(HttpMethod.OPTIONS.name().toLowerCase())) {

            String origin = httpRequest.getHeaders("origin").nextElement();
            httpResponse.setHeader("Access-Control-Allow-Origin", origin);
            // 这个allow-headers要配为*，这样才能允许所有的请求头 --- update by zxy  in 2018-10-19
            httpResponse.setHeader("Access-Control-Allow-Headers", "admin-token,Content-Type");
            httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
            httpResponse.setHeader("Access-Control-Allow-Methods", "PUT,POST,GET,DELETE,OPTIONS");
            httpResponse.setHeader("Access-Control-Max-Age", "18000");
            ((HttpServletResponse) response).setStatus(HttpStatus.SC_OK);
            return;
        }


        String requestUri = httpRequest.getRequestURI();
        //请求放行，无需验证权限
        if (pathMatcher(requestUri)) {
            chain.doFilter(request, response);
            return;
        }

        //获取用户token
        String token = ((HttpServletRequest) request).getHeader(Constant.ADMIN_TOKEN_HRADER);
        if (StringUtils.isBlank(token)) {
            token = ((HttpServletRequest) request).getParameter((Constant.ADMIN_TOKEN_HRADER));
        }
        //资源访问权限

        ResourceService resourceService = SpringContextUtils.getBean(ResourceService.class);

        UserDetail userDetail = resourceService.resource(token, requestUri, ((HttpServletRequest) request).getMethod());
        //获取用户信息
        if (userDetail != null) {

            // 在Spring Cloud gateway中添加filter的时候，要在header中加入含有中文属性的对象，在另外一个微服务中接受的时候，发现中文乱码，
            // 具体解决方案，是先用URLEncoder编码，然后微服务的接受的时候再解码 获取
            // 编码 在网关添加内容是编码  URLEncoder.encode(str, "UTF-8")
            // 解码 微服务获取时解码 URLDecoder.decode(str, "UTF-8")

            if (StringUtils.isNotBlank(userDetail.getUsername())) {
                try {
                    //编码
                    String encode = URLEncoder.encode(userDetail.getUsername(), "UTF-8");
                    userDetail.setUsername(encode);
                } catch (UnsupportedEncodingException e) {
                    log.error("用户名称:{}，解码异常:{}", userDetail.getUsername(), e);
                }
            }

            //当前登录用户userId，添加到header中
            request.setAttribute(Constant.USER_KEY, userDetail.getId() + "");
            request.setAttribute(Constant.USER_NAME_KEY, userDetail.getUsername() + "");
            chain.doFilter(request, response);
            return;

        }


        return;
    }

    private boolean pathMatcher(String requestUri) {
        for (String url : urls) {
            if (antPathMatcher.match(url, requestUri)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) {
        this.urls = urls;
    }

    @Override
    public void destroy() {
    }
}
