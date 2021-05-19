/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.service.impl;

import com.leimingtech.auth.redis.CaptchaRedis;
import com.leimingtech.auth.service.CaptchaService;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码
 *
 * @since 1.0.0
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {
    @Autowired
    private CaptchaRedis captchaRedis;

    @Override
    public void create(HttpServletResponse response, String uuid) throws IOException {
        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        // 几位数运算，默认是两位
        captcha.setLen(2);
//        captcha.getArithmeticString();  // 获取运算的公式：3+2=?
//        captcha.text();  // 获取运算的结果：5
        captcha.out(response.getOutputStream());

        //保存验证码
        captchaRedis.set(uuid, captcha.text());
    }

    @Override
    public boolean validate(String uuid, String code) {
        String captcha = captchaRedis.get(uuid);

        //验证码是否正确
        if (code.equalsIgnoreCase(captcha)) {
            return true;
        }

        return false;
    }
}
