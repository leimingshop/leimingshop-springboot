/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * <购物车模块日志码>
 *
 * @author 程前
 * @email 543826372@qq.com
 * @since 1.0 2019/7/2
 */
public class CartStatusCode extends ServiceStatusCode {

    /**
     * 购物车key
     */
    public static final String FRONT_CART_KEY_SUCCESS_CODE = "200101";
    public static final String FRONT_CART_KEY_SUCCESS_MESSAGE = "购物车访问成功";

    /**
     * 加入购物车成功
     */
    public static final String FRONT_SAVE_CART_SUCCESS_CODE = "200102";
    public static final String FRONT_SAVE_CART_SUCCESS_MESSAGE = "加入购物车成功";
    /**
     * 同步购物车
     */
    public static final String FRONT_MERGE_CART_SUCCESS_CODE = "200103";
    public static final String FRONT_MERGE_CART_SUCCESS_MESSAGE = "同步购物车成功";

    /**
     * 全选或者取消全选
     */
    public static final String FRONT_CART_CHECK_ALL_SUCCESS_CODE = "200104";
    public static final String FRONT_CART_CHECK_ALL_SUCCESS_MESSAGE = "全选或者取消全选";
    /**
     * 全选或者取消全选
     */
    public static final String FRONT_BUY_NEW_SUCCESS_CODE = "200105";
    public static final String FRONT_BUY_NEW_SUCCESS_MESSAGE = "立即购买";
    /**
     * 修改购物车选中
     */
    public static final String FRONT_CART_CHECK_SUCCESS_CODE = "200106";
    public static final String FRONT_CART_CHECK_SUCCESS_MESSAGE = "修改购物车选中";

    /**
     * 删除缓存购物车
     */
    public static final String FRONT_CART_DELETE_SUCCESS_CODE = "200107";
    public static final String FRONT_CART_DELETE_SUCCESS_MESSAGE = "删除缓存购物车";

    /**
     * 购物车去结算
     */
    public static final String FRONT_CART_SETTLEMENT_SUCCESS_CODE = "200108";
    public static final String FRONT_CART_SETTLEMENT_SUCCESS_MESSAGE = "购物车去结算";

    /**
     * 商品详情发起拼团
     */
    public static final String GROUP_FRONT_BUY_NEW_SUCCESS_CODE = "200110";
    public static final String GROUP_FRONT_BUY_NEW_SUCCESS_MESSAGE = "发起拼团";

    /**
     * 立即购买
     */
    public static final String FRONT_CART_BUYNOW_SUCCESS_CODE = "200109";
    public static final String FRONT_CART_BUYNOW_SUCCESS_MESSAGE = "立即购买";

    /**
     * 去结算商品不存在
     */
    public static final ServiceStatusCode SETTLEMENT_GOODS_NOTFOUND_ERROR = new CartStatusCode("400101", "商品已下架，请重新选择商品");

    /**
     * 去结算店铺不存在
     */
    public static final ServiceStatusCode SETTLEMENT_STORE_NOTFOUND_ERROR = new CartStatusCode("400102", "店铺不存在，请重新选择商品");

    /**
     * 去结算未选中商品
     */
    public static final ServiceStatusCode SETTLEMENT_NO_SELECT_GOODS_ERROR = new CartStatusCode("400103", "未发现选中商品");

    /**
     * 库存不足
     */
    public static final ServiceStatusCode CART_STORAGE_IS_NOT = new CartStatusCode("400104", "库存不足");
    /**
     * 加入购物车数量不能超过200件
     */
    public static final ServiceStatusCode CART_NUM_ERROR = new CartStatusCode("400105", "加入购物车数量不能超过200件");
    /**
     * 商品已经下架
     */
    public static final ServiceStatusCode GOODS_NOT_FOUND = new CartStatusCode("400106", "商品已经下架");
    /**
     * 当前规格已经不存在
     */
    public static final ServiceStatusCode GOODS_SPEC_NOT_FOUND = new CartStatusCode("400107", "当前规格已经不存在");

