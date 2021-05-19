/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * @Author: LX 17839193044@162.com
 * @Description: 订单商品相关枚举
 * @Date: 9:43 2019/6/24
 * @Version: V1.0
 */
public enum OrderGoodsEnum {

    /**
     * 是赠品
     */
    YES_GIFT(1),

    /**
     * 不是赠品
     */
    NO_GIFT(0),

    /**
     * 未退分摊金额
     */
    RETURN_PREFERENTIAL_NO(0),

    /**
     * 已退分摊金额
     */
    RETURN_PREFERENTIAL_YES(1),;

    private int value;

    OrderGoodsEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }


}
