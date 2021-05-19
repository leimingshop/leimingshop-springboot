/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

public class InvoiceStatusCode extends ServiceStatusCode {

    /**
     * 订单未完成，不能申请开票
     */
    public static final ServiceStatusCode ORDER_NOT_FINISH = new InvoiceStatusCode("400101", "订单未完成，不能申请开票");
    /**
     * 订单结束超过一个月，无法开票
     */
    public static final ServiceStatusCode ORDER_FINISH_FOR_MONTH = new InvoiceStatusCode("400102", "已过开票时间，无法开票");
    /**
     * 订单完成超过一年，无法申请换开发票
     */
    public static final ServiceStatusCode ORDER_FINISH_FOR_YEAR = new InvoiceStatusCode("400103", "订单完成超过一年，无法申请换开发票");
    /**
     * 订单有退换货无法开票
     */
    public static final ServiceStatusCode ORDER_HAS_AFTER = new InvoiceStatusCode("400104", "该订单有退换货商品，不能申请开票");

    /**
     * 订单有退换货无法开票
     */
    public static final ServiceStatusCode ORDER_GOODS_NOT_INVOICE = new InvoiceStatusCode("400105", "该订单有商品无法开发票");
    public static final ServiceStatusCode EMAIL_SEND_FAIL = new InvoiceStatusCode("400106", "邮件发送失败");
    /**
     * 申请过换开无法再次换开
     */
    public static final ServiceStatusCode ORDER_CHANGE_REPEAT = new InvoiceStatusCode("400107", "已申请过换开，无法再次申请");
    public static final ServiceStatusCode ORDER_INVOICE_CHANGE_FAIL = new InvoiceStatusCode("400108", "换开申请失败，请稍后重试");
    public static final ServiceStatusCode ORDER_INVOICE_ALREADY_APPLY = new InvoiceStatusCode("400109", "已申请过开票，无法再次申请");
    public static final ServiceStatusCode INVOICE_PARAMETER_FAIL = new InvoiceStatusCode("400400", "提交数据有误");

    protected InvoiceStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
