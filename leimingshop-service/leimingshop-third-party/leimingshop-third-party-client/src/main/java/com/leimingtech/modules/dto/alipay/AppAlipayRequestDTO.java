/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * @author zhangtai
 * @date 2020/4/3 15:10
 * @Description:
 */
@Data
@ToString
@ApiModel(description = "AppAlipayRequestDTO")
public class AppAlipayRequestDTO {

    @ApiModelProperty("对一笔交易的具体描述信息")
    private String body;

    @ApiModelProperty("商品的标题/交易标题/订单标题/订单关键字等")
    private String subject;

    @ApiModelProperty("商户网站唯一订单号")
    private String outTradeNo;

    @ApiModelProperty("订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]")
    private BigDecimal totalAmount;
}
