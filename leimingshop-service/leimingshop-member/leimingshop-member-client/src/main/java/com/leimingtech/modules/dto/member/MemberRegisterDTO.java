/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;

/**
 * @ClassName MemberRegisterDTO
 * @Description 会员注册实体
 * @Author DY
 * @Date 2019/5/15 14:59
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MemberRegisterDTO")
public class MemberRegisterDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "{sysuser.username.require}")
    private String memberName;
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    @NotBlank(message = "手机号必填")
    private String memberMobile;
    /**
     * 用户邮箱
     */
    @ApiModelProperty(value = "用户邮箱")
    @NotBlank(message = "邮箱必填")
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
     * 用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）
     */
    @ApiModelProperty(value = "0:pc,1:h5,2:android,3:ios,4:微信,5:小程序")
    private Integer memberSource;
    /**
     * 用户性别（默认0:保密，1：女，2：男）
     */
    @ApiModelProperty(value = "用户性别 （默认0:保密，1：女，2：男）")
    private Integer memberSex;
    /**
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String memberPasswd;
    /**
     * 用户生日
     */
    @ApiModelProperty(value = "用户生日")
    private Date memberBirthday;

    /**
     * 支付密码
     */
    @ApiModelProperty(value = "支付密码")
    private String paymentPasswd;

    /**
     * 微信openId
     */
    private String wechatOpenid;

    /**
     * 当且仅当该网站应用已获得该用户的userinfo授权时
     */
    private String wechatUnionid;


}
