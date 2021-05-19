/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.flashsale;

/**
 * 限时抢购活动枚举
 *
 * @author xuzhch
 * @date 2020年10月16日
 */
public enum FlashSaleActivityEnum {

    /**
     * 活动状态 0未开始
     */
    ACTIVITY_STATE_PREPARE(0),

    /**
     * 活动状态 1进行中
     */
    ACTIVITY_STATE_START(1),

    /**
     * 活动状态 2已结束
     */
    ACTIVITY_STATE_END(2),

    /**
     * 不能使用优惠券
     */
    COUPONS_FLAG_NOT(0),
    /**
     * 可以使用优惠券
     */
    COUPONS_FLAG_CAN(1),
    /**
     * 不能使用满减
     */
    REDUCE_FLAG_NOT(0),
    /**
     * 可以使用满减
     */
    REDUCE_FLAG_CAN(1),
    /**
     * 不能使用积分
     */
    POINT_FLAG_NOT(0),
    /**
     * 可以使用积分
     */
    POINT_FLAG_CAN(1),
    /**
     * 不能使用余额
     */
    BALANCE_FLAG_NOT(0),
    /**
     * 可以使用余额
     */
    BALANCE_FLAG_CAN(1),;
    private int value;

    FlashSaleActivityEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
