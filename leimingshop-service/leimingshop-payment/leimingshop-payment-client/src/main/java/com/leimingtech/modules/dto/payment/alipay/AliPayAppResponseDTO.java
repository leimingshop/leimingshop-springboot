/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.alipay;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author zhangtai
 * @date 2020/4/3 18:12
 * @Description:
 */
@Data
@ApiModel(description = "AliPayAppResponseDTO")
public class AliPayAppResponseDTO {

    @ApiModelProperty("返回状态码")
    private Integer code;

    @ApiModelProperty("返回前端提示信息")
    private String msg;

    @ApiModelProperty("支付宝返回信息")
    private String orderString;
}
