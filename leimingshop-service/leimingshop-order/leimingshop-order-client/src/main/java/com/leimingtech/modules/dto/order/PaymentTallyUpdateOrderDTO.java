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
 * @Author: 刘远杰
 * @Description: 订单流水更新订单实体
 * @Date :2019/6/18 11:26
 * @Version V1.0
 **/
@Data
@ApiModel(description = "PaymentTallyUpdateOrderDTO")
public class PaymentTallyUpdateOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;
    @ApiModelProperty(value = "店铺id")
    private Long storeId;
    @ApiModelProperty(value = "订单编号（支付记录为父订单编号，退款记录为子订单编号）")
    private Long orderSn;
    @ApiModelProperty(value = "退款编号")
    private Long aftersaleSn;
    @ApiModelProperty(value = "收支标识（0:收入，1:支出）")
    private Integer transactionStatus;
    @ApiModelProperty(value = "支付方式id")
    private Long paymentId;
    @ApiModelProperty(value = "支付方式代码")
    private String paymentCode;
    @ApiModelProperty(value = "支付方式名称")
    private String paymentName;
    @ApiModelProperty(value = "支付状态:1,成功;2,失败;")
    private Integer paymentStatus;
    @ApiModelProperty(value = "交易编号(商城内部交易单号)")
    private Long paymentSn;
    @ApiModelProperty(value = "1:PC;2:APP;3:h5")
    private Integer paymentFrom;
    @ApiModelProperty(value = "交易金额")
    private BigDecimal paymentAmount;
    @ApiModelProperty(value = "交易类型:10,订单支付;20,充值,30退款")
    private Integer tradeType;
    @ApiModelProperty(value = "交易流水号")
    private String tradeSn;
    @ApiModelProperty(value = "买家id")
    private Long buyerId;
    @ApiModelProperty(value = "买家名称")
    private String buyerName;
    @ApiModelProperty(value = "交易备注")
    private String paymentRemarks;

}