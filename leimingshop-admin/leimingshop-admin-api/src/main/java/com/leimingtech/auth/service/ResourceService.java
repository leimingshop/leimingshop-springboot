/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.service;

import com.leimingtech.commons.tools.security.user.UserDetail;

/**
 * 资源服务
 *
 * @since 1.0.0
 */
public interface ResourceService {

    /**
     * 是否有资源访问权限
     *
     * @param token  token
     * @param url    资源URL
     * @param method 请求方式
     * @return 有访问权限，则返回用户信息
     */
    UserDetail resource(String token, String url, String method);
}