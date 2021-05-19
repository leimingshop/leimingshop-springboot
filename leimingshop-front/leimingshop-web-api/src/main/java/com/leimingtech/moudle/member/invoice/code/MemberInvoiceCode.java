/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.invoice.code;

/**
 * 用户发票信息入参日志码
 *
 * @author xuzhch
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface MemberInvoiceCode {

    /**
     * 用户发票列表
     */
    String MEMBER_INVOICE_LIST_CODE = "200601";
    String MEMBER_INVOICE_LIST_DESC = "访问用户发票列表";


    /**
     * 用户保存、修改发票信息
     */
    String MEMBER_INVOICE_SAVE_UPDATE_CODE = "200602";
    String MEMBER_INVOICE_SAVE_UPDATE_DESC = "访问保存或修改发票";

    /**
     * 用户删除发票信息
     */
    String MEMBER_INVOICE_DELETE_CODE = "200603";
    String MEMBER_INVOICE_DELETE_DESC = "访问用户删除发票信息";
    /**
     * 访问用户发票详情
     */
    String MEMBER_INVOICE_DETAIL_CODE = "200604";
    String MEMBER_INVOICE_DETAIL_DESC = "访问用户发票详情";
    /**
     * 访问获取用户默认发票详情
     */
    String MEMBER_INVOICE_DEFAULT_CODE = "200605";
    String MEMBER_INVOICE_DEFAULT_DESC = "访问获取用户默认发票详情";
    /**
     * 访问设置默认发票信息
     */
    String MEMBER_INVOICE_SETTING_DEFAULT_CODE = "200606";
    String MEMBER_INVOICE_SETTING_DEFAULT_DESC = "访问设置默认发票信息";

}
