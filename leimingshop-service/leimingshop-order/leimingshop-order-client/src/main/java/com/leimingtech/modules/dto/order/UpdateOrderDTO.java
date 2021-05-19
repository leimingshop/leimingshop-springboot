/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;


/**
 * @Author: 刘远杰
 * @Description: mq修改订单状态实体
 * @Date :2019/5/20 14:53
 * @Version V1.0
 **/
@Data
@ApiModel(description = "UpdateOrderDTO")
public class UpdateOrderDTO implements Serializable {

    @ApiModelProperty("支付单号")
    private Long paySn;

    @ApiModelProperty("支付方式id")
    private Long paymentId;

    @ApiModelProperty("支付方式名称")
    private String paymentName;

    @ApiModelProperty("支付标识")
    private String paymentCode;

    @ApiModelProperty("第三方交易单号")
    private String transactionId;

}
