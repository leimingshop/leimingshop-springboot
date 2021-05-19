/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.filter;

import com.alibaba.fastjson.JSON;
import com.leimingtech.commons.tools.constant.Constant;
import com.leimingtech.commons.tools.exception.ErrorCode;
import com.leimingtech.front.strategy.StrategyContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


/**
 * @Author tyl
 * @Date 2019/6/1 11:23
 * @Description 自定义OncePerRequestFilter 验证码验证
 **/
@Slf4j
@Component
public class ValidateCodeFilter extends OncePerRequestFilter {
    private final String MOBILE_STR = "mobile";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 使用策略模式执行授权校验
        Map<String, Object> resultMap = StrategyContext.contextMethod(httpServletRequest.getRequestURI());

        if (resultMap != null && ErrorCode.SUCCESS != (int) resultMap.get(Constant.CODE)) {
            httpServletResponse.setContentType("application/json;charset=UTF-8");
            httpServletResponse.getWriter().write(JSON.toJSONString(resultMap));
            return;
        }
        if (null != resultMap && null != resultMap.get(MOBILE_STR)) {
            httpServletRequest.setAttribute(MOBILE_STR, resultMap.get(MOBILE_STR));
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
