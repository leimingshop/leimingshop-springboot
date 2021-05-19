/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.alipay;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author zhangtai
 * @date 2020/4/2 15:03
 * @Description:
 */
@Data
@ApiModel(description = "AliPayAppRequestDTO（pc和app共用一个）")
public class AliPayAppReqestDTO {
    @ApiModelProperty("支付单号")
    @NotNull(message = "支付单号不能为空", groups = AddGroup.class)
    private String paySn;

    @ApiModelProperty("订单Id")
    @NotNull(message = "订单Id", groups = AddGroup.class)
    private String orderId;
}
