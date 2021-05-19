/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 功能描述：
 * <h5根据订单信息生成预支付订单>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/12 15:46
 **/
@Data
@ApiModel(description = "H5OrderWXPayDTO")
public class H5OrderWXPayDTO {

    @ApiModelProperty("订单id")
    @NotNull(message = "订单id不能为空", groups = DefaultGroup.class)
    private Long orderId;

    @ApiModelProperty("交易终端类型")
    @NotBlank(message = "交易终端类型不能为空", groups = DefaultGroup.class)
    private String type;

    @ApiModelProperty("支付金额")
    @NotBlank(message = "支付金额不能为空", groups = DefaultGroup.class)
    private String totalFee;

    @ApiModelProperty("商品名称")
    @NotBlank(message = "商品名称不能为空", groups = DefaultGroup.class)
    private String body;

    @ApiModelProperty("微信登陆openId")
    @NotBlank(message = "openId不能为空", groups = DefaultGroup.class)
    private String openId;

}
