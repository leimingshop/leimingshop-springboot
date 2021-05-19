/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName MemberListDTO
 * @Description 会员管理列表DTO
 * @Author DY
 * @Date 2019/6/13 14:28
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MemberListDTO")
public class MemberListDTO implements Serializable {
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
    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;

    /**
     * 可用余额
     */
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;
    /**
     * 用户手机号
     */
    @ApiModelProperty(value = "用户手机号")
    private String memberMobile;

    /**
     * 用户头像
     */
    @ApiModelProperty(value = "用户头像")
    private String memberAvatar;

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
     * 用户等级
     */
    @ApiModelProperty(value = "用户等级")
    private String gradeName;

    /**
     * 注册时间
     */
    @ApiModelProperty(value = "注册时间")
    private Date createDate;

    /**
     * 邮箱
     */
    @ApiModelProperty(value = "邮箱")
    private String memberEmail;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名")
    private String memberRealName;

}
