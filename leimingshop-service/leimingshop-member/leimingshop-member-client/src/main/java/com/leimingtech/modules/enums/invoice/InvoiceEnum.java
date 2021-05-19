/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.invoice;


/**
 * @Author: weixianchun
 * @Description: 发票状态枚举
 * @Date :2019/5/24 18:09
 * @Version V1.0
 **/
public enum InvoiceEnum {

    /**
     * 个人抬头不能为空
     */
    PERSONAL_INV(301),

    /**
     * 单位抬头不能为空
     */
    COMPANY_INV(302),

    /**
     * 发票类型（1:普通发票）
     */
    GENERAL_INVOICE(1),

    /**
     * 发票类型（2:增值税发票）
     */
    VAT_INVOICE(2),

    /**
     * 发票类型（3:电子发票）
     */
    ELECTRONIC_INVOICE(3),

    /**
     * 审核状态（0未审核）
     */
    AUDIT_STATUS_NO(0),

    /**
     * 审核状态（1审核通过）
     */
    AUDIT_STATUS_YES(1),

    /**
     * 审核状态（2审核未通过）
     */
    AUDIT_STATUS_REFUSE(2),

    /**
     * 审核状态（3审核中）
     */
    AUDIT_STATUS_ING(3),

    /**
     * 设为默认（默认0:否）
     */
    IS_DEFULT_NO(0),

    /**
     * 设为默认（1:是）
     */
    IS_DEFULT_YES(1),

    /**
     * 普通发票类型 :1 个人发票
     */
    PERSONAL_INVOICE(1),

    /**
     * 普通发票类型 :2单位发票
     */
    COMPANY_INVOICE(2);

    private int value;

    InvoiceEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
