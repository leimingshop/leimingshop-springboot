/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.member;

/**
 * 用户错误信息枚举
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
public enum MemberErrorEnum {

    /**
     * 验证码已过期
     */
    E_CAPTCHA_PASS(1001, "验证码已过期"),
    /**
     * 验证码错误
     */
    E_CAPTCHA_ERROR(1002, "验证码错误"),
    /**
     * 手机号格式不正确
     */
    E_MOBILE_ERROR(1003, "手机号格式不正确"),
    /**
     * 手机号已被注册
     */
    E_MOBILE_REGISTERED(1003, "手机号已被注册"),
    /**
     * 手机号已被使用，请联系管理员
     */
    E_MOBILE_USE(1025, "手机号已被使用，请联系管理员"),
    /**
     * 邮箱不能为空
     */
    E_EMAIL_ERROR(1004, "邮箱不能为空"),
    /**
     * 用户名不能为空
     */
    E_MEMBERNAME_ERROR(1030, "用户名不能为空"),
    /**
     * 邮箱已被注册
     */
    E_EMAIL_REGISTERED(1005, "邮箱已被注册"),
    /**
     * 用户名已被注册
     */
    E_MEMBERNAME_REGISTERED(1031, "用户名已被注册"),
    /**
     * 请重新填写注册类型
     */
    E_LOGIN_TYPE(1006, "请重新填写注册类型"),
    /**
     * 验证码类型不能为空
     */
    E_CAPTCHA_TYPE_ERROR(1024, "验证码类型不能为空"),
    /**
     * 验证码已发送
     */
    E_CAPTCHA_SUCCESS(1007, "验证码已发送"),
    /**
     * 验证码已发送,请稍后重试
     */
    E_CAPTCHA_AGAIN(1008, "验证码已发送,请稍后重试"),
    /**
     * 获取accessToken失败
     */
    E_WECHAT_ACCESSTOKEN_ERROR(1009, "获取accessToken失败"),
    /**
     * 获取accessToken失败
     */
    E_WEIBO_ACCESSTOKEN_ERROR(1021, "获取accessToken失败"),
    /**
     * security用户名空,没有认证
     */
    E_USERLOGIN_CODE_ERROR(401, "security用户名空,没有认证"),
    /**
     * code不能为空
     */
    E_WECHAT_CODE_ERROR(1010, "code不能为空"),
    /**
     * 用户不存在
     */
    E_IS_NULL(1011, "用户不存在"),
    /**
     * 未使用手机号注册
     */
    E_MOBILE_IS_NULL(1012, "未使用手机号注册"),
    /**
     * 该号码已注册
     */
    E_MOBILE_IS_EXIT(1020, "该号码已注册"),
    /**
     * 发送失败，短信验证码未开启
     */
    E_MESSAGE_SMS_IS_NULL(1013, "发送失败，短信验证码未开启"),
    /**
     * 校验通过
     */
    E_REGISTER_SUCCESS(200, "校验通过"),
    /**
     * 微信登录失败，未绑定会员手机号
     */
    E_WECHAT_MOBILE_UNBIND(1017, "微信登录失败，未绑定会员手机号"),
    /**
     * 微博登录失败，未绑定会员手机号
     */
    E_WEIBO_MOBILE_UNBIND(1020, "微博登录失败，未绑定会员手机号"),
    /**
     * 当前手机账户已被禁止使用
     */
    E_WECHAT_BIND_ACCESS_EXCERTION(1015, "当前手机账户已被禁止使用"),
    /**
     * 该手机账号已绑定第三方平台
     */
    E_WECHAT_BINDED_MOBILE(1016, "该手机账号已绑定第三方平台"),
    /**
     * 获取微信授权失败
     */
    E_WECHAT_MOBILE_FAIL(1018, "获取微信授权失败"),
    /**
     * 获取微博授权失败
     */
    E_WEIBO_MOBILE_FAIL(1022, "获取微博授权失败"),
    /**
     * 注册失败，请联系管理员
     */
    E_REGISTER_FAILED(1014, "注册失败，请联系管理员"),
    /**
     * 微博授权不存在或已超时
     */
    E_WEIBO_BIND_ACCESS_EXCERTION(1023, "微博授权不存在或已超时"),
    /**
     * 该手机账号已绑定第三方平台
     */
    E_WEIBO_BINDED_MOBILE(1024, "该手机账号已绑定第三方平台"),;
    private String value;
    private int code;

    MemberErrorEnum(int code, String value) {
        this.value = value;
        this.code = code;
    }

    public String value() {
        return this.value;
    }

    public int code() {
        return this.code;
    }
}
