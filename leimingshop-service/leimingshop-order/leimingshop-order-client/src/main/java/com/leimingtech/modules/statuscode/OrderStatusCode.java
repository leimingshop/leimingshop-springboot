/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * <订单模块日志码>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/2
 */
public class OrderStatusCode extends ServiceStatusCode {

    /**
     * admin订单分页查询成功
     */
    public static final String ADMIN_ORDER_PAGE_SUCCESS_CODE = "200101";
    public static final String ADMIN_ORDER_PAGE_SUCCESS_MESSAGE = "admin订单分页查询成功";

    /**
     * admin订单详情查询成功
     */
    public static final String ADMIN_ORDER_DETAIL_SUCCESS_CODE = "200102";
    public static final String ADMIN_ORDER_DETAIL_SUCCESS_MESSAGE = "admin订单详情查询成功";

    /**
     * admin订单详情查询成功
     */
    public static final String ADMIN_ORDER_EXPORT_SUCCESS_CODE = "200103";
    public static final String ADMIN_ORDER_EXPORT_SUCCESS_MESSAGE = "admin订单列表导出成功";

    /**
     * seller订单分页查询成功
     */
    public static final String SELLER_ORDER_PAGE_SUCCESS_CODE = "200201";
    public static final String SELLER_ORDER_PAGE_SUCCESS_MESSAGE = "seller订单分页查询成功";

    /**
     * seller订单详情查询成功
     */
    public static final String SELLER_ORDER_DETAIL_SUCCESS_CODE = "200202";
    public static final String SELLER_ORDER_DETAIL_SUCCESS_MESSAGE = "seller订单详情查询成功";

    /**
     * seller订单列表导出成功
     */
    public static final String SELLER_ORDER_EXPORT_SUCCESS_CODE = "200203";
    public static final String SELLER_ORDER_EXPORT_SUCCESS_MESSAGE = "seller订单列表导出成功";

    /**
     * seller订单发货成功
     */
    public static final String SELLER_ORDER_SHIPPMENT_SUCCESS_CODE = "200204";
    public static final String SELLER_ORDER_SHIPPMENT_SUCCESS_MESSAGE = "seller订单发货成功";

    /**
     * seller取消订单成功
     */
    public static final String SELLER_ORDER_CANCEL_SUCCESS_CODE = "200205";
    public static final String SELLER_ORDER_CANCEL_SUCCESS_MESSAGE = "seller取消订单成功";

    /**
     * h5订单分页查询成功
     */
    public static final String H5_ORDER_PAGE_SUCCESS_CODE = "200301";
    public static final String H5_ORDER_PAGE_SUCCESS_MESSAGE = "h5订单分页查询成功";

    /**
     * h5订单详情查询成功
     */
    public static final String H5_ORDER_DETAIL_SUCCESS_CODE = "200302";
    public static final String H5_ORDER_DETAIL_SUCCESS_MESSAGE = "h5订单详情查询成功";

    /**
     * h5订单物流记录查询成功
     */
    public static final String H5_ORDER_LOGISTICS_LOG_DETAIL_SUCCESS_CODE = "200303";
    public static final String H5_ORDER_LOGISTICS_LOG_DETAIL_SUCCESS_MESSAGE = "h5订单物流记录查询成功";

    /**
     * h5指定状态订单数量查询成功
     */
    public static final String H5_ORDER_STATUS_COUNT_SUCCESS_CODE = "200304";
    public static final String H5_ORDER_STATUS_COUNT_SUCCESS_MESSAGE = "h5指定状态订单数量查询成功";

    /**
     * h5可申请售后订单分页查询成功
     */
    public static final String H5_ORDER_AVAILABLE_AFTERSALE_PAGE_SUCCESS_CODE = "200305";
    public static final String H5_ORDER_AVAILABLE_AFTERSALE_PAGE_SUCCESS_MESSAGE = "h5可申请售后订单分页查询成功";

    /**
     * h5订单中可申请售后商品详情查询成功
     */
    public static final String H5_ORDER_AVAILABLE_AFTERSALE_GOODS_SUCCESS_CODE = "200306";
    public static final String H5_ORDER_AVAILABLE_AFTERSALE_GOODS_SUCCESS_MESSAGE = "h5订单中可申请售后商品详情查询成功";

    /**
     * h5立即购买提交订单成功
     */
    public static final String H5_ORDER_BUYNOW_SUCCESS_CODE = "200307";
    public static final String H5_ORDER_BUYNOW_SUCCESS_MESSAGE = "h5立即购买提交订单成功";

