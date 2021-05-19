/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.statuscode;

import com.leimingtech.exception.ServiceStatusCode;

/**
 * <支付模块日志码>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2019/7/3
 */
public class PaymentStatusCode extends ServiceStatusCode {

    /**
     * 查询所有支付信息--admin端
     */
    public static final String ADMIN_PAYMENT_FIND_SUCCESS_CODE = "200101";
    public static final String ADMIN_PAYMENT_FIND_SUCCESS_MESSAGE = "查询所有支付信息";

    /**
     * 修改支付方式开启关闭状态
     */
    public static final String ADMIN_PAYMENT_STATUS_UPDATE_CODE = "200102";
    public static final String ADMIN_PAYMENT_STATUS_UPDATE_MESSAGE = "修改支付方式开启关闭状态";

    /**
     * 根据支付id, 查询支付接口信息
     */
    public static final String ADMIN_PAYMENT_FIND_BY_ID_CODE = "200103";
    public static final String ADMIN_PAYMENT_FIND_BY_ID_MESSAGE = "根据支付id, 查询支付接口信息";

    /**
     * 修改支付方式信息(页面保存)
     */
    public static final String ADMIN_PAYMENT_UPDATE_SAVE_CODE = "200104";
    public static final String ADMIN_PAYMENT_UPDATE_SAVE_MESSAGE = "修改支付方式信息(页面保存)";

    /**
     * 导出支付流水信息
     */
    public static final String ADMIN_PAYMENT_TALLY_EXPORT_CODE = "200105";
    public static final String ADMIN_PAYMENT_TALLY_EXPORT_CODE_MESSAGE = "导出支付流水信息";


    /**
     * 查询所有支付信息--seller端
     */
    public static final String SELLER_PAYMENT_FIND_SUCCESS_CODE = "200201";
    public static final String SELLER_PAYMENT_FIND_SUCCESS_MESSAGE = "查询所有支付信息";

    /**
     * 根据支付单号提交微信预支付成功
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_SUCCESS = new PaymentStatusCode("200301", "H5根据支付单号提交微信预支付成功");

    /**
     * 根据支付单号提交支付宝预支付成功
     */
    public static final ServiceStatusCode ALI_PRE_PAY_PAYSN_SUCCESS = new PaymentStatusCode("200304", "H5根据支付单号提交支付宝预支付成功");


    /**
     * H5微信支付回调成功
     */
    public static final String H5_WECHAT_PAY_NOTIFY_SUCCESS_CODE = "200302";
    public static final String H5_WECHAT_PAY_NOTIFY_SUCCESS_MESSAGE = "H5微信支付回调成功";

    /**
     * h5获取待支付订单去支付成功
     */
    public static final String H5_ORDER_PAY_SUCCESS_CODE = "200303";
    public static final String H5_ORDER_PAY_SUCCESS_MESSAGE = "获取待支付订单去支付成功";

    /**
     * 根据支付单号提交微信预支付成功
     */
    public static final String WECHAT_PAY_PAYSN_SUCCESS_CODE = "200901";
    public static final String WECHAT_PAY_PAYSN_SUCCESS_MESSAGE = "根据支付单号提交微信预支付成功";

    /**
     * 微信支付回调成功
     */
    public static final String WECHAT_PAY_NOTIFY_SUCCESS_CODE = "200902";
    public static final String WECHAT_PAY_NOTIFY_SUCCESS_MESSAGE = "微信支付回调成功";

    /**
     * 支付宝支付回调成功
     */
    public static final String ALI_PAY_NOTIFY_SUCCESS_CODE = "200904";
    public static final String ALI_PAY_NOTIFY_SUCCESS_MESSAGE = "支付宝支付回调成功";

    /**
     * 微信退款
     */
    public static final String WECHAT_REFUND_SUCCESS_CODE = "200903";
    public static final String WECHAT_REFUND_SUCCESS_MESSAGE = "微信退款";

    /**
     * 去支付失败，订单不存在或已经支付完成
     */
    public static final ServiceStatusCode TO_PAY_NO_PAY = new PaymentStatusCode("400101", "去支付失败，订单不存在或已经支付完成");

    /**
     * 根据支付单号提交微信预支付失败，服务器内部异常
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_EXCEPTION = new PaymentStatusCode("500101", "根据支付单号提交微信预支付失败，服务器内部异常");

    /**
     * H5根据支付单号提交微信预支付失败,服务器内部异常
     */
    public static final String H5_WECHAT_PAY_PAYSN_SERVER_INTERNAL_ERROR_CODE = "500301";
    public static final String H5_WECHAT_PAY_PAYSN_SERVER_INTERNAL_ERROR_MESSAGE = "H5根据支付单号提交微信预支付失败,服务器内部异常";


    /**
     * H5根据支付单号提交支付宝预支付失败,服务器内部异常
     */
    public static final String H5_ALI_PAY_PAYSN_SERVER_INTERNAL_ERROR_CODE = "500303";
    public static final String H5_ALI_PAY_PAYSN_SERVER_INTERNAL_ERROR_MESSAGE = "H5根据支付单号提交支付宝预支付失败,服务器内部异常";

