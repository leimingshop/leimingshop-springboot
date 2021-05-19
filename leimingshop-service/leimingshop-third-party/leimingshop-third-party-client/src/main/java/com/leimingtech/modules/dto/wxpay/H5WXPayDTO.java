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
 * 功能描述：
 * <h5根据交易单信息生成预支付订单>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/12 15:25
 **/
@Data
@ApiModel(description = "H5WXPayDTO")
public class H5WXPayDTO {

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


//    @ApiModelProperty("编码字符集")
//    @NotBlank(message = "编码字符集不能为空", groups = DefaultGroup.class)
//    private String enc;

}
