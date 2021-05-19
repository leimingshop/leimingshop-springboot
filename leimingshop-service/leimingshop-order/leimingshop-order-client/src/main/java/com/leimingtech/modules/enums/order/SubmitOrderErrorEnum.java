/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 提交订单状态码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/1 14:35
 **/
public enum SubmitOrderErrorEnum {

    /**
     * 校验是否通过，10:商品与用户认证校验通过，20:商品校验未通过 30用户信息校验未通过 40其他校验未通过 50其他错误
     */
    ALL_CHECK_SUCCESS(10),
    GOODS_CHECK_FAILED(20),
    MEMBER_CHECK_FAILED(30),
    OTHER_ACTIVITY_CHECK_FAILED(40),
    ORDER_ORTHER_FAILED(50),


    /**
     * 订单内商品错误状态码 10:超过购买限制 20:库存不足 30:价格变更 40:下架状态
     */
    GOODS_BUY_LIMIT(10),
    GOODS_UNDER_STOCK(20),
    GOODS_PRICE_UPDATE(30),
    GOODS_UP_STATUS(40);

    private int value;

    SubmitOrderErrorEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
