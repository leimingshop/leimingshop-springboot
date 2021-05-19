/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import com.leimingtech.commons.tools.validator.group.UpdateGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName MemberUpdateDTO
 * @Description 会员修改实体
 * @Author DY
 * @Date 2019/5/15 17:26
 * @Version 1.0
 **/
@Data
@ApiModel(description = "MemberUpdateDTO")
public class MemberUpdateDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    @ApiModelProperty(value = "用户ID")
    @NotNull(message = "{id.require}", groups = UpdateGroup.class)
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
     * 用户密码
     */
    @ApiModelProperty(value = "用户密码")
    @NotBlank(message = "{sysuser.password.require}")
    private String memberPasswd;

    /**
     * 省份ID
     */
    @ApiModelProperty(value = "省份ID")
    private String memberProvinceid;

    /**
     * 城市ID
     */
    @ApiModelProperty(value = "城市ID")
    private String memberCityid;

    /**
     * 地区ID
     */
    @ApiModelProperty(value = "地区ID")
    private String memberAreaid;

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
    @ApiModelProperty(value = "用户可用积分")
    private Integer availablePoint;

    @ApiModelProperty(value = "用户等级积分")
    private Integer gradePoint;

    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;

    @ApiModelProperty(value = "冻结余额")
    private BigDecimal blockedBalance;

    @ApiModelProperty(value = "可提现余额")
    private BigDecimal availableWithdrawal;
}