    /**
     * h5购物车提交订单成功
     */
    public static final String H5_ORDER_CART_SUCCESS_CODE = "200308";
    public static final String H5_ORDER_CART_SUCCESS_MESSAGE = "h5购物车提交订单成功";

    /**
     * h5订单确认收货成功
     */
    public static final String H5_ORDER_COMFIRM_RECEIPT_SUCCESS_CODE = "200309";
    public static final String H5_ORDER_COMFIRM_RECEIPT_SUCCESS_MESSAGE = "h5订单确认收货成功";

    /**
     * h5订单取消成功
     */
    public static final String H5_ORDER_CANCEL_SUCCESS_CODE = "200310";
    public static final String H5_ORDER_CANCEL_SUCCESS_MESSAGE = "h5订单取消成功";

    /**
     * h5订单删除成功
     */
    public static final String H5_ORDER_DELETE_SUCCESS_CODE = "200311";
    public static final String H5_ORDER_DELETE_SUCCESS_MESSAGE = "h5订单删除成功";

    /**
     * h5订单保存状态查询成功
     */
    public static final String H5_ORDER_SAVE_CHECK_SUCCESS_CODE = "200312";
    public static final String H5_ORDER_SAVE_CHECK_SUCCESS_MESSAGE = "h5订单保存状态查询成功";

    /**
     * 订单发货成功
     */
    public static final String ORDER_SHIPPMENT_SUCCESS_CODE = "200901";
    public static final String ORDER_SHIPPMENT_SUCCESS_MESSAGE = "订单发货成功";

    /**
     * 立即购买提交订单成功
     */
    public static final String ORDER_BUYNOW_SUCCESS_CODE = "200902";
    public static final String ORDER_BUYNOW_SUCCESS_MESSAGE = "立即购买提交订单成功";

    /**
     * 异步订单保存成功
     */
    public static final String ORDER_CONFIRM_SVAE_SUCCESS_CODE = "200903";
    public static final String ORDER_CONFIRM_SAVE_SUCCESS_MESSAGE = "异步订单保存成功";

    /**
     * 订单收货地址保存成功
     */
    public static final String ORDER_ADDRESS_SVAE_SUCCESS_CODE = "200904";
    public static final String ORDER_ADDRESS_SAVE_SUCCESS_MESSAGE = "订单收货地址保存成功";

    /**
     * 订单支付信息保存成功
     */
    public static final String ORDER_PAY_SAVE_SUCCESS_CODE = "200905";
    public static final String ORDER_PAY_SAVE_SUCCESS_MESSAGE = "订单支付信息保存成功";

    /**
     * 订单保存成功
     */
    public static final String ORDER_SAVE_SUCCESS_CODE = "200906";
    public static final String ORDER_SAVE_SUCCESS_MESSAGE = "订单保存成功";

    /**
     * 订单商品保存成功
     */
    public static final String ORDER_GOODS_SAVE_SUCCESS_CODE = "200907";
    public static final String ORDER_GOODS_SAVE_SUCCESS_MESSAGE = "订单商品保存成功";

    /**
     * 订单商品保存成功
     */
    public static final String ORDER_LOG_SAVE_SUCCESS_CODE = "200908";
    public static final String ORDER_LOG_SAVE_SUCCESS_MESSAGE = "订单商品保存成功";

    /**
     * 支付完成异步回调修改订单状态成功
     */
    public static final String ORDER_STATE_PAY_UPDATE_SUCCESS_CODE = "200909";
    public static final String ORDER_STATE_PAY_UPDATE_SUCCESS_MESSAGE = "支付完成异步回调修改订单状态成功";

    /**
     * 订单活动保存成功
     */
    public static final String ORDER_ACTIVITY_SAVE_SUCCESS_CODE = "200910";
    public static final String ORDER_ACTIVITY_SAVE_SUCCESS_MESSAGE = "订单活动保存成功";

    /**
     * 删除订单失败，订单状态必须为已完成
     */
    public static final ServiceStatusCode ORDER_CHANGE_SHOW_STATUS_EXCERTION = new OrderStatusCode("400101", "删除订单失败，订单状态必须为已完成或已取消");

    /**
     * admin订单列表导出异常
     */
    public static final String ADMIN_ORDER_EXPORT_SERVER_INTERNAL_ERROR_CODE = "500101";
    public static final String ADMIN_ORDER_EXPORT_SERVER_INTERNAL_ERROR_MESSAGE = "admin订单列表导出异常";


