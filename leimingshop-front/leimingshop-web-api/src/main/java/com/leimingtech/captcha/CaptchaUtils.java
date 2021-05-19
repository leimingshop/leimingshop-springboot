/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.captcha;

import com.leimingtech.redis.CaptchaRedis;
import com.wf.captcha.ArithmeticCaptcha;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 图片验证码工具类
 * @Date: 16:52 2019/6/25
 * @Version: V1.0
 */
@Component
public class CaptchaUtils {


    @Autowired
    private CaptchaRedis captchaRedis;

    /**
     * 生成验证码
     *
     * @param uuid: 唯一标识用于校验验证码
     * @return 验证码
     * @date 2019/6/25 16:54
     * @author lixiang
     **/
    public void create(HttpServletResponse response, String uuid) throws IOException {

        response.setContentType("image/gif");
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);

        // 算术类型
        ArithmeticCaptcha captcha = new ArithmeticCaptcha(130, 48);
        // 几位数运算，默认是两位
        captcha.setLen(2);
        captcha.out(response.getOutputStream());

        //保存验证码
        captchaRedis.set(uuid, captcha.text());
    }

    /**
     * 验证码校验
     *
     * @param uuid: 校验值
     * @param code: 验证码
     * @return 成功/失败
     * @date 2019/6/25 16:54
     * @author lixiang
     **/
    public boolean validate(String uuid, String code) {

        String captcha = captchaRedis.get(uuid);

        //验证码是否正确
        if (code.equalsIgnoreCase(captcha)) {
            return true;
        }

        return false;
    }
}
