/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.code;

/**
 * 用户注册入参码
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-05-19 16:05
 **/
public interface MemberCode {

    /**
     * 用户注册
     */
    String MEMBER_REGISTER_CODE = "200001";
    String MEMBER_REGISTER_DESC = "访问用户注册";

    /**
     * 校验用户注册
     */
    String MEMBER_VERIFY_REGISTER_CODE = "200002";
    String MEMBER_VERIFY_REGISTER_DESC = "访问校验用户注册";

    /**
     * 获取用户注册手机验证码
     */
    String MEMBER_REGISTER_PHONE_CODE = "200003";
    String MEMBER_REGISTER_PHONE_DESC = "访问获取用户注册手机验证码";

    /**
     * 忘记密码:根据手机号修改密码
     */
    String MEMBER_FORGET_PASSWORD_CODE = "200004";
    String MEMBER_FORGET_PASSWORD_DESC = "忘记密码:根据手机号修改密码";

    /**
     * 访问用户积分列表
     */
    String MEMBER_POINT_LIST_CODE = "200005";
    String MEMBER_POINT_LIST_DESC = "访问用户积分列表";

    /**
     * 访问用户积分详情
     */
    String MEMBER_POINT_DETAIL_CODE = "200006";
    String MEMBER_POINT_DETAIL_DESC = "访问用户积分详情";

    /**
     * 获取登陆验证码
     */
    String MEMBER_LOGIN_CODE = "200007";
    String MEMBER_LOGIN_DESC = "访问用户积分详情";
    /**
     * 访问个人中心
     */
    String MEMBER_CENTER_CODE = "200008";
    String MEMBER_CENTER_DESC = "访问个人中心";
    /**
     * 访问更换手机号获取验证码
     */
    String MEMBER_CHANGE_PHONE_CODE_CODE = "200009";
    String MEMBER_CHANGE_PHONE_CODE_DESC = "访问更换手机号获取验证码";
    /**
     * 访问更换手机号验证验证码
     */
    String MEMBER_VERIFY_CHANGE_PHONE_CODE = "200010";
    String MEMBER_VERIFY_CHANGE_PHONE_DESC = "访问更换手机号验证验证码";
    /**
     * 访问根据旧密码修改密码
     */
    String MEMBER_CHANGE_PWD_BY_OLD_CODE = "200011";
    String MEMBER_CHANGE_PWD_BY_OLD_DESC = "访问根据旧密码修改密码";
    /**
     * 访问个人信息修改
     */
    String MEMBER_INFO_UPDATE_CODE = "200012";
    String MEMBER_INFO_UPDATE_DESC = "访问个人信息修改";

    /**
     * 访问更换手机号获取验证码
     */
    String MEMBER_CHANGE_EMAIL_CODE_CODE = "200013";
    String MEMBER_CHANGE_EMAIL_CODE_DESC = "访问更换邮箱获取验证码";
    /**
     * 访问更换手机号验证验证码
     */
    String MEMBER_VERIFY_CHANGE_EMAIL_CODE = "200014";
    String MEMBER_VERIFY_CHANGE_EMAIL_DESC = "访问更换邮箱验证验证码";
    /**
     * 访问更换手机号验证验证码
     */
    String MEMBER_BIND_EMAIL_SEND_CODE = "200015";
    String MEMBER_BIND_EMAIL_SEND_DESC = "访问绑定邮箱发送验证信息";
    /**
     * 访问更换手机号验证验证码
     */
    String MEMBER_BIND_EMAIL_CALLBACK_CODE = "200016";
    String MEMBER_BIND_EMAIL_CALLBACK_DESC = "访问绑定邮箱回调方法";
    /**
     * 访问更换手机号获取验证码
     */
    String MEMBER_GET_MAIL_CODE_CODE = "200017";
    String MEMBER_GET_MAIL_CODE_DESC = "访问用户邮箱";
}
