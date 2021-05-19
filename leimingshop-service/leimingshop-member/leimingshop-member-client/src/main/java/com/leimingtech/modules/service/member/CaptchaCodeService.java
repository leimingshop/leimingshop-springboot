/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.service.member;


import org.springframework.web.bind.annotation.RequestParam;

/**
 * 验证码
 *
 * @author dy 2019/5/12
 * @since 1.0.0
 */

public interface CaptchaCodeService {


    /**
     * 获取手机验证码
     *
     * @param mobile 手机号
     * @param prefix redisKey前缀
     * @author xuzhch
     * @date 2020年09月18日
     */

    void moblieCaptcha(@RequestParam("mobile") String mobile, @RequestParam("prefix") String prefix);

    /**
     * 获取邮箱验证码
     *
     * @param email    验证码
     * @param sendType 发送类型 1 忘记密码 2 登录
     * @author xuzhch
     * @date 2020年09月18日
     */

    void emailCaptha(@RequestParam("email") String email, @RequestParam("sendType") Integer sendType);

}
