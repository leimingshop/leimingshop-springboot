/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * admin订单优惠券明细
 *
 * @author 刘远杰
 * @since v1.0.0 2020/02/24
 */
@Data
@ApiModel(description = "AdminOrderCouponsDTO")
public class AdminOrderCouponsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "优惠券活动名称")
    private String activityName;

    @ApiModelProperty(value = "活动范围 0平台 1店铺")
    private Integer activityScope;

    @ApiModelProperty(value = "优惠券类型 0满减券 1满折券")
    private Integer couponsType;

    @ApiModelProperty(value = "使用限制金额")
    private BigDecimal limitAmount;

    @ApiModelProperty(value = "优惠券面额")
    private BigDecimal faceValue;

    @ApiModelProperty(value = "优惠券总数")
    private Long totalNum;

}
