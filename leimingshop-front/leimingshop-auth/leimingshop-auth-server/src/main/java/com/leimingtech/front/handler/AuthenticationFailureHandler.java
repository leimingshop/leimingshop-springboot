/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.handler;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.commons.tools.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author tyl
 * @Date 2019/6/1 11:20
 * @Description security失败处理器
 **/
@Slf4j
@Component
public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        log.info("用户登录失败:{}", exception.getMessage());

        response.setStatus(ErrorCode.INTERNAL_SERVER_ERROR);
        //将 登录失败 信息打包成json格式返回
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(new Result()
                .error(ErrorCode.ACCOUNT_PASSWORD_ERROR, exception.getMessage())));
    }
}
