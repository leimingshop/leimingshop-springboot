/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * 活动类型枚举类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/15 14:34
 **/
public enum ActivityTypeEnum {

    /**
     * 无活动
     */
    NO_ACTIVITY(0),

    /**
     * 优惠券活动
     */
    COUPONS_ACTIVITY(1),

    /**
     * 满减活动
     */
    REDUCE_ACTIVITY(2),

    /**
     * 秒杀活动
     */
    SECKILL_ACTIVITY(3),

    /**
     * 其他进入结算
     */
    OTHER_SETTLEMENT(3),

    /**
     * 拼团活动
     */
    GROUP_ACTIVITY(4),
    /**
     * 限时购活动
     */
    FLASH_SALE_ACTIVITY(5),;

    private int value;

    ActivityTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
