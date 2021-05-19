/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.math.BigDecimal;


/**
 * @Description pc支付宝支付请求入参
 * @Author huangkeyuan
 * @Date 10:13 2019-12-10
 * @return
 */
@Data
@ToString
@ApiModel(description = "H5AliPayRequestDTO")
public class PcAliPayRequestDTO implements Serializable {

    @ApiModelProperty("对一笔交易的具体描述信息")
    private String body;

    @ApiModelProperty("商品的标题/交易标题/订单标题/订单关键字等")
    private String subject;

    @ApiModelProperty("商户网站唯一订单号")
    private String outTradeNo;

    @ApiModelProperty("订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]")
    private BigDecimal totalAmount;

    @ApiModelProperty("销售产品码，商家和支付宝签约的产品码。wap支付：QUICK_WAP_WAY; app支付：QUICK_MSECURITY_PAY;pc支付：FAST_INSTANT_TRADE_PAY")
    private String productCode;

    @ApiModelProperty("支付完成后的回调链接")
    private String returnUrl;

}
