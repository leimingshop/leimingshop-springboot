/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.invoice;

/**
 * 订单发票枚举类
 *
 * @author xuzhch
 * @date 2020年10月13日
 */
public enum OrderInvoiceEnum {

    /**
     * 抬头类型（默认0：不开票、1：个人、2：单位）
     */
    INVOICE_NO(0),
    INVOICE_PERSONAL(1),
    INVOICE_COMPANY(2),
    /**
     * 发票类型(1：电子、2：纸质、3：增值税)
     */
    INVOICE_TYPE_ELECTRONIC(1),
    INVOICE_TYPE_PAPER(2),
    INVOICE_TYPE_VAT(3),
    /**
     * 发票内容（1：商品明细、2：商品类别）
     */
    INVOICE_CONTENT_DETAIL(1),
    INVOICE_CONTENT_CLASS(2),;

    private int value;

    OrderInvoiceEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
