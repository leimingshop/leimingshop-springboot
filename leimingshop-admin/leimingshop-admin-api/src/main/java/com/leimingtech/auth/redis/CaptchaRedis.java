/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.auth.redis;

import com.leimingtech.commons.tools.redis.RedisKeys;
import com.leimingtech.commons.tools.redis.RedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 验证码Redis
 *
 * @since 1.0.0
 */
@Component
public class CaptchaRedis {
    /**
     * 验证码5分钟过期
     */
    private static final long EXPIRE = 60 * 5L;
    @Autowired
    private RedisUtils redisUtils;

    public void set(String uuid, String captcha) {
        String key = RedisKeys.getLoginCaptchaKey(uuid);
        redisUtils.set(key, captcha, EXPIRE);
    }

    public String get(String uuid) {
        String key = RedisKeys.getLoginCaptchaKey(uuid);
        String captcha = (String) redisUtils.get(key);

        //删除验证码
        if (captcha != null) {
            redisUtils.delete(key);
        }

        return captcha;
    }
}