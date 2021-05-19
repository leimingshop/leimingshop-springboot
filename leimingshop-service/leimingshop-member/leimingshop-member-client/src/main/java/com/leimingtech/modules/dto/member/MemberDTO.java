/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;


/**
 * 会员表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@ApiModel(description = "MemberDTO")
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    private String memberName;

    @ApiModelProperty(value = "用户等级名称")
    private String gradeName;

    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String memberMobile;
    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    private String memberEmail;
    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String memberRealName;
    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;
    /**
     * 用户性别（默认0:保密，1：女，2：男）
     */
    @ApiModelProperty(value = "用户性别 （默认0:保密，1：女，2：男）")
    private Integer memberSex;
    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    private String memberPasswd;
    /**
     * 用户生日
     */
    @ApiModelProperty(value = "用户生日")
    private Date memberBirthday;
    /**
     * 用户当前登录IP
     */
    @ApiModelProperty(value = "用户当前登录IP")
    private String memberLoginIp;
    /**
     * 用户当前登录时间
     */
    @ApiModelProperty(value = "用户当前登录时间")
    private Date memberLoginTime;
    /**
     * 用户最后登录时间
     */
    @ApiModelProperty(value = "用户最后登录时间")
    private Date lastLoginDate;
    /**
     * 用户最后登录IP
     */
    @ApiModelProperty(value = "用户最后登录IP")
    private String lastLoginIp;
    /**
     * 微信openId
     */
    @ApiModelProperty(value = "微信openId")
    private String wechatOpenid;
    /**
     * 当且仅当该网站应用已获得该用户的userinfo授权时
     */
    @ApiModelProperty(value = "微信Unionid")
    private String wechatUnionid;
    /**
     * QQopenId
     */
    @ApiModelProperty(value = "QQopenId")
    private String qqOpenid;
    /**
     * 微博uid
     */
    @ApiModelProperty(value = "微博uid")
    private String weiboUid;
    /**
     * 用户来源（默认0:网站注册）
     */
    @ApiModelProperty(value = "用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    private Integer memberSource;
    /**
     * 用户状态（默认0:正常、1:锁定）
     */
    @ApiModelProperty(value = "用户状态（默认0:正常、1:锁定）")
    private Integer memberState;
    /**
     * 邮箱验证状态（默认为0:未验证，1:已验证）
     */
    @ApiModelProperty(value = "邮箱验证状态（默认为0:未验证，1:已验证）")
    private Integer emailValidateState;

    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    private Date createDate;

    /**
     * 用户token
     */
    @ApiModelProperty(value = "用户token")
    private String token;

    @ApiModelProperty(value = "友盟token")
    private String deviceToken;
    @ApiModelProperty(value = "友盟来源（1：安卓，2：ios）")
    private Integer umengSource;

}
