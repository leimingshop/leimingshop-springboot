/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.invoice;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * 发票服务响应状态码
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
public class InvoiceStatusCode extends ServiceStatusCode {

    /**
     * 获取商品信息异常
     */
    public static final ServiceStatusCode INVOICE_OUT_OF_MAX_COUNT = new InvoiceStatusCode("400401", "发票抬头最多设置5个");

    public static final ServiceStatusCode INVOICE_COMPANY_NAME_REPEAT = new InvoiceStatusCode("400402", "该发票抬头已存在");
    public static final ServiceStatusCode PARAMETER_FAIL = new InvoiceStatusCode("400400", "提交数据有误");

    protected InvoiceStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }
}
