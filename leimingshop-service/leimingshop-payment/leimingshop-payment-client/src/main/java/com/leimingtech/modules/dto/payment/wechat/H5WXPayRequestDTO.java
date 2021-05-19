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
 * 功能描述：
 * <H5生成预支付订单请求参数实体>
 *
 * @author 刘远杰
 * @version 7.0
 * @Date 2019/5/24 16:01
 **/
@Data
@ApiModel(description = "H5WXPayRequestDTO")
public class H5WXPayRequestDTO implements Serializable {


    @ApiModelProperty("支付单号")
    @NotNull(message = "支付单号不能为空", groups = AddGroup.class)
    private Long paySn;

    @ApiModelProperty("交易类型 JSAPI(微信浏览器内) MWEB(非微信浏览器) NATIVE APP")
    @NotNull(message = "交易类型不能为空", groups = AddGroup.class)
    private String tradeType;

    @ApiModelProperty("openId 交易类型为JSAPI(微信浏览器内)时，必传")
    private String openId;
//
//    @ApiModelProperty("微信小程序支付传code值，不传openid值")
//    private String code;

}
