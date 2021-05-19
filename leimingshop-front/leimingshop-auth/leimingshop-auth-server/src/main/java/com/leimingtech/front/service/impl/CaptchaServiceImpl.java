/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.front.service.impl;

import com.leimingtech.commons.tools.redis.RedisUtils;
import com.leimingtech.front.service.CaptchaService;
import com.leimingtech.redis.CaptchaRedis;
import com.wf.captcha.ArithmeticCaptcha;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 验证码
 *
 * @since 1.0.0
 */
@Service
public class CaptchaServiceImpl implements CaptchaService {

    public static final String LOGIN_MOBILE_CODE_PREFIX = "login_mobile_code:";
    private static final String KEY_VALIDATE_TOKEN = "key_validate_token";
    private static final int NUM = 4;

    @Autowired
    private CaptchaRedis captchaRedis;
    @Autowired
    private RedisUtils redisUtils;

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


    @Override
    public Map<String, Object> validateSlideCode(String token, int x, int y) {
        Map<String, Object> resultMap = new HashMap<>(10);
        resultMap.put("code", 400);
        resultMap.put("massage", "验证不通过，请重试！");
        if (null == token || token.trim().length() < 1) {
            resultMap.put("code", 400);
            resultMap.put("massage", "请求参数错误:token为空！");
        }
        Map<String, Object> tokenObj = (Map<String, Object>) redisUtils.get(KEY_VALIDATE_TOKEN + ":" + token);
        if (null == tokenObj) {
            resultMap.put("code", 400);
            resultMap.put("massage", "验证码超期，请重新请求！");
            return resultMap;
        } else {
            int sX = (Integer) tokenObj.get("X");
            int sY = (Integer) tokenObj.get("Y");
            if (sY != y) {
                resultMap.put("code", 400);
                resultMap.put("massage", "请求参数错误:位置信息不正确！");
                return resultMap;
            } else {
                if (Math.abs(sX - x) <= NUM) {
                    resultMap.put("code", 200);
                    resultMap.put("massage", "验证通过！");
                } else {
                    resultMap.put("code", 400);
                    resultMap.put("massage", "验证码不通过，请重试！");
                    return resultMap;
                }
            }
        }
        resultMap.put("code", 200);
        resultMap.put("massage", "验证通过！");
        return resultMap;
    }

    @Override
    public boolean validateMobileCode(String mobile, String code) {
        String captcha = (String) redisUtils.get(LOGIN_MOBILE_CODE_PREFIX + mobile);
        if (StringUtils.isNotEmpty(captcha) && captcha.equals(code)) {
            redisUtils.delete(LOGIN_MOBILE_CODE_PREFIX + mobile);
            return true;
        }
        return false;
    }
}
