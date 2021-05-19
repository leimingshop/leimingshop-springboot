/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Author: SWH ab4856812@163.com
 * @Description:微信退款基础实体
 * @Date: 2019/6/22 14:56
 * @Version: V1.0
 */
@Data
@ToString
@ApiModel(description = "H5WxRefundBaseDTO")
public class H5WxRefundBaseDTO implements Serializable {

    @ApiModelProperty("主键id")
    private Long id;

    @ApiModelProperty("退款单号，也叫微信支付单号，每次必须退款的时候每次必须不一致")
    private String outrefundno;

    @ApiModelProperty("支付单号")
    private String outtradeno;

    @ApiModelProperty("订单号")
    private String ordersn;

    @ApiModelProperty(value = "订单ID")
    private Long orderId;

    @ApiModelProperty("支付方式")
    private Integer payType;

    @ApiModelProperty("总金额(分)")
    private Integer totalfee;

    @ApiModelProperty("退款金额(分)")
    private Integer refundfee;

    @ApiModelProperty(value = "店铺id")
    private Long storeId;

    @ApiModelProperty("店铺名称")
    private String storeName;

    @ApiModelProperty(value = "买家id")
    private Long buyerId;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "余额退款金额")
    private BigDecimal balanceRefundAmount;

}
