/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.member.evaluate.code;

/**
 * 购物车入参日志码
 *
 * @author chengqian
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcEvaluateCode {

    /**
     * 访问未评价的订单商品
     */
    String NOT_EVALUATE_ORDER_GOODS_CODE = "200950";
    String NOT_EVALUATE_ORDER_GOODS_DESC = "访问未评价的订单商品";

    /**
     * 访问已评价评价的订单商品
     */
    String YES_EVALUATE_ORDER_GOODS_CODE = "200951";
    String YES_EVALUATE_ORDER_GOODS_DESC = "访问已评价评价的订单商品";

    /**
     * 访问新增商品评价
     */
    String SAVE_GOODS_EVALUATE_CODE = "200952";
    String SAVE_GOODS_EVALUATE_DESC = "访问新增商品评价";

    /**
     * 访问订单待评价商品
     */
    String ORDER_GOODS_NOT_EVALUATE_CODE = "200953";
    String ORDER_GOODS_NOT_EVALUATE_DESC = "访问订单待评价商品";

    /**
     * 访问订单待评价商品
     */
    String ORDER_GOODS_YEX_EVALUATE_CODE = "200954";
    String ORDER_GOODS_YESEVALUATE_DESC = "访问订单待评价商品";

}
