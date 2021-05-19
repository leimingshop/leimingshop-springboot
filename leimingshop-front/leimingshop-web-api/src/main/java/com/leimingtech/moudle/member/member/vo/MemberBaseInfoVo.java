/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.member.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户基本信息
 *
 * @author xuzhch
 * @date 2020年5月18日22:13:28
 */
@Data
@ApiModel(description = "MemberBaseInfoVo")
public class MemberBaseInfoVo implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 昵称
     */
    @ApiModelProperty(value = "昵称")
    private String nickName;
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
     * 用户生日
     */
    @ApiModelProperty(value = "用户生日")
    private Date memberBirthday;


    /**
     * 地区ID
     */
    @ApiModelProperty(value = "地区ID")
    private String memberAreaid;

    /**
     * 城市ID
     */
    @ApiModelProperty(value = "城市ID")
    private String memberCityid;

    /**
     * 省份ID
     */
    @ApiModelProperty(value = "省份ID")
    private String memberProvinceid;

    /**
     * 街道ID
     */
    @ApiModelProperty(value = "街道ID")
    private String stressId;

    /**
     * 地区内容
     */
    @ApiModelProperty(value = "地区内容")
    private String memberAreainfo;
}
