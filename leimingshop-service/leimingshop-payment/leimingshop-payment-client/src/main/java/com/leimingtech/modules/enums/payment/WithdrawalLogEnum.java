/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.payment;

/**
 * 提现审核审核状态日志对应的枚举类
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-10-20 17:33
 **/
public enum WithdrawalLogEnum {

    /**
     * 1提现申请中
     */
    SUBMIT(1),

    /**
     * 2申请取消
     */
    CANCEL(2),

    /**
     * 3提现申请驳回
     */
    REFUSE_AUDITING(3),

    /**
     * 4提现审核通过发放申请中
     */
    PASS_ISSUEING(4),

    /**
     * 5发放审核(已驳回)
     */
    REFUSE_ISSUE(5),

    /**
     * 6发放通过
     */
    FINISH(6);

    private int value;

    WithdrawalLogEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
