/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * <支付模块日志码>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/3
 */
public class ThirdTransferStatusCode extends ServiceStatusCode {

    /**
     * 根据支付单号提交微信预支付失败,获取微信预支付id失败
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_NO_PREPAYID = new ThirdTransferStatusCode("500903", "根据支付单号提交微信预支付失败,获取微信预支付id失败");

    protected ThirdTransferStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
