/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;


/**
 * 充值金额
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-10-26 14:56
 **/
@Data
@ApiModel(description = "RechargePayDTO")
public class RechargePayDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 用户来源（默认0:网站注册）
     */
    @ApiModelProperty(value = "用户来源平台（默认0:pc,1:h5,2:android,3:ios,4:微信,5:小程序）")
    @NotNull(message = "用户来源平台不能为空", groups = AddGroup.class)
    private Integer memberSource;

    @ApiModelProperty(value = "充值金额")
    @NotNull(message = "充值金额不能为空", groups = AddGroup.class)
    private BigDecimal rechargeAmount;

    @ApiModelProperty("交易类型 JSAPI(微信浏览器内) MWEB(非微信浏览器) NATIVE(APP）wxMiniProgram(微信小程序)")
    @NotNull(message = "交易类型不能为空", groups = AddGroup.class)
    private String tradeType;

    @ApiModelProperty("openId")
    private String openId;

    @ApiModelProperty("微信小程序支付传code值，不传openid值")
    private String code;


}
