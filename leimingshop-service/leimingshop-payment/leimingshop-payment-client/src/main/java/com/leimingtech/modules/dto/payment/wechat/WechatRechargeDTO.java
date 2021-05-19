/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import com.leimingtech.commons.tools.validator.group.DefaultGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 微信充值
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-10-26 14:56
 **/
@Data
@ApiModel(description = "WechatRechargeDTO")
public class WechatRechargeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "买家ID")
    private Long buyerId;

    @ApiModelProperty(value = "买家名称")
    private String buyerName;

    @ApiModelProperty(value = "商品名称")
    private String goodsName;

    @ApiModelProperty(value = "第三方支付总金额")
    private BigDecimal payAmount;

    @ApiModelProperty("交易单号")
    @NotBlank(message = "交易单号不能为空", groups = DefaultGroup.class)
    private String paySn;

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
    private String openId;

    @ApiModelProperty("用户ip")
    @NotBlank(message = "ip不能为空", groups = DefaultGroup.class)
    private String ip;

    @ApiModelProperty("交易类型 JSAPI(微信浏览器内)、MWEB(非微信浏览器)、NATIVE、APP、wxMiniProgram(微信小程序)")
    @NotBlank(message = "交易类型不能为空", groups = DefaultGroup.class)
    private String tradeType;

    @ApiModelProperty("微信小程序支付传code值，不传openid值")
    private String code;

}
