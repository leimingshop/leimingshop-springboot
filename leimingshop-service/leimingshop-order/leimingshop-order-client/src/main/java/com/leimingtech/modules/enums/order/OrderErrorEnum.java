/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 订单错误码
 */
public enum OrderErrorEnum {

    /**
     * 订单不可取消
     */
    NOT_CANCELED(200001, "订单不可取消"),

    /**
     * 订单不存在
     */
    IS_NULL(200002, "订单不存在"),

    /**
     * 操作错误
     */
    OPERATION_ERROR(200003, "操作错误"),

    /**
     * 获取父订单信息异常
     */
    GET_PARENT_ORDER_ERROR(200004, "获取父订单信息异常");

    private int code;
    private String msg;

    OrderErrorEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        return this.msg;
    }

}
