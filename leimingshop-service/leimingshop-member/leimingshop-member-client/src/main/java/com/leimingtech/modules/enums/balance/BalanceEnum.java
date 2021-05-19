/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.balance;


/**
 * 余额枚举类
 *
 * @author huangkeyuan@leimingtech.com
 * @return
 * @date 2020-07-08 18:32
 **/
public enum BalanceEnum {

    /**
     * 运营端调整
     */
    ADMIN_CHANGE(1),

    /**
     * 用户购买下单
     */
    MEMBER_ORDER(2),

    /**
     * 订单退款
     */
    ORDER_REFUND(3),

    /**
     * 用户充值
     */
    MEMBER_RECHARGE(4),

    /**
     * 商家返利
     */
    STORE_RETURN(5),

    /**
     * 商家返利
     */
    MEMBER_WITHDRAW(6);

    private int value;

    BalanceEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
