/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.cart;

/**
 * @Description
 * @Data: 2019/6/14 16:57
 * @Author: chengqian
 * @Version: 1.0
 */
public enum CartEnum {
    /**
     * 未选中
     */
    IS_SELECT_NO(0),
    /**
     * 购物车限制数量
     */
    MAX_CART_NUM(200),
    /**
     * 不可领券
     */
    CANNT_RECEIVED_COUPONS(0),
    /**
     * 可领券
     */
    CAN_RECEIVED_COUPONS(1),
    /**
     * 超过加入购物车最大数量
     */
    CART_NUM_ERROR(430),
    /**
     * 加入购物车数量大于当前库存
     */
    CART_BIG_STORAGE(440),

    /**
     * 已选中
     */
    IS_SELECT_YES(1),

    /**
     * 返回状态值200：成功
     */
    SUCCESS_CODE(200),

    /**
     * 商品不存在
     */
    GOODS_NOT_FOUND(410),

    /**
     * 店铺不存在
     */
    STORE_NOT_FOUND(420),

    /**
     * pc端查询购物车
     */
    PC_CART_LIST(1),
    /**
     * 购物车内商品数量最大999
     */
    CART_GOODS_NUM(999),

    /**
     * 结算时是否使用用户余额
     */
    SETTMENT_NO_USE_BALANCE(0),
    SETTMENT_USE_BALANCE(1),

    /**
     * 购物车内商品状态（0正常，1无货 ，2下架,3 商品不存在）
     */
    CART_STATUS_UNDER(2),
    CART_STATUS_NORMAL(0),
    CART_STATUS_STOCK(1),
    CART_STATUS_DEL(3),;


    private int value;

    CartEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
