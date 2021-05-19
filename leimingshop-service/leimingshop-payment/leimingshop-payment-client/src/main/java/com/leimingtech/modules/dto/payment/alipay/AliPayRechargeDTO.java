/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 支付宝充值
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-10-26 14:56
 **/
@Data
@ApiModel(description = "AliPayRechargeDTO")
public class AliPayRechargeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

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

}
