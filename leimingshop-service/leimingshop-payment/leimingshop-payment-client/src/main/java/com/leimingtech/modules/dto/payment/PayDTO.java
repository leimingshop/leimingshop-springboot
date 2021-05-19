/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 功能描述：
 * <订单支付情况实体>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/21 14:04
 **/
@Data
@ApiModel(description = "PayDTO")
public class PayDTO {

    @ApiModelProperty("支付单号")
    private Long paySn;

    @ApiModelProperty("支付金额")
    private BigDecimal payAmount;

    @ApiModelProperty("支付描述")
    private String beWrite;

    @ApiModelProperty("支付订单状态 0:已取消;10:待付款;20:待发货;30:待收货;40:交易完成;")
    private Integer orderStatus;

    @ApiModelProperty("支付状态:0:未支付,1:已支付")
    private Integer payStatus;

    @ApiModelProperty("微信支付商品信息body")
    private String body;

}
