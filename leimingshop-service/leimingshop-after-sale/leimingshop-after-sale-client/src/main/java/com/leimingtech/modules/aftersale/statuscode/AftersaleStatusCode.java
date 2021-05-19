/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.aftersale.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * <订单模块日志码>
 *
 * @author hky
 * @since 1.0 2019/7/2
 */
public class AftersaleStatusCode extends ServiceStatusCode {

    /**
     * h5申请售后保存成功
     */
    public static final String H5_AFTERSALE_APPLY_SAVE_SUCCESS_CODE = "200001";
    public static final String H5_AFTERSALE_APPLY_SAVE_SUCCESS_MESSAGE = "h5申请售后保存成功";

    /**
     * h5售后买家上传物流信息
     */
    public static final String H5_AFTERSALE_DELIVERY_SAVE_SUCCESS_CODE = "200002";
    public static final String H5_AFTERSALE_DELIVERY_SAVE_SUCCESS_MESSAGE = "h5售后买家上传物流信息保存成功";

    /**
     * h5售后取消申请
     */
    public static final String H5_AFTERSALE_CANCEL_APPLY_SUCCESS_CODE = "200003";
    public static final String H5_AFTERSALE_CANCEL_APPLY_SUCCESS_MESSAGE = "h5售后买家取消申请成功";

    /**
     * h5售后买家确认收货
     */
    public static final String H5_AFTERSALE_CONFIRM_RECEIPT_SUCCESS_CODE = "200003";
    public static final String H5_AFTERSALE_CONFIRM_RECEIPT_SUCCESS_MESSAGE = "h5售后买家确认收货成功";

    /**
     * h5申请售后详情查询
     */
    public static final String H5_AFTERSALE_APPLY_DETAIL_SUCCESS_CODE = "200004";
    public static final String H5_AFTERSALE_APPLY_DETAIL_SUCCESS_MESSAGE = "h5申请售后详情查询";

    /**
     * 保存审核申请成功，修改为不通过
     */
    public static final String SERVICE_AFTERSALE_APPLY_SUCCESS_REFUSE_CODE = "200005";
    public static final String SERVICE_AFTERSALE_APPLY_SUCCESS_REFUSE_MESSAGE = "保存审核申请成功，修改为不通过";

    /**
     * 修改审核状态成功
     */
    public static final String SERVICE_AFTERSALE_APPLY_SUCCESS_STATUS_CODE = "200006";
    public static final String SERVICE_AFTERSALE_APPLY_SUCCESS_STATUS_MESSAGE = "修改审核状态成功";

    /**
     * 保存退货表成功
     */
    public static final String SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_CODE = "200007";
    public static final String SERVICE_AFTERSALE_APPLY_SAVE_RETURN_SUCCESS_MESSAGE = "保存退货表成功";

    /**
     * 保存换货表成功
     */
    public static final String SERVICE_AFTERSALE_APPLY_SAVE_BATER_SUCCESS_CODE = "200008";
    public static final String SERVICE_AFTERSALE_APPLY_SAVE_BATER_SUCCESS_MESSAGE = "保存换货表成功";

    /**
     * 进入取消售后单流程
     */
    public static final String SERVICE_AFTERSALE_APPLY_CANCEL_SUCCESS_CODE = "200009";
    public static final String SERVICE_AFTERSALE_APPLY_CANCEL_SUCCESS_MESSAGE = "进入取消售后单流程";


    /**
     * 查询售后设置异常，请稍后再试
     */
    public static final String SERVICE_AFTERSALE_APPLY_SETTING_SUCCESS_CODE = "200010";
    public static final String SERVICE_AFTERSALE_APPLY_SETTING_SUCCESS_MESSAGE = "查询售后设置异常，请稍后再试";

    /**
     * 售后设置{}
     */
    public static final String SERVICE_AFTERSALE_SETTING_DETAIL_SUCCESS_CODE = "200011";
    public static final String SERVICE_AFTERSALE_SETTING_DETAIL_SUCCESS_MESSAGE = "售后设置{}";

    /**
     * 查询售后时间异常，请稍后再试
     */
    public static final String SERVICE_AFTERSALE_APPLY_TIME_SUCCESS_CODE = "200012";
    public static final String SERVICE_AFTERSALE_APPLY_TIME_SUCCESS_MESSAGE = "查询售后时间异常，请稍后再试";

    /**
     * 查询订单完成时间异常，请稍后再试
     */
    public static final String SERVICE_AFTERSALE_ORDER_TIMEOUT_SUCCESS_CODE = "200013";
    public static final String SERVICE_AFTERSALE_ORDER_TIMEOUT_SUCCESS_MESSAGE = "查询订单完成时间异常，请稍后再试";

    /**
     * 此商品{}申请数量超过可售后数量
     */
    public static final String SERVICE_AFTERSALE_APPLY_GOODS_COUNT_SUCCESS_CODE = "200014";
    public static final String SERVICE_AFTERSALE_APPLY_GOODS_COUNT_SUCCESS_MESSAGE = "此商品{}申请数量超过可售后数量";


    public static final ServiceStatusCode AFTERSALE_ALREADY_CHECK = new AftersaleStatusCode("400101", "售后单已审核，请勿重复操作");

    protected AftersaleStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
