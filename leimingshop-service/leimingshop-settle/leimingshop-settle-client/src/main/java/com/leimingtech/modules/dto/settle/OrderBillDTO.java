/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.settle;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单结算表
 *
 * @author chengqian chengqian@leimingtech.com
 * @since v1.0.0 2019-12-27
 */
@Data
@ApiModel(description = "OrderBillDTO")
public class OrderBillDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "结算单编号")
    private Long billTotalId;
    @ApiModelProperty(value = "订单Id")
    private Long orderId;
    @ApiModelProperty(value = "订单编号")
    private Long orderSn;
    @ApiModelProperty(value = "交易时间")
    private Date payTime;
    @ApiModelProperty(value = "订单金额")
    private BigDecimal orderTotals;
    @ApiModelProperty(value = "佣金金额")
    private BigDecimal commisTotals;
    @ApiModelProperty(value = "店铺促销活动费用")
    private BigDecimal storeCostTotals;
    @ApiModelProperty(value = "积分抵扣金额")
    private BigDecimal pointAmount;
    @ApiModelProperty(value = "支付方式")
    private String paymentName;
}