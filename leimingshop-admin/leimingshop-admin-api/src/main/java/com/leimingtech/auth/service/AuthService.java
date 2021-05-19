/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.service;

import com.leimingtech.auth.dto.AuthorizationDTO;
import com.leimingtech.auth.dto.LoginDTO;

/**
 * 认证服务
 *
 * @since 1.0.0
 */
public interface AuthService {

    /**
     * 登录
     *
     * @param login 登录
     * @return AuthorizationDTO 用户信息
     * @date 2020-09-18 16:34
     * @author huangkeyuan@leimingtech.com
     **/
    AuthorizationDTO login(LoginDTO login);

    /**
     * 退出
     *
     * @param userId 用户id
     * @date 2020-09-18 16:37
     * @author huangkeyuan@leimingtech.com
     **/
    void logout(Long userId);
}