    /**
     * seller订单列表导出异常
     */
    public static final String SELLER_ORDER_EXPORT_SERVER_INTERNAL_ERROR_CODE = "500102";
    public static final String SELLER_ORDER_EXPORT_SERVER_INTERNAL_ERROR_MESSAGE = "seller订单列表导出异常";

    /**
     * 提交订单失败，保存订单确认表异常
     */
    public static final ServiceStatusCode BUYNOW_CONFIRM_SAVE_FAIL = new OrderStatusCode("500103", "提交订单失败，保存订单确认表异常");

    /**
     * 提交订单失败，购物车未选中
     */
    public static final ServiceStatusCode CART_CONFIRM_NO_SELECT_CART = new OrderStatusCode("500104", "提交订单失败，购物车未选中");

    /**
     * 异步订单保存异常
     */
    public static final String ORDER_CONFIRM_SVAE_SERVER_INTERNAL_ERROR_CODE = "500105";
    public static final String ORDER_CONFIRM_SAVE_SERVER_INTERNAL_ERROR_MESSAGE = "异步订单保存异常";

    /**
     * 取消订单失败，修改支付状态失败
     */
    public static final ServiceStatusCode CANCEL_ORDER_UPDATE_PAY_FAIL = new OrderStatusCode("500106", "取消订单失败，修改支付状态失败");

    /**
     * 取消订单失败，订单不存在
     */
    public static final ServiceStatusCode CANCEL_ORDER_NO_EXIST_ORDER = new OrderStatusCode("500107", "取消订单失败，订单不存在");

    /**
     * 取消订单失败，订单状态异常
     */
    public static final ServiceStatusCode CANCEL_ORDER_STATUS_EXCPTION = new OrderStatusCode("500108", "取消订单失败，订单状态异常");

    /**
     * 取消订单失败，更新库存销量失败
     */
    public static final ServiceStatusCode CANCEL_ORDER_UPDATE_STROAGR_SALE_FAIL = new OrderStatusCode("500109", "取消订单失败，更新库存销量失败");

    /**
     * 保存订单失败，更新库存销量失败
     */
    public static final ServiceStatusCode SAVE_ORDER_UPDATE_STROAGR_SALE_FAIL = new OrderStatusCode("500110", "保存订单失败，更新库存销量失败");

    /**
     * h5立即购买提交订单失败，商品库存不足
     */
    public static final ServiceStatusCode BUYNOW_GOODS_NO_STORAGE = new OrderStatusCode("500301", "立即购买提交订单失败，商品库存不足");

    /**
     * h5立即购买提交订单失败，商品价格变化
     */
    public static final ServiceStatusCode BUYNOW_GOODS_PRICE_CHANGE = new OrderStatusCode("500302", "立即购买提交订单失败，商品价格变化");

    /**
     * h5立即购买提交订单失败，商品已下架
     */
    public static final ServiceStatusCode BUYNOW_GOODS_NO_EXIST = new OrderStatusCode("500303", "立即购买提交订单失败，商品已下架");

    /**
     * h5立即购买提交订单失败，商品不能开发票
     */
    public static final ServiceStatusCode BUYNOW_GOODS_NO_INVOICE = new OrderStatusCode("500312", "立即购买提交订单失败，商品不能开发票");

    /**
     * h5立即购买提交订单失败，用户地址不存在
     */
    public static final ServiceStatusCode BUYNOW_ADDRESS_NO_EXIST = new OrderStatusCode("500304", "立即购买提交订单失败，用户地址不存在");

    /**
     * h5提交订单失败，购物车未勾选商品
     */
    public static final ServiceStatusCode BUY_CART_NO_CHECKED = new OrderStatusCode("500305", "h5提交订单失败，购物车未勾选商品");

    /**
     * h5提交订单失败，订单支付信息保存失败
     */
    public static final ServiceStatusCode BUY_PAY_SAVE_FAIL = new OrderStatusCode("500306", "h5提交订单失败，订单支付信息保存失败");

    /**
     * 提交订单失败，优惠券不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_NO_COUPONS = new OrderStatusCode("500307", "提交订单失败，优惠券不存在");

    /**
     * 提交订单失败，优惠券不可用
     */
    public static final ServiceStatusCode SAVE_ORDER_COUPONS_CANNT_USE = new OrderStatusCode("500308", "提交订单失败，优惠券不可用");

    /**
     * 提交订单失败，满减活动不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_NO_REDUCE_ACTIVITY = new OrderStatusCode("500309", "提交订单失败，满减活动不存在");

    /**
     * 提交订单失败，满减活动未开始
     */
    public static final ServiceStatusCode SAVE_ORDER_REDUCE_ACTIVITY_PREPARE = new OrderStatusCode("500310", "提交订单失败，满减活动未开始");

