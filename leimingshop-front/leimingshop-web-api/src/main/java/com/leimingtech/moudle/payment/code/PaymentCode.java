/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.payment.code;

/**
 * 支付方法入参状态码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/20 13:54
 **/
public interface PaymentCode {

    /**
     * PC端微信支付统一下单
     */
    String PC_PAY_ORDER_CODE = "200801";
    String PC_PAY_ORDER_DESC = "PC端微信支付统一下单";

    /**
     * PC端支付宝支付统一下单
     */
    String PC_ALI_PAY_ORDER_CODE = "200802";
    String PC_ALI_PAY_ORDER_DESC = "PC端支付宝支付统一下单";

}
