/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

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
     * @throws IOException
     */
    void create(HttpServletResponse response, String uuid) throws IOException;

    /**
     * 验证码效验
     *
     * @param uuid uuid
     * @param code 验证码
     * @return true：成功  false：失败
     */
    boolean validate(String uuid, String code);

    /**
     * 滑动验证码效验
     *
     * @param token token
     * @param x     图形坐标
     * @param y     图形坐标
     * @return 是否成功
     */
    Map<String, Object> validateSlideCode(String token, int x, int y);

    /**
     * 手机验证码效验
     *
     * @param mobile 手机号
     * @param code   验证码
     * @return
     */
    boolean validateMobileCode(String mobile, String code);
}
