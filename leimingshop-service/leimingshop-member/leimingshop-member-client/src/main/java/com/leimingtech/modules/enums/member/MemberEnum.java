/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.member;


/**
 * 用户状态枚举
 *
 * @author DY 2019.5.10
 * @since 1.0.0
 */
public enum MemberEnum {
    /**
     * 微博接口返回性别 1男,
     */
    WEI_BO_SEX_MAN(1),
    /**
     * 微博接口返回性别 2女,
     */
    WEI_BOS_EX_WOMAN(2),
    /**
     * 用户性别：男
     */
    MEMBER_SEX_MAN(2),
    /**
     * 用户性别：女
     */
    MEMBER_SEX_WOMAN(1),
    /**
     * 用户性别：保密
     */
    MEMBER_SEX_PRIVACY(0),
    /**
     * 用户状态:正常
     */
    MEMBER_STATE_NO(0),
    /**
     * 用户状态:锁定
     */
    MEMBER_STATE_OFF(1),

    /**
     * 用户角色:商家
     */
    MEMEBER_ROLE_SELLER(1),
    /**
     * 用户角色:会员
     */
    MEMBER_ROLE_MEMBER(0),
    /**
     * 邮箱验证状态:已验证
     */
    EMAILVALIDATE_STATE_ON(1),

    /**
     * 邮箱验证状态:未验证
     */
    EMAILVALIDATE_STATE_OFF(0),
    /**
     * 删除标记:未删除
     */
    DEL_FLAG_NOT(0),
    /**
     * 删除标记:已删除
     */
    DEL_FLAG_YET(1),
    /**
     * 是否设置密码：未设置
     */
    PWDFLAG_NO(0),

    /**
     * 发送邮箱类型 1 忘记密码
     */
    SEND_TYPE_FORGET_PWD(1),
    /**
     * 发送邮箱类型 2 登录
     */
    SEND_TYPE_LOGIN(2),

    /**
     * 是否设置密码：已设置
     */
    PWDFLAG_YES(1);

    private int value;

    MemberEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
