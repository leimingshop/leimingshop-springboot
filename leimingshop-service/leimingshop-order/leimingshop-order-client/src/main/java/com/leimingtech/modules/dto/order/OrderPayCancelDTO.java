/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 购物车保存订单返回实体
 * @Date: 15:12 2019/6/21
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "OrderPayCancelDTO")
public class OrderPayCancelDTO implements Serializable {

    @ApiModelProperty(value = "支付id")
    private Long payId;

    @ApiModelProperty(value = "支付单号")
    private Long paySn;

    @ApiModelProperty(value = "买家id")
    private Long buyerId;

    @ApiModelProperty(value = "0默认未支付1已支付(只有第三方支付接口通知到时才会更改此状态)")
    private Integer apiPayState;

    @ApiModelProperty(value = "支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "订单状态(单个订单的时候使用,多个订单为空值)")
    private Integer orderState;

    @ApiModelProperty(value = "订单总额")
    private BigDecimal orderTotalPrice;

    @ApiModelProperty(value = "订单id")
    private Long orderId;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "需要支付金额  用来判断是否是货到付款的")
    private BigDecimal amount;
}
