/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;

/**
 * 商品错误码
 *
 * @ClassName GoodsErrorCodeEnum
 * @Description 商品错误码
 * @Author DY
 * @Date 2019/6/18 10:52
 * @Version 1.0
 **/
public enum GoodsErrorCodeEnum {
    /**
     * 商品不存在
     */
    IS_NULL(100001, "商品不存在"),
    /**
     * 请重新选择上下架状态类型
     */
    GOODS_SHOW(100002, "请重新选择上下架状态类型"),
    /**
     * 审核原因不能为空
     */
    REMARKS_IS_NULL(100003, "审核原因不能为空"),
    /**
     * 货号重复
     */
    SERIALD_REPEAT(100004, "货号重复"),
    /**
     * 商品未通过审核不能进行操作
     */
    NOT_UPDATE(100005, "商品未通过审核不能进行操作");


    private int code;
    private String msg;

    GoodsErrorCodeEnum(int code, String msg) {
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
