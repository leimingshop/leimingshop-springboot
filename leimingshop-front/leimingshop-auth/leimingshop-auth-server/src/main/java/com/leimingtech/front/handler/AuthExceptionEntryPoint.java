/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leimingtech.commons.tools.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author tyl
 * @Date 2019/6/6 16:46
 * @Description
 **/
@Component
public class AuthExceptionEntryPoint implements AuthenticationEntryPoint {


    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        httpServletResponse.setHeader("Content-Type", "application/json;charset=UTF-8");

        String errorMessage = "";
        if (e.getCause() == null) {
            errorMessage = "抱歉，您未登录";
        } else if (e.getCause() instanceof InvalidTokenException) {
            errorMessage = "登录超时，请重新登录";
        } else {
            errorMessage = "抱歉，您未登录";
        }

        httpServletResponse.getWriter().
                write(objectMapper.writeValueAsString(
                        new Result<>().error(401, errorMessage)));
    }
}