    /**
     * 提交订单失败，满减活动已结束
     */
    public static final ServiceStatusCode SAVE_ORDER_REDUCE_ACTIVITY_END = new OrderStatusCode("500311", "提交订单失败，满减活动已结束");

    /**
     * 提交订单失败，秒杀活动不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_NO_SECKILL_ACTIVITY = new OrderStatusCode("500312", "提交订单失败，秒杀活动不存在");

    /**
     * 提交订单失败，秒杀活动未开始
     */
    public static final ServiceStatusCode SAVE_ORDER_SECKILL_ACTIVITY_PREPARE = new OrderStatusCode("500313", "提交订单失败，秒杀活动未开始");

    /**
     * 提交订单失败，秒杀活动已结束
     */
    public static final ServiceStatusCode SAVE_ORDER_SECKILL_ACTIVITY_END = new OrderStatusCode("500314", "提交订单失败，秒杀活动已结束");

    /**
     * 提交订单失败，活动商品库存不足
     */
    public static final ServiceStatusCode SAVE_ORDER_ACTIVITY_STORAGE_LACK = new OrderStatusCode("500315", "提交订单失败，活动商品库存不足");

    /**
     * 提交订单失败，超出活动商品限购数量
     */
    public static final ServiceStatusCode SAVE_ORDER_ACTIVITY_LIMIT_OVER = new OrderStatusCode("500316", "提交订单失败，超出活动商品限购数量");

    /**
     * 提交订单失败，扣减活动商品库存异常
     */
    public static final ServiceStatusCode SAVE_ORDER_REDUCE_ACTIVITY_STORAGE_ERROR = new OrderStatusCode("500317", "提交订单失败，扣减活动商品库存异常");

    /**
     * 提交订单失败，低于活动限制会员等级
     */
    public static final ServiceStatusCode SAVE_ORDER_ACTIVITY_LESS_MEMBER_GRADE = new OrderStatusCode("500318", "提交订单失败，低于活动限制会员等级");

    /**
     * 提交订单失败，获取活动商品库存异常
     */
    public static final ServiceStatusCode SAVE_ORDER_ACTIVITY_STORAGE_ERROR = new OrderStatusCode("500319", "提交订单失败，获取活动商品库存异常");

    /**
     * 提交订单失败，获取秒杀商品价格异常
     */
    public static final ServiceStatusCode SAVE_ORDER_ACTIVITY_PRICE_ERROR = new OrderStatusCode("500320", "提交订单失败，获取活动商品价格异常");

    /**
     * 取消订单失败，更新秒杀库存异常
     */
    public static final ServiceStatusCode CANCEL_ORDER_UPDATE_SECKILL_STROAGR_EXCEPTION = new OrderStatusCode("500321", "取消订单失败，更新秒杀库存异常");

    /**
     * 取消订单失败，更新拼团库存异常
     */
    public static final ServiceStatusCode CANCEL_ORDER_UPDATE_GROUP_STROAGR_EXCEPTION = new OrderStatusCode("500400", "取消订单失败，更新拼团库存异常");


    /**
     * h5获取待支付订单去支付失败
     */
    public static final ServiceStatusCode ORDER_PAY_SERVER_RESPONSE_VALIDATION_ERROR = new OrderStatusCode("200305", "获取paySn失败");

    /**
     * 提交订单失败，拼团活动不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_NO_GROUP_ACTIVITY = new OrderStatusCode("500324", "提交订单失败，拼团活动不存在");

    /**
     * 提交订单失败，拼团活动未开始
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_PREPARE = new OrderStatusCode("500322", "提交订单失败，拼团活动未开始");

    /**
     * 提交订单失败，拼团活动已结束
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_END = new OrderStatusCode("500323", "提交订单失败，拼团活动已结束");

    /**
     * 提交订单失败，获取拼团商品价格异常
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_PRICE_ERROR = new OrderStatusCode("500320", "提交订单失败，获取拼团商品价格异常");

    /**
     * 提交订单失败，获取拼团记录不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_RECORD_ERROR = new OrderStatusCode("500321", "提交订单失败，获取拼团记录异常");

    /**
     * 提交订单失败，获取拼团人数已满
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_PEOPLE_OVER = new OrderStatusCode("500325", "提交订单失败，拼团人数已满");

    /**
     * 提交订单失败，拼团记录状态拼团失败
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_RECORD_FAIL = new OrderStatusCode("500326", "提交订单失败，拼团失败");

    /**
     * 提交订单失败，请稍后重试
     */
    public static final ServiceStatusCode SAVE_ORDER_UNKNOWN_ERROR = new OrderStatusCode("500326", "提交订单失败，请稍后重试");

