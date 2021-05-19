/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.spec;

/**
 * 规格错误码
 *
 * @author chengqina
 */
public enum SpecErrorCodeEnum {
    /**
     * 商品总库存最大为999999
     */
    STORAGE_OUT(100001, "商品总库存最大为999999");

    private int code;
    private String msg;

    SpecErrorCodeEnum(int code, String msg) {
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
