/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.moudle.cart.code;

/**
 * 购物车入参日志码
 *
 * @author chengqian
 * @version V1.0
 * @date 2020/5/16 9:48
 **/
public interface PcCartCode {

    /**
     * 首页-购物车弹窗列表
     */
    String INDEX_CART_CODE = "200750";
    String INDEX_CART_DESC = "访问首页购物车弹窗列表";

    /**
     * 访问购物车列表
     */
    String INDEX_CART_PAGE_CODE = "200751";
    String INDEX_CART_PAGE_DESC = "访问购物车列表";

    /**
     * 访问加入购物车操作
     */
    String INDEX_SAVE_CART_CODE = "200752";
    String INDEX_SAVE_CART_DESC = "访问加入购物车操作";

    /**
     * 访问未登录删除购物车操作
     */
    String INDEX_NOT_LOGIN_DELETE_CART_CODE = "200753";
    String INDEX_NOT_LOGIN_DELETE_CART_DESC = "访问未登录删除购物车操作";

    /**
     * 访问已登录删除购物车操作
     */
    String INDEX_LOGIN_DELETE_CART_CODE = "200754";
    String INDEX_LOGIN_DELETE_CART_DESC = "访问已登录删除购物车操作";

    /**
     * 访问同步购物车操作
     */
    String INDEX_MERGE_CART_CODE = "200755";
    String INDEX_MERGE_CART_DESC = "访问同步购物车操作";

    /**
     * 访问购物车单选操作
     */
    String INDEX_CHECK_CART_CODE = "200756";
    String INDEX_CHECK_CART_DESC = "访问购物车单选操作";

    /**
     * 访问购物车全选操作
     */
    String INDEX_CHECK_ALL_CART_CODE = "200757";
    String INDEX_CHECK_ALL_CART_DESC = "访问购物车全选操作";

    /**
     * 访问查询购物车内数量
     */
    String INDEX_CART_NUM_CODE = "200758";
    String INDEX_CART_NUM_DESC = "访问查询购物车内数量";

    /**
     * 访问购物车移入收藏操作
     */
    String INDEX_CART_FAVORITE_CODE = "200759";
    String INDEX_CART_FAVORITE_DESC = "访问购物车移入收藏操作";

    /**
     * 访问购物车立即购买
     */
    String INDEX_CART_BUYNOW_CODE = "200760";
    String INDEX_CART_BUYNOW_DESC = "访问购物车立即购买";

    /**
     * 访问购物车立即购买切换优惠
     */
    String INDEX_CART_BUYNOW_CHANGE_CODE = "200760";
    String INDEX_CART_BUYNOW_CHANGE_DESC = "访问购物车立即购买切换优惠";

    /**
     * 访问发起拼团
     */
    String INDEX_CART_GROUP_BUYNOW_CODE = "200761";
    String INDEX_CART_GROUP_BUYNOW_DESC = "访问发起拼团";

    /**
     * 访问购物车去结算切换优惠
     */
    String INDEX_CART_SETTMENT_CHANGE_CODE = "200762";
    String INDEX_CART_SETTMENT_CHANGE_DESC = "访问购物车去结算切换优惠";

    /**
     * 访问购物车去结算
     */
    String INDEX_CART_SETTMENT_CODE = "200763";
    String INDEX_CART_SETTMENT_DESC = "访问购物车去结算";


}
