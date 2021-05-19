/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.wxpay;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhangtai
 * @date 2020/4/7 14:14
 * @Description:
 */
@Data
@ApiModel(description = "AppWXPayDTO")
public class AppWXPayDTO {

    @ApiModelProperty("商品名称")
    private String body;

    @ApiModelProperty("交易单号")
    @NotBlank(message = "交易单号不能为空", groups = DefaultGroup.class)
    private String paySn;

    @ApiModelProperty("支付金额")
    private String totalFee;

    @ApiModelProperty("用户ip")
    @NotBlank(message = "ip不能为空", groups = DefaultGroup.class)
    private String ip;
}
