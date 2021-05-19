/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.code;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * @ClassName MemberStatusCode
 * @Description 会员模块日志码
 * @Author DY
 * @Date 2019/7/3 15:04
 * @Version 1.0
 */

public class MemberStatusCode extends ServiceStatusCode {

    /**
     * 会员管理列表查询成功
     */
    public static final String ADMIN_MEMBER_PAGE_SUCCESS_CODE = "2001001";
    public static final String ADMIN_MEMBER_PAGE_SUCCESS_MSG = "会员列表分页查询成功";
    /**
     * 会员管理列表查询成功(站内信)
     */
    public static final String ADMIN_MEMBER_MESSAGE_PAGE_SUCCESS_CODE = "2001002";
    public static final String ADMIN_MEMBER_MESSAGE_PAGE_SUCCESS_MSG = "站内信会员列表分页查询成功";
    /**
     * 会员详情查询成功
     */
    public static final String ADMIN_MEMBER_DETAILS_SUCCESS_CODE = "2001003";
    public static final String ADMIN_MEMBER_DETAILS_SUCCESS_MSG = "会员详情查询成功";
    /**
     * 会员信息保存成功
     */
    public static final String ADMIN_MEMBER_SAVE_SUCCESS_CODE = "2001004";
    public static final String ADMIN_MEMBER_SAVE_SUCCESS_MSG = "会员信息保存成功";
    /**
     * 会员信息修改成功
     */
    public static final String ADMIN_MEMBER_UPDATE_SUCCESS_CODE = "2001005";
    public static final String ADMIN_MEMBER_UPDATE_SUCCESS_MSG = "会员信息修改成功";
    /**
     * 会员编辑信息查询成功
     */
    public static final String ADMIN_MEMBER_EDIT_SUCCESS_CODE = "2001006";
    public static final String ADMIN_MEMBER_EDIT_SUCCESS_MSG = "会员编辑信息查询成功";
    /**
     * 会员信息删除成功
     */
    public static final String ADMIN_MEMBER_DEL_SUCCESS_CODE = "2001007";
    public static final String ADMIN_MEMBER_DEL_SUCCESS_MSG = "会员信息删除成功";

    /**
     * 会员密码重置成功
     */
    public static final String ADMIN_MEMBER_RESET_PWD_SUCCESS_CODE = "2001008";
    public static final String ADMIN_MEMBER_RESET_PWD_SUCCESS_MSG = "重置密码成功";
    /**
     * 修改用户状态
     */
    public static final String ADMIN_MEMBER_STATE_SUCCESS_CODE = "2001009";
    public static final String ADMIN_MEMBER_STATE_SUCCESS_MSG = "修改用户状态成功";
    /**
     * 获取微信openid
     */
    public static final String MEMBER_WXOPENID_SUCCESS_CODE = "200001";
    public static final String MEMBER_WXOPENID_SUCCESS_MSG = "获取微信openid";
    /**
     * 获取微博登录信息
     */
    public static final String MEMBER_WEIBO_SUCCESS_CODE = "200002";
    public static final String MEMBER_WEIBO_SUCCESS_MSG = "获取微博登录信息";
    /**
     * 两次输入密码不一致
     */
    public static final ServiceStatusCode ORDER_CHANGE_SHOW_STATUS_EXCERTION = new MemberStatusCode("400101", "两次输入密码不一致");

    /**
     * 用户不存在
     */
    public static final ServiceStatusCode MEMBER_NOT_EXIST = new MemberStatusCode("400501", "用户不存在");
    /**
     * 发送失败，短信验证码未开启
     */
    public static final ServiceStatusCode MESSAGE_SEND_FAILED = new MemberStatusCode("400502", "发送失败，短信验证码未开启");


    /**
     * 发送失败，发送短信时间间隔为60s
     */
    public static final ServiceStatusCode MESSAGE_SEND_TIME_FAILED = new MemberStatusCode("400503", "发送失败，发送短信时间间隔为60s");
    /**
     * 邮箱验证码验证失败
     */
    public static final ServiceStatusCode EMAIL_VERIFY_FAIL = new MemberStatusCode("400504", "邮箱验证码验证失败");
    /**
     * 邮箱已被绑定
     */
    public static final ServiceStatusCode EMAIL_REPEAT_BINDING_FAIL = new MemberStatusCode("400505", "邮箱已被绑定");
    /**
     * 身份验证失败，请重试
     */
    public static final ServiceStatusCode EMAIL_BIND_VLIDATE_FAIL = new MemberStatusCode("400506", "身份验证失败，请重试");
    /**
     * 密码解析失败
     */
    public static final ServiceStatusCode PASSWORD_ANALYZE_FAIL = new MemberStatusCode("400507", "密码解析失败");
    /**
     * 旧密码不能为空
     */
    public static final ServiceStatusCode OLD_PAY_PASSWORD_NOT_NULL = new MemberStatusCode("400508", "旧密码不能为空");
    /**
     * 原支付密码输入错误
     */
    public static final ServiceStatusCode OLD_PAY_PASSWORD_INPUT_ERROR = new MemberStatusCode("400509", "原支付密码输入错误");
    /**
     * 新密码与确认密码不一致
     */
    public static final ServiceStatusCode PASSWORD_AND_CONFRIMPWD_MISMATCH = new MemberStatusCode("400510", "新密码与确认密码不一致");
    /**
     * 支付密码只能输入6位数字
     */
    public static final ServiceStatusCode PASSWORD_FORMAT_MUST_NUMBER = new MemberStatusCode("400511", "支付密码只能输入6位数字");
    /**
     * 支付密码修改失败，请重试
     */
    public static final ServiceStatusCode PASSWORD_UPDATE_FAIL = new MemberStatusCode("400512", "支付密码修改失败，请重试");
    /**
     * 支付密码输入错误
     */
    public static final ServiceStatusCode PAY_PASSWORD_CHECK_FAIL = new MemberStatusCode("400513", "支付密码输入错误");
    /**
     * 扣减失败，扣除后余额不能少于0
     */
    public static final ServiceStatusCode BALANCE_CANNOT_LESS_THAN_ZERO = new MemberStatusCode("400514", "扣减失败，扣除后余额不能少于0");
    /**
     * 会员等级不存在
     */
    public static final ServiceStatusCode MEMBER_GRADE_NOT_EXIST = new MemberStatusCode("400515", "会员等级不存在");
    /**
     * 查询上次选择地址异常
     */
    public static final ServiceStatusCode GET_MEMBER_LAST_SELECTED_EXCEPTION = new MemberStatusCode("400516", "查询会员上次选择地址异常,one to many exception");
    /**
     * 获取微信信息失败
     */
    public static final ServiceStatusCode GET_WECHAT_OPENID_ERROR = new ServiceStatusCode
            .InternalServiceStatusCode("500501", "获取微信信息失败", new Object[0]);
    /**
     * 开始进行用户的积分封装
     */
    public static final String COMPUTE_MEMBER_POINT_CODE = "200002";
    public static final String COMPUTE_MEMBER_POINT_CODE_MSG = "";
    /**
     * 用户最终的积分
     */
    public static final String FINAL_COMPUTE_MEMBER_POINT_CODE = "200003";
    public static final String FINAL_COMPUTE_MEMBER_POINT_CODE_MSG = "";

    protected MemberStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
