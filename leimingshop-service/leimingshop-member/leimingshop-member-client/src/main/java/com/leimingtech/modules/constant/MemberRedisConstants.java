/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constant;

/**
 * <redis常量类>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/18
 */
public class MemberRedisConstants {

    /**
     * 注册手机验证码缓存前缀
     */
    public static final String REGISTER_MOBILE_CODE_PREFIX = "register_mobile_code:";
    /**
     * 店铺入住验证码
     */
    public static final String STORE_REGISTER_MOBILE_CODE_PREFIX = "store_register_mobile_code:";
    /**
     * 忘记密码验证码
     */
    public static final String STORE_FORGET_PWD_PREFIX = "store_forget_pwd_code:";
    /**
     * 登陆手机验证码缓存前缀
     */
    public static final String LOGIN_MOBILE_CODE_PREFIX = "login_mobile_code:";
    /**
     * 修改密码手机验证码缓存前缀
     */
    public static final String UPDATE_PWD_MOBILE_CODE_PREFIX = "update_pwd_mobile_code:";
    /**
     * 绑定手机号验证码Key
     */
    public static final String MOBILE_BIND_CODE_PREFIX = "mobile_bind_code:";
    /**
     * 提现申请手机号验证码Key
     */
    public static final String APPLY_MONEY_CODE_PREFIX = "apply_money_code:";
    /**
     * 修改密码手机验证码缓存前缀
     */
    public static final String AVOID_REPEAT_SEND_PREFIX = "avoid_repeat_send_mobile:";
    /**
     * 更改邮箱验证码前缀
     */
    public static final String MEMBER_EMAIL_CODE = "member:email:code:";
    /**
     * 绑定邮箱缓存前缀
     */
    public static final String MEMBER_BIND_EMAIL = "member:bind:email:";
    /**
     * 邮箱验证信息缓存时间(10分钟)
     */
    public static final long EMAIL_CACHE_TIME = 600L;
    /**
     * 发送短信时间间隔（60秒）
     */
    public static final long AVOID_REPEAT_TIME = 60L;
    /**
     * 当天已经发起过提现申请
     */
    public static final String APPLY_MONEY = "1";

    private MemberRedisConstants() {
    }


}
