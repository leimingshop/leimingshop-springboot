/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.coupons;

/**
 * 优惠券相关枚举类
 *
 * @author lixiang
 * @version V1.0
 * @date 2019/7/15 14:34
 **/
public enum CouponsEnum {

    /**
     * 领取方式：主动领取
     */
    RECEIVE_TYPE_INITIATIVE(0),

    /**
     * 领取方式：后台赠送
     */
    RECEIVE_TYPE_GIFT(1),

    /**
     * 优惠券使用状态：未使用(不可使用)
     */
    COUPON_CANNT_USE(0),

    /**
     * 优惠券使用状态：未使用(可使用)
     */
    COUPON_CAN_USE(1),

    /**
     * 优惠券使用状态：已使用
     */
    COUPONS_USED(2),

    /**
     * 优惠券使用状态：失效
     */
    COUPONS_INVALID(3),

    /**
     * 有效期类型 0固定时间
     */
    VALIDITY_TYPE_RANGE(0),

    /**
     * 有效期类型 1有效天数
     */
    VALIDITY_TYPE_DAYS(1),

    /**
     * 活动场景 0普通券
     */
    ACTIVITY_SCENE_NORMAL(0),

    /**
     * 活动场景 1新人券
     */
    ACTIVITY_SCENE_NEW(1),

    /**
     * 活动范围 0平台
     */
    ACTIVITY_SCOPE_PLATFORM(0),

    /**
     * 活动范围 1店铺
     */
    ACTIVITY_SCOPE_STORE(1),

    /**
     * 优惠券类型 0满减券
     */
    COUPONS_TYPE_REDUCE(0),

    /**
     * 优惠券类型 1满折券
     */
    COUPONS_TYPE_DISCOUNT(1),

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
    ACTIVITY_STATE_END(2),
    /**
     * 是否与其他活动共享 0不共享
     */
    SHARE_FLAG_NO(0),

    /**
     * 是否与其他活动共享 1共享
     */
    SHARE_FLAG_YES(1),

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
    ACTIVITY_GOODS_SCOPE_BRAND(3),

    /**
     * 可凑单
     */
    ADD_FLAG_YES(0),

    /**
     * 未到达使用时间
     */
    ADD_FLAG_TIME(1),

    /**
     * 不满足使用范围
     */
    ADD_FLAG_SCOPE(2),

    /**
     * 未选择
     */
    SELECT_FLAG_NO(0),

    /**
     * 已选择
     */
    SELECT_FLAG_YES(1),;

    private int value;

    CouponsEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
