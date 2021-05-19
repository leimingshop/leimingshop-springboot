/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.activity.seckill;

/**
 * <秒杀设置枚举类>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/6
 */
public enum SeckillSettingEnum {

    /**
     * 销售价格显示开关：开
     */
    SELLPRICE_SWITCH_ON(1),

    /**
     * 销售价格显示开关：关
     */
    SELLPRICE_SWITCH_OFF(0),

    /**
     * 秒杀审核开关：开
     */
    AUDIT_SWITCH_ON(1),

    /**
     * 秒杀审核开关：关
     */
    AUDIT_SWITCH_OFF(0);

    private int value;

    SeckillSettingEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
