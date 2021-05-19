/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;

/**
 * @Author: weixianchun
 * @Description: 定时商品状态枚举
 * @Date :2019/6/7 0:56
 * @Version V1.0
 **/
public enum GoodsTimeStatusEnum {

    /**
     * 操作状态:定时发布
     */
    GOODS_TIME_STATUS_PUBLISH(0),
    /**
     * 操作状态:定时上下架状态
     */
    GOODS_TIME_STATUS_SHOW(1),

    /**
     * 查询类型:商品
     */
    GOODS(0),

    /**
     * 操作人标识: 0 商家; 1平台
     */
    OPERATOR_ADMIN(1),
    OPERATOR_SELLER(0),
    /**
     * 查询类型:规格
     */
    SPEC(1);

    private int value;

    GoodsTimeStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