    /**
     * 加入购物车失败，活动不存在
     */
    public static final ServiceStatusCode ACTIVITY_NOT_FOUNT = new CartStatusCode("400108", "加入购物车失败，活动不存在");

    /**
     * 加入购物车失败，活动未开始
     */
    public static final ServiceStatusCode ACTIVITY_NOT_START = new CartStatusCode("400109", "加入购物车失败，活动未开始");

    /**
     * 加入购物车失败，活动已结束
     */
    public static final ServiceStatusCode ACTIVITY_HAS_END = new CartStatusCode("400110", "加入购物车失败，活动已结束");

    /**
     * 加入购物车失败，商品未参加当前活动
     */
    public static final ServiceStatusCode GOODS_NOT_JOINED_CHOICE_ACTIVITY = new CartStatusCode("400111", "加入购物车失败，商品未参加当前活动");

    /**
     * 加入购物车失败，活动信息错误
     */
    public static final ServiceStatusCode ACTIVITY_MESSAGE_ERROR = new CartStatusCode("400112", "加入购物车失败，活动信息错误");

    /**
     * 库存不足
     */
    public static final ServiceStatusCode SETTLEMENT_GOODS_STOREAGE_NULL_ERROR = new CartStatusCode("400113", "暂无库存");

    /**
     * 加入购物车失败，活动库存不足
     */
    public static final ServiceStatusCode ACTIVITY_STORAGE_LACK_ERROR = new CartStatusCode("400114", "加入购物车失败，活动库存不足");

    /**
     * 加入购物车失败，超出活动限购数量
     */
    public static final ServiceStatusCode OVER_ACTIVITY_RESTICTION_ERROR = new CartStatusCode("400115", "提交订单失败，超出活动限购数量");

    /**
     * 超出活动限购数量，无法加入购物车
     */
    public static final ServiceStatusCode OVER_ACTIVITY_ADDCART_ERROR = new CartStatusCode("400122", "超出活动限购数量，无法加入购物车");

    /**
     * 拼团活动不存在
     */
    public static final ServiceStatusCode SETTLEMENT_GROUP_NOTFOUND_ERROR = new CartStatusCode("400116", "拼团活动不存在");

    /**
     * 拼团活动不存在
     */
    public static final ServiceStatusCode GROUP_EXCEED_LIMIT_ERROR = new CartStatusCode("400117", "超出拼团活动参团次数");

    /**
     * 虚拟商品与实体商品同结算，请分开结算
     */
    public static final ServiceStatusCode SETTLEMENT_VIRTUAL_GOODS_ERROR = new CartStatusCode("400118", "虚拟商品与实体商品同结算，请分开结算");

    /**
     * 运费模板不存在
     */
    public static final ServiceStatusCode FREIGHT_TEMPLATE_NOTFOUND_ERROR = new CartStatusCode("400118", "运费模板不存在");

    /**
     * 暂不支持该类运费模板
     */
    public static final ServiceStatusCode FREIGHT_TEMPLATE_TYPE_ERROR = new CartStatusCode("400119", "暂不支持该类运费模板");

    /**
     * 暂不支持该类运费计算规则
     */
    public static final ServiceStatusCode FREIGHT_TEMPLATE_RULE_ERROR = new CartStatusCode("400120", "暂不支持该类运费计算规则");

    /**
     * 购物车内商品数量不能超过999件
     */
    public static final ServiceStatusCode SAVE_CART_GOODS_NUM_ERROR = new CartStatusCode("400121", "加入购物车商品数量不能超过999件");

    /**
     * 同步购物车
     */
    public static final String FRONT_CART_MERGE_SUCCESS_CODE = "200110";
    public static final String FRONT_CART_MERGE_SUCCESS_MESSAGE = "同步购物车";

    protected CartStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
