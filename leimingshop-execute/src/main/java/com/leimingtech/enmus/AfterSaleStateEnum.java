/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enmus;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:售后状态枚举
 * @Date: 2019/6/18 17:46
 * @Version: V1.0
 */
public enum AfterSaleStateEnum {


    //售后类型0:退货
    TYPE_RETURN(0),
    //售后类型1:换货
    TYPE_BARTER(1),
    //售后类型2:仅退款
    TYPE_MONEY(2),

    //售后单状态1:用户取消
    AFTER_STATUS_CANCEL(1),
    //售后单状态2:退款失败
    AFTER_STATUS_PAYFAIL(2),
    //售后单状态3:待退货入库
    AFTER_STATUS_RETURNIN(3),
    //售后单状态4:退款中
    AFTER_STATUS_PAYING(4),
    //售后单状态5:退款成功
    AFTER_STATUS_PAYSUCESS(5),
    //售后单状态6:换货失败
    AFTER_STATUS_BARTERFAIL(6),
    //售后单状态7:待换货入库
    AFTER_STATUS_BARTERIN(7),
    //售后单状态8:换货出库中
    AFTER_STATUS_BARTEROUT(8),
    //售后单状态1:换货成功
    AFTER_STATUS_BARTERSUCESS(9),
    //售后物流状态1为买家待发货
    LOGISTICS_STATUS_BUYEROUT(1),
    //售后物流状态1为卖家待收货
    LOGISTICS_STATUS_SELLERWAITIN(2),
    //售后物流状态1为卖家已收货
    LOGISTICS_STATUS_SELLERIN(3),
    //售后物流状态1为买家待收货
    LOGISTICS_STATUS_BUYERWAITIN(4),
    //售后物流状态1为已完成
    LOGISTICS_STATUS_END(5),
    //日志流转
    LOG_START(0),
    //1:申请审核中
    LOG_AUDIT(1),
    //2:用户取消
    LOG_CANCEL(2),
    //3:售后收货
    LOG_IN(3),
    //4:换货出库
    LOG_BARTOUT(4),
    //5:进行退款
    LOG_PAY(5),
    //6:处理完成
    LOG_END(6),

    //退款记录表退款状态
    //1:待审核,2:待退款,3:退款中,4:退款成功,5:退款失败
    REFUND_STATE_WAIT_PAY_AUDIT(1),
    REFUND_STATE_PAY_PREPARE(2),
    REFUND_STATE_PAYING(3),
    REFUND_STATE_PAY_SUCCESS(4),
    REFUND_STATE_PAY_FAIL(5);


    private Integer value;


    AfterSaleStateEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
