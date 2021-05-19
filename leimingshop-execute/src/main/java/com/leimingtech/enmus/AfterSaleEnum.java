/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enmus;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:售后设置枚举
 * @Date: 2019/6/18 17:46
 * @Version: V1.0
 */
public enum AfterSaleEnum {

    //默认值
    DEFAULT(0),

    //默认值
    DEFAULTONE(1),

    //申请数量默认值
    APPLYNUMDEFAULT(1),

    //0:退货,1:换货
    TYPERETURN(0),

    //1:换货
    TYPEBARTER(1),

    //审核状态1:商家审核中
    SELLERAUDITING(1),

    //审核状态2:平台审核中
    ADMINAUDITING(2),

    //审核状态3:已完成
    FINISH(3),

    //审核状态4:已取消
    CANCEL(4),

    //审核结果（0:未审核）
    NOAUDIT(0),

    //审核结果（1:审核通过）
    RESULT_PROCESS(1),

    //审核结果（2:审核不通过）
    RESULT_REFUSE(2),

    //审核结果（3审核中）
    RESULT_REVIEW(3),

    //审核结果（4:用户取消）
    RESULT_CANCEL(4),
    //审核流程1:商家审核
    SELLER_AUDIT(1),
    //审核流程2:平台审核
    ADMIN_AUDIT(2);

    private Integer value;


    AfterSaleEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
