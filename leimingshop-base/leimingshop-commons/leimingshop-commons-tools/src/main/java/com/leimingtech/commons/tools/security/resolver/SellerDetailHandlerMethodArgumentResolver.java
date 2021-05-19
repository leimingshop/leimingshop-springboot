/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.security.resolver;

import com.leimingtech.commons.tools.security.user.SecurityUser;
import com.leimingtech.commons.tools.security.user.SellerDetail;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/**
 * @Author: lixiang
 * @Description: 自定义参数解析器解析SellerDetail
 * @Date: 2019/6/26 15:22
 * @Version: V1.0
 */
@Component
public class SellerDetailHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().isAssignableFrom(SellerDetail.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                  NativeWebRequest request, WebDataBinderFactory factory) {
        //获取用户信息
        return SecurityUser.getSeller();
    }
}