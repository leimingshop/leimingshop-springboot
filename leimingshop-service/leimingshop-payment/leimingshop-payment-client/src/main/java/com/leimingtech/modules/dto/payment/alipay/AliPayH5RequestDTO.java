/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.alipay;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import com.leimingtech.modules.dto.payment.OrderPayDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Description 支付宝支付请求参数
 * @Author huangkeyuan
 * @Date 10:49 2019-12-10
 * @return
 */
@Data
@ApiModel(description = "AliPayH5RequestDTO")
public class AliPayH5RequestDTO implements Serializable {

    @ApiModelProperty("支付单号")
    @NotNull(message = "支付单号不能为空", groups = AddGroup.class)
    private String paySn;

    @ApiModelProperty("交易类型 h5 或者 app")
    @NotNull(message = "交易类型", groups = AddGroup.class)
    private String tradeType;

    @ApiModelProperty("订单id")
    @NotNull(message = "订单id", groups = AddGroup.class)
    private String orderId;

    @ApiModelProperty("订单支付实体")
    private OrderPayDTO orderPayDTO;
}
