/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.cart;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @Author: 刘远杰
 * @Description: 结算页结算价格刷新实体
 * @Date: 15:23 2019/12/31
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "SettlementPriceDTO")
public class SettlementPriceDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单总金额
     */
    @ApiModelProperty(value = "订单总金额")
    private BigDecimal orderPrice;

    /**
     * 应支付总金额
     */
    @ApiModelProperty(value = "应支付金额")
    private BigDecimal payPrice;

    /**
     * 优惠券金额
     */
    @ApiModelProperty(value = "优惠券金额")
    private BigDecimal couponsPrice;

    /**
     * 活动立减
     */
    @ApiModelProperty(value = "活动立减")
    private BigDecimal reducePrice;

    /**
     * 优惠总金额
     */
    @ApiModelProperty(value = "优惠总金额")
    private BigDecimal activityPrice;

    /**
     * 立即购买商品单价
     */
    @ApiModelProperty(value = "立即购买商品单价")
    private BigDecimal specSellPrice;

}
