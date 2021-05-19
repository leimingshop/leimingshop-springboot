/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;

/**
 * @Author: weixianchun
 * @Description: 商品规格枚举
 * @Date :2019/6/7 0:56
 * @Version V1.0
 **/
public enum GoodsSpecStatusEnum {
    /**
     * 规格上下架类型:定时上下架
     */
    GOODS_SPEC_SHOW_TYPE_TIMING(1),

    /**
     * 规格上下架类型:立即上下架
     */
    GOODS_SPEC_SHOW_TYPE_NOW(0),
    /**
     * 成功
     */
    GOODS_SPEC_STATUS_SUCCESS(200),
    /**
     * 启用(默认)
     */
    GOODS_SPEC_ENABLE(0),
    /**
     * 未启用
     */
    GOODS_SPEC_NOT_ENABLE(1),
    /**
     * 下架状态
     */
    GOODS_SPEC_SHOW_DOWN(0),
    /**
     * 上架状态
     */
    GOODS_SPEC_SHOW_UP(1),

    /**
     * 自营店铺无须审核
     */
    STORE_NO_AUDIT(2),
    /**
     * 未上架状态
     */
    GOODS_SPEC_SHOW_WAIT(2);


    private int value;

    GoodsSpecStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
