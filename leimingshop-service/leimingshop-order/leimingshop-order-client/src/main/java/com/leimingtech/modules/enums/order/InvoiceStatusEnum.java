/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;


/**
 * 功能描述：
 * <发票状态>
 *
 * @author 黄佳能
 * @version 1.0
 * @Date 2020/3/31
 **/

public enum InvoiceStatusEnum {
    //开票进度（1：待开票、2：已开票、3：待换开、4：已换开
    INVOCIE_STATUS_NO(1),
    //开票进度（1：待开票、2：已开票、3：待换开、4：已换开
    INVOCIE_STATUS_YES(2),
    //开票进度（1：待开票、2：已开票、3：待换开、4：已换开
    INVOCIE_STATUS_TO_BE_CHANGE(3),
    //开票进度（1：待开票、2：已开票、3：待换开、4：已换开
    INVOCIE_STATUS_HAS_CHANGE(4),


    //是否换开（默认0：未换开、1：换开
    IS_CHANGEFLAG_NO(0),
    //是否换开（默认0：未换开、1：换开
    IS_CHANGEFLAG_YES(1),

    //用户提交抬头类型（默认0：不开票、1：个人、2：单位）
    COMPANY_TYPE_N0(0),
    COMPANY_TYPE_PERSON(1),
    COMPANY_TYPE_COMPANY(2),

    //发票内容（1：商品明细、2：商品类别）
    INVOCIE_CONTENT_DETAIL(1),
    //发票内容（1：商品明细、2：商品类别）
    INVOCIE_CONTENT_CLASS(2),

    //开具发票类型(1：电子、2：纸质、3：增值税)
    INVOCIE_TYPE_ELECTRONICS(1),
    //开具发票类型(1：电子、2：纸质、3：增值税)
    INVOCIE_TYPE_PAPER(2),
    //开具发票类型(1：电子、2：纸质、3：增值税)
    INVOCIE_TYPE_VAT(3),

    //是否经过售后（0:未申请）
    AFTER_NOT_APPLY(0),
    //是否经过售后（1:售后）
    AFTER_APPLY(1),
    //是否经过售后（2:申请被拒绝）
    AFTER_REJECT(2);


    private Integer value;


    InvoiceStatusEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
