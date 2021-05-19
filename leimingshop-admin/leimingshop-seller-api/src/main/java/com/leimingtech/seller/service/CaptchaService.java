/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.seller.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码
 *
 * @since 1.0.0
 */
public interface CaptchaService {

    /**
     * 图片验证码
     *
     * @param response 响应
     * @param uuid     id
     * @date 2020-09-18 16:39
     * @author huangkeyuan@leimingtech.com
     **/
    void create(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     * @return true：成功  false：失败
     */
    boolean validate(String uuid, String code);

}