    /**
     * H5微信支付回调失败，服务器内部异常
     */
    public static final String H5_WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE = "500302";
    public static final String H5_WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE = "H5微信支付回调失败，服务器内部异常";

    /**
     * 微信支付回调失败，服务器内部异常
     */
    public static final String WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE = "500902";
    public static final String WECHAT_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE = "微信支付回调失败，服务器内部异常";

    /**
     * 支付宝支付回调失败，服务器内部异常
     */
    public static final String ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_CODE = "500911";
    public static final String ALI_PAY_NOTIFY_SERVER_INTERNAL_ERROR_MESSAGE = "支付宝支付回调失败，服务器内部异常";

    /**
     * 根据支付单号提交微信预支付失败,支付单号不存在
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION = new PaymentStatusCode("500901", "根据支付单号提交微信预支付失败,支付单号不存在");

    /**
     * 根据支付单号提交微信预支付失败,订单支付状态异常
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_STATUS_EXCEPTION = new PaymentStatusCode("500902", "根据支付单号提交微信预支付失败,订单支付状态异常");

    /**
     * 根据支付单号提交微信预支付失败,获取微信预支付id失败
     */
    public static final ServiceStatusCode WECHAT_PRE_PAY_PAYSN_NO_PREPAYID = new PaymentStatusCode("500903", "根据支付单号提交微信预支付失败,获取微信预支付id失败");


    /**
     * 根据支付单号查询微信支付结果失败
     */
    public static final ServiceStatusCode WECHAT_ORDER_QUERY_FAIL = new PaymentStatusCode("500908", "根据支付单号查询微信支付结果失败");

    /**
     * 微信支付回调失败，验证签名错误
     */
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORA_CODE = "500904";
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORA_MESSAGE = "微信支付回调失败，验证签名错误";

    /**
     * 微信支付回调失败，验证签名错误
     */
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_CODE = "500905";
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_MESSAGE = "微信支付回调失败，结果码错误";

    /**
     * 支付宝支付回调失败
     */
    public static final String ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_CODE = "500910";
    public static final String ALI_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORB_MESSAGE = "支付包支付回调失败，结果码错误";

    /**
     * 微信支付回调失败，修改订单状态异常
     */
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORC_CODE = "500906";
    public static final String WECHAT_PAY_NOTIFY_SERVER_RESPONSE_VALIDATION_ERRORC_MESSAGE = "微信支付回调失败，修改订单状态异常";

    /**
     * 根据支付单号提交微信预支付失败,获取微信预支付id失败
     */
    public static final ServiceStatusCode ORDER_CANCEL_TIMING_UPDATE_ORDER_FAIL = new PaymentStatusCode("500907", "定时取消订单失败，更新订单状态异常");

    /**
     * 根据支付单号提交支付宝预支付失败,订单支付状态异常
     */
    public static final ServiceStatusCode ALIPAY_PRE_PAY_PAYSN_STATUS_EXCEPTION = new PaymentStatusCode("500908", "根据支付单号提交支付宝预支付失败,订单支付状态异常");

    /**
     * 根据支付单号提交支付宝预支付失败,支付单号不存在
     */
    public static final ServiceStatusCode ALIPAY_PRE_PAY_PAYSN_NO_EXIST_EXCEPTION = new PaymentStatusCode("500909", "根据支付单号提交支付宝预支付失败,支付单号不存在");

    public static final ServiceStatusCode WECHAT_MINI_PAYSN_NO_PREPAYID = new PaymentStatusCode("500904", "小程序根据支付单号提交微信预支付失败");


    public static final ServiceStatusCode WECHAT_WECHAT_PAY_SUCCESS = new PaymentStatusCode("200302", "小程序根据支付单号提交微信预支付成功");

    /**
     * 根据支付单号查询微信支付结果失败
     */
    public static final ServiceStatusCode APPLY_NOTEXITC_FAIL = new PaymentStatusCode("500910", "提现申请单号不存在");

    public static final ServiceStatusCode REQUEST_APPLE_SEVER_FAIL = new PaymentStatusCode("500910", "请求Apple票据校验服务器失败");

    public static final ServiceStatusCode BUNDLE_ID_WRONG = new PaymentStatusCode("500911", "非法请求，bundle_id不匹配");

    public static final ServiceStatusCode ORDER_REPEAT_PAY = new PaymentStatusCode("500913", "订单重复支付");

    public static final ServiceStatusCode TRANSACTION_ID_IS_USED = new PaymentStatusCode("500914", "票据已经被使用");

    public static final ServiceStatusCode ENV_WRONG = new PaymentStatusCode("500915", "环境错误，属于沙箱环境订单");

    public static final ServiceStatusCode PARAM_WRONG = new PaymentStatusCode("500916", "请求参数错误");


    protected PaymentStatusCode(String code, String message, Object... arguments) {
        super(code, message, arguments);
    }

    @Override
    public ServiceStatusCode copy(String var1, Object... var2) {
        return null;
    }

}