    /**
     * 提交订单失败，超出成团时间
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_OVER_TIME = new OrderStatusCode("500323", "提交订单失败，超出拼团成团时间");

    /**
     * 提交订单失败，已参加过本次拼团
     */
    public static final ServiceStatusCode SAVE_ORDER_GROUP_ACTIVITY_OVER_JOIN = new OrderStatusCode("500324", "提交订单失败，已参加过此拼团");
    /**
     * 提交订单失败，获取秒杀商品价格异常
     */
    public static final ServiceStatusCode ORDER_AMOUNT_OUT_MAX_PRICE = new OrderStatusCode("500326", "提交订单失败，单笔订单最大金额不可超过1000万");

    public static final ServiceStatusCode ORDER_SHIPPMENT_FAILED = new OrderStatusCode("500902", "订单发货失败，订单不存在");

    public static final ServiceStatusCode ORDER_SHIPPMENT_FAILED_STATUS = new OrderStatusCode("500901", "订单发货失败，订单状态异常");
    /**
     * 提交订单失败，限时抢购活动不存在
     */
    public static final ServiceStatusCode SAVE_ORDER_NO_FLASH_SALE_ACTIVITY = new OrderStatusCode("500312", "提交订单失败，限时抢购活动不存在");

    /**
     * 提交订单失败，限时抢购活动未开始
     */
    public static final ServiceStatusCode SAVE_ORDER_FLASH_SALE_ACTIVITY_PREPARE = new OrderStatusCode("500313", "提交订单失败，限时抢购活动未开始");

    /**
     * 提交订单失败，限时抢购活动已结束
     */
    public static final ServiceStatusCode SAVE_ORDER_FLASH_SALE_ACTIVITY_END = new OrderStatusCode("500314", "提交订单失败，限时抢购活动已结束");
    /**
     * 虚拟订单核销相关码
     */
    public static final ServiceStatusCode VERIFY_ORDER_NOT_EXIST = new OrderStatusCode("500501", "电子提货码不存在");
    public static final ServiceStatusCode VERIFY_ORDER_EXPRISE = new OrderStatusCode("500502", "电子提货码已过期");
    public static final ServiceStatusCode VERIFY_ORDER_NUMBER_ERROR = new OrderStatusCode("500502", "核销数量不对");

    /**
     * h5立即购买提交订单失败，用户可用余额为0
     */
    public static final ServiceStatusCode BUYNOW_AVAILABLE_BALANCE_ZERO = new OrderStatusCode("500601", "立即购买提交订单失败，用户可用余额为0");

    /**
     * 支付完成异步回调修改订单状态失败，订单不存在
     */
    public static final String ORDER_STATE_PAY_UPDATE_SERVER_RESPONSE_VALIDATION_ERRORA_CODE = "500909";
    public static final String ORDER_STATE_PAY_UPDATE_SERVER_RESPONSE_VALIDATION_ERRORA_MESSAGE = "支付完成异步回调修改订单状态失败，订单不存在";

    /**
     * h5端点击订单确认收货，表示订单进入已完成阶段
     */
    public static final String ORDER_CONFIRM_RECEIPT_H5 = "200306";
    public static final String ORDER_CONFIRM_RECEIPT_H5_MESSAGE = "h5端点击订单确认收货";

    /**
     * 确认收货后，开始计算本次购物的积分和成长值
     */
    public static final String AFTER_ORDER_CONFIRM_RECEIPT_H5_COMPUTE_POINT = "200307";
    public static final String AFTER_ORDER_CONFIRM_RECEIPT_H5_COMPUTE_POINT_MESSAGE = "确认收货后，开始计算本次购物的积分和成长值";

    /**
     * 订单完成计算出的积分值和成长值
     */
    public static final String ORDER_FINISH_H5_COMPUTE_POINT = "200308";
    public static final String ORDER_FINISH_H5_COMPUTE_POINT_MESSAGE = "订单完成计算出的积分值和成长值";

    public static final ServiceStatusCode SAVE_ORDER_EXCEPTION = new OrderStatusCode("500910", "下单异常，请稍后重试");

    public static final ServiceStatusCode GET_PARENT_ORDER_FAILED = new OrderStatusCode("500911", "获取父订单信息异常");
    public static final ServiceStatusCode AFTER_GOODS_COUNT_IS_ZERO = new OrderStatusCode("500912", "该订单无可售后商品");

    protected OrderStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
