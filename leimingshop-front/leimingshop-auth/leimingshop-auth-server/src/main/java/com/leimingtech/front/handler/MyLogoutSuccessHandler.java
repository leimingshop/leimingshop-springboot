/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.leimingtech.commons.tools.redis.RedisUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author tyl
 * @Date 2019/6/6 10:18
 * @Description
 **/
@Component
public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
    private Logger logger = LoggerFactory.getLogger(getClass());
    @Autowired
    private RedisUtils redisUtils;

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        // 暂时注释 后期查询问题
        logger.info("要退出的用户为{}", authentication);
        try {
            httpServletResponse.getWriter().write
                    (("{\n" +
                            "    \"code\": 200,\n" +
                            "    \"msg\": \"退出登录成功\",\n" +
                            "    \"data\": null\n" +
                            "}"));
        } catch (IOException e) {
            logger.error("错误信息为" + e);
        }

    }
}
