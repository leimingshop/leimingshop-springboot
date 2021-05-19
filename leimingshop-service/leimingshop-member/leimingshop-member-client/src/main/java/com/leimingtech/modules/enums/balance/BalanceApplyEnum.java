/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.balance;


/**
 * 余额提现枚举类
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-07-08 18:32
 **/
public enum BalanceApplyEnum {

    /**
     * 申请过
     */
    APPLYED(1),

    /**
     * 未申请过
     */
    UNAPPLYED(0);

    private int value;

    BalanceApplyEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
