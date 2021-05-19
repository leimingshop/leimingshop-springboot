/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import com.leimingtech.commons.tools.validator.group.AddGroup;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 小程序支付参数实体
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/19 20:43
 **/
@Data
@ApiModel(description = "MiniPayRequestDTO")
public class MiniPayRequestDTO implements Serializable {

    private static final long serialVersionUID = -7520182167155431847L;
    @ApiModelProperty("支付单号")
    @NotNull(message = "支付单号不能为空", groups = AddGroup.class)
    private Long paySn;

    @ApiModelProperty("交易类型 JSAPI(微信浏览器内) MWEB(非微信浏览器) NATIVE(APP) wxMiniProgram(微信小程序)")
    @NotNull(message = "交易类型不能为空", groups = AddGroup.class)
    private String tradeType;

    @ApiModelProperty("微信小程序支付传code值，不传openid值")
    @NotNull(message = "code不能为空", groups = AddGroup.class)
    private String code;

}
