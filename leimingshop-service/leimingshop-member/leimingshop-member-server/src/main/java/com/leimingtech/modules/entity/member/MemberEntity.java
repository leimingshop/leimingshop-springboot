/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.entity.member;

import com.baomidou.mybatisplus.annotation.*;
import com.leimingtech.commons.mybatis.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("lmshop_member")
public class MemberEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String memberName;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 用户手机号
     */
    private String memberMobile;

    /**
     * 用户邮箱
     */
    private String memberEmail;

    /**
     * 真实姓名
     */
    private String memberRealName;

    /**
     * 用户头像
     */
    private String memberAvatar;

    /**
     * 用户性别 （默认0:保密，1：女，2：男）
     */
    private Integer memberSex;

    /**
     * 用户密码
     */
    private String memberPasswd;

    /**
     * 用户生日
     */
    private Date memberBirthday;

    /**
     * 用户当前登录IP
     */
    private String memberLoginIp;

    /**
     * 用户当前登录时间
     */
    private Date memberLoginTime;

    /**
     * 用户最后登录时间
     */
    private Date lastLoginDate;

    /**
     * 用户最后登录IP
     */
    private String lastLoginIp;

    /**
     * 微信openId
     */
    private String wechatOpenid;

    /**
     * 当且仅当该网站应用已获得该用户的userinfo授权时
     */
    private String wechatUnionid;

    /**
     * QQopenId
     */
    private String qqOpenid;

    /**
     * 微博uid
     */
    private String weiboUid;
    /**
     * 友盟token
     */
    private String deviceToken;
    /**
     * 友盟来源（1：安卓，2：ios）
     */
    private Integer umengSource;

    /**
     * 用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
     */
    private Integer memberSource;

    /**
     * 会员角色
     * 会员角色：0：会员，1：店铺和会员
     */
    private Integer memberRole;

    /**
     * 用户状态（默认0:正常、1:锁定）
     */
    private Integer memberState;

    /**
     * 邮箱验证状态（默认为0:未验证，1:已验证）
     */
    private Integer emailValidateState;

    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private String updater;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateDate;

    /**
     * 删除标记（默认为0未删除，1已删除）
     */
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer delFlag;

    /**
     * 乐观锁
     */
    @Version
    private Integer version;

}
