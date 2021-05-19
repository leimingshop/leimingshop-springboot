/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @Author: weixianchun
 * @Description: 订单支付管理
 * @Date :2019/6/18 10:25
 * @Version V1.0
 **/
@Data
@ApiModel(description = "OrderPayDTO")
public class OrderPayDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "消息记录id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private Long orderSn;

    @ApiModelProperty(value = "支付单号")
    private Long paySn;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "第三方支付总金额")
    private BigDecimal payAmount;

    @ApiModelProperty(value = "余额支付金额")
    private BigDecimal balanceAmount;

    @ApiModelProperty(value = "支付总金额")
    private BigDecimal payTotalAmount;

    @ApiModelProperty(value = "第三方交易单号")
    private String transactionId;

    @ApiModelProperty(value = "0默认未支付1已支付(只有第三方支付接口通知到时才会更改此状态)2已取消")
    private Integer payState;

    @ApiModelProperty(value = "创建时间")
    private Date createDate;

    @ApiModelProperty(value = "支付自动取消时间")
    private Date cancalDate;

    @ApiModelProperty(value = "是否拆单（默认0:否,1:是）")
    private Integer isSplit;

}
