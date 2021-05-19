/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.order.code;

/**
 * 订单入参日志码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcOrderCode {

    /**
     * 访问订单列表
     */
    String ORDER_LIST_CODE = "200601";
    String ORDER_LIST_DESC = "访问订单列表";

    /**
     * 访问订单详情
     */
    String ORDER_DETAIL_CODE = "200602";
    String ORDER_DETAIL_DESC = "访问订单详情";

    /**
     * 访问确认收货
     */
    String ORDER_CONFIRM_RECEIPT_CODE = "200603";
    String ORDER_CONFIRM_RECEIPT_DESC = "访问确认收货";

    /**
     * 访问取消订单
     */
    String ORDER_CANCEL_CODE = "200604";
    String ORDER_CANCEL_DESC = "访问取消订单";

    /**
     * 访问删除订单
     */
    String ORDER_DELETE_CODE = "200605";
    String ORDER_DELETE_DESC = "访问删除订单";

    /**
     * 立即购买提交订单
     */
    String ORDER_BUY_NOW_CODE = "200606";
    String ORDER_BUY_NOW_DESC = "订单-立即购买提交订单";

    /**
     * 购物车保存订单
     */
    String ORDER_CART_SAVE_ORDER_CODE = "200607";
    String ORDER_CART_SAVE_ORDER_DESC = "订单-购物车保存订单";

    /**
     * 拼团立即购买提交订单
     */
    String ORDER_GROUP_SAVE_ORDER_CODE = "200608";
    String ORDER_GROUP_SAVE_ORDER_DESC = "订单-拼团立即购买提交订单";


}
