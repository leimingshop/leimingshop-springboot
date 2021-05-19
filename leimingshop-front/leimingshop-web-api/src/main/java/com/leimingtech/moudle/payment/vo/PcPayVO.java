/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.payment.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * PC端统一下单VO
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/20 13:51
 **/
@Data
@ApiModel("PC端统一下单实体")
public class PcPayVO implements Serializable {

    private static final long serialVersionUID = 1492476063300738927L;

    /**
     * 订单编号
     */
    @ApiModelProperty("订单编号")
    private Long orderSn;

    /**
     * 订单支付金额
     */
    @ApiModelProperty("订单支付金额")
    private String orderPrice;


    /**
     * trade_type=NATIVE时有返回，codeUrl用于生成支付二维码，然后提供给用户进行扫码支付。
     * 支付二维码codeUrl转化为二维码的base64
     */
    @ApiModelProperty("支付二维码base64")
    private String payCodeBase64;

    /**
     * 0未支付 1已支付(只有第三方支付接口通知到时才会更改此状态) 2已取消
     */
    @ApiModelProperty("0未支付 1已支付(只有第三方支付接口通知到时才会更改此状态) 2已取消")
    private Integer payState;
}
