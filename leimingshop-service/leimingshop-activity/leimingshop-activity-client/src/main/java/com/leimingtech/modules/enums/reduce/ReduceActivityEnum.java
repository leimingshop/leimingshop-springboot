/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.reduce;

/**
 * 满减活动相关枚举类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/15 14:34
 **/
public enum ReduceActivityEnum {
    /**
     * 优惠券 0不可用
     */
    UNABLE_COUPONS_FLAG(0),
    /**
     * 优惠券 0可用
     */
    ENABLE_COUPONS_FLAG(1),

    /**
     * 活动范围 0平台
     */
    ACTIVITY_SCOPE_PLATFORM(0),

    /**
     * 活动范围 1店铺
     */
    ACTIVITY_SCOPE_STORE(1),

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
     * 活动规则 0普通满减
     */
    PLAIN_RULE_TYPE(0),

    /**
     * 活动规则  1每满减
     */
    EACH_RULE_TYPE(1),

    /**
     * 活动规则 2阶梯满减
     */
    LADDER_RULE_TYPE(2),

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
     * 不是满减商品
     */
    REDUCE_FLAG_NO(0),

    /**
     * 是满减商品
     */
    REDUCE_FLAG_YES(1),

    /**
     * 普通满减
     */
    RULE_TYPE_NORMAL(0),

    /**
     * 每满减
     */
    RULE_TYPE_AVG(1),

    /**
     * 阶梯满减
     */
    RULE_TYPE_LADDER(2),

    /**
     * 未选择
     */
    SELECT_FLAG_NO(0),

    /**
     * 已选择
     */
    SELECT_FLAG_YES(1),

    /**
     * 活动商品范围 0全场通用
     */
    ACTIVITY_GOODS_SCOPE_ALL(0),

    /**
     * 活动商品范围 1指定店铺分类
     */
    ACTIVITY_GOODS_SCOPE_CLASS(1),

    /**
     * 活动商品范围 2指定商品
     */
    ACTIVITY_GOODS_SCOPE_GOODS(2),

    /**
     * 活动商品范围 3指定品牌
     */
    ACTIVITY_GOODS_SCOPE_BRAND(3);

    private int value;

    ReduceActivityEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
