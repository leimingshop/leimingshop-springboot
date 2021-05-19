/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 交易统计
 *
 * @author xuzhch
 * @date 2020年9月17日
 */
@Data
public class TransactionStatisDTO implements Serializable {
    @Id
    private Long id;

    @ApiModelProperty(value = "店铺ID")
    private Long storeId;

    @ApiModelProperty(value = "浏览量")
    private Integer pageView;

    @ApiModelProperty(value = "访客数")
    private Integer visitorsNumber;

    @ApiModelProperty(value = "用户Id")
    private Long memberId;

    @ApiModelProperty(value = "下单人数")
    private Integer submitOrderNumber;

    @ApiModelProperty(value = "订单数")
    private Integer orderNumber;

    @ApiModelProperty(value = "下单件数")
    private Integer orderGoodsNumber;

    @ApiModelProperty(value = "下单金额")
    private BigDecimal submitOrderAmount;

    @ApiModelProperty(value = "退款金额")
    private BigDecimal refundAmount;

    @ApiModelProperty(value = "付款人数")
    private Integer paymentNumber;

    @ApiModelProperty(value = "付款订单数")
    private Integer paymentOrderNumber;

    @ApiModelProperty(value = "付款件数")
    private Integer paymentGoodsNumber;

    @ApiModelProperty(value = "付款金额")
    private BigDecimal paymentAmount;

    @ApiModelProperty(value = "客单价")
    private BigDecimal perTicketSales;

    @ApiModelProperty(value = "订单状态")
    private Integer orderStatus;

    @ApiModelProperty(value = "支付状态")
    private Integer paymentStatus;

    @ApiModelProperty(value = "创建时间（日）")
    private Date createDayTime;

    @ApiModelProperty(value = "创建时间（月）")
    private Date createMonthTime;
}
