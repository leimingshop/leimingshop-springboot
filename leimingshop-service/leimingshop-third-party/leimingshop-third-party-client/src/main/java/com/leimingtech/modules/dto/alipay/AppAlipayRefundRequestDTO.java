/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

/**
 * @author zhangtai
 * @date 2020/4/3 17:22
 * @Description:
 */
@Data
@ToString
@ApiModel(description = "AppAlipayRefundRequestDTO")
public class AppAlipayRefundRequestDTO {

    @ApiModelProperty("支付时传入的商户订单号，与 trade_no 必填一个")
    private String outTradeNo;

    @ApiModelProperty("支付时返回的支付宝交易号，与 out_trade_no 必填一个")
    private String tradeNo;

    @ApiModelProperty("本次退款请求流水号，部分退款时必传")
    private String outRequestNo;

    @ApiModelProperty("本次退款金额")
    private String refundAmount;
}
