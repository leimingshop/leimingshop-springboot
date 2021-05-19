/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.seckill;

/**
 * <秒杀活动枚举类>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/3/9
 */
public enum SeckillActivityEnum {

    /**
     * 是否与其他活动共享 不共享
     */
    SHARE_FLAG_NO(0),

    /**
     * 是否与其他活动共享 共享
     */
    SHARE_FLAG_YES(1),


    /**
     * 活动状态 0未审核
     */
    AUDIT_STATE_PREPARE(0),

    /**
     * 活动状态 1审核通过
     */
    AUDIT_STATE_PASS(1),

    /**
     * 活动状态 2审核不通过
     */
    AUDIT_STATE_REJECT(2),

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
    ACTIVITY_STATE_END(2);

    private int value;

    SeckillActivityEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
