/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.constants.wxpay;

/**
 * @author 刘远杰
 * @Description 返回状态码常量类
 * @Date 2019/5/27 16：18
 * Version 7.0
 **/
public class PaymentResultCodeConstants {

    public static final int SUCCESS = 200;

    /**
     * 参数错误
     */
    public static final int ERR_INV_PARAMS = 400;

    /**
     * 权限不足
     */
    public static final int ERR_NO_PER = 403;

    /**
     * 重复
     */
    public static final int ERR_REPEAT = 407;

    /**
     * 查无数据
     */
    public static final int ERR_NO_RESULT = 408;

    /**
     * 代码错误
     */
    public static final int ERR_BADCODE = 500;

    /**
     * 正常支付业务（paySn以1开头）
     */
    public static final String NORMALPAY = "1";

    /**
     * 充值（paySn以6开头）
     */
    public static final String RECHARGEPAY = "6";

}
