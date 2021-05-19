/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.dto.payment.wechat;

import lombok.Data;

import java.io.Serializable;

/**
 * PC统一下单返回实体
 *
 * @author lixiangx@leimingtech.com
 * @date 2020/5/19 19:52
 **/
@Data
public class PCPayResponseDTO implements Serializable {


    private static final long serialVersionUID = 1839113145644549650L;

    /**
     * trade_type=NATIVE时有返回，此url用于生成支付二维码，然后提供给用户进行扫码支付。
     */
    private String codeUrl;

    /**
     * 订单编号
     */
    private Long orderSn;

    /**
     * 订单支付金额
     */
    private String orderPrice;


    /**
     * trade_type=NATIVE时有返回，codeUrl用于生成支付二维码，然后提供给用户进行扫码支付。
     * 支付二维码codeUrl转化为二维码的base64
     */
    private String payCodeBase64;

    /**
     * 0未支付 1已支付(只有第三方支付接口通知到时才会更改此状态) 2已取消
     */
    private Integer payState;
}
