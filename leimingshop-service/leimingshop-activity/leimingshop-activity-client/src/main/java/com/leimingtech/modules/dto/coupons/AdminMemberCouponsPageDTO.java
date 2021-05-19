/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.coupons;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 后台会员优惠券分页实体
 *
 * @author 刘远杰 2634443725@qq.com
 * @since v1.0.0 2019-12-05
 */
@Data
@ApiModel(description = "AdminMemberCouponsPageDTO")
public class AdminMemberCouponsPageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    private Long id;

    @ApiModelProperty(value = "优惠券活动id")
    private Long activityId;

    @ApiModelProperty(value = "会员id")
    private Long memberId;

    @ApiModelProperty(value = "会员名称")
    private String memberName;

    @ApiModelProperty(value = "优惠券状态 未使用(0不可使用 1可使用) 2已使用 3已过期")
    private Integer couponsState;

    @ApiModelProperty(value = "使用开始时间")
    private Date startDate;

    @ApiModelProperty(value = "使用结束时间")
    private Date endDate;

    @ApiModelProperty(value = "使用时间")
    private Date useDate;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单状态")
    private Long orderStatus;

    @ApiModelProperty(value = "商品总价格")
    private BigDecimal goodsAmount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal orderAmount;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

}
