/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.member;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 会员详细信息表
 *
 * @author DY
 * @email 1197793912@qq.com
 * @since 1.0.0 2019-05-10
 */
@Data
@ApiModel(description = "MemberInfoDTO")
public class MemberInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * id
     */
    @ApiModelProperty(value = "主键")
    private Long id;
    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID")
    private Long memberId;
    /**
     * 支付密码
     */
    @ApiModelProperty(value = "支付密码")
    private String paymentPasswd;
    /**
     * 可用余额
     */
    @ApiModelProperty(value = "可用余额")
    private BigDecimal availableBalance;
    /**
     * 冻结余额
     */
    @ApiModelProperty(value = "冻结余额")
    private BigDecimal blockedBalance;
    /**
     * 用户消费积分
     */
    @ApiModelProperty(value = "用户消费积分")
    private Integer consumePoint;
    /**
     * 用户可用积分
     */
    @ApiModelProperty(value = "用户可用积分")
    private Integer availablePoint;
    /**
     * 用户等级积分
     */
    @ApiModelProperty(value = "用户等级积分")
    private Integer gradePoint;

    /**
     * 用户等级
     */
    @ApiModelProperty(value = "用户等级")
    private String gradeName;
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