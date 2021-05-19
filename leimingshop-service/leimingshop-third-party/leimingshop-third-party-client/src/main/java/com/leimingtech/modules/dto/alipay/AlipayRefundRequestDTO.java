/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @ClassName AlipayRefundRequestDTO
 * @Description
 * @Author huangkeyuan
 * @Date 2019-12-13 16:51
 * @Version 1.0
 */
@Data
@ToString
@ApiModel(description = "AlipayRefundRequestDTO")
public class AlipayRefundRequestDTO implements Serializable {
    @ApiModelProperty("退款单号，也叫微信支付单号，每次必须退款的时候每次必须不一致")
    private String outrefundno;

    @ApiModelProperty("支付单号")
    private String outtradeno;

    @ApiModelProperty("订单号")
    private String ordersn;

    @ApiModelProperty("总金额(元)")
    private String totalfee;

    @ApiModelProperty("退款金额(元)")
    private String refundfee;

}
