/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.message.enums;

/**
 * 短信模板code
 *
 * @author xuzhch
 */
public enum MessageCodeEnum {
    /**
     * 私信
     */
    MESSAGE_TYPE_PRVT("0"),
    /**
     * 系统消息
     */
    MESSAGE_TYPE_SYSINFO("1"),
    /**
     * 到货通知
     */
    MESSAGE_TYPE_DELIVERY("2"),
    /**
     * 退款通知
     */
    REFUNDS_MESSAGE("3"),
    /**
     * 订单发货消息类型
     */
    ORDER_DELIVER_SMS("6"),
    /**
     * 秒杀活动即将开始通知
     */
    SECKLL_PRESTART_SMS("8"),
    /**
     * 退货审核通过
     */
    AFTER_AUDIT_YES("12"),
    /**
     * 重置密码验证码类型
     */
    TEMPLATECODE_RESET_PWD_CODE("14"),
    /**
     * 退货审核不通过
     */
    AFTER_AUDIT_NO("15"),
    /**
     * 登录注册验证码类型
     */
    TEMPLATECODE_LOGIN_REGISTER("16"),
    /**
     * 购物车商品降价
     */
    CART_GOODS_REDUCE_REMIND("19"),
    /**
     * 库存预警通知
     */
    STORAGE_WARNING("20"),
    /**
     * 购物车商品库存不足
     */
    CART_STORAGE_REMIND("21"),
    /**
     * 订单支付成功通知
     */
    ORDER_PAY_SUCCESS_REMIND("22"),
    /**
     * 虚拟订单支付成功通知
     */
    VIRTUAL_ORDER_PAY_SUCCESS_REMIND("23"),

    /**
     * 订单超时取消通知
     */
    ORDER_TIME_OUT_CANCEL("24");

    private String value;

    MessageCodeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
