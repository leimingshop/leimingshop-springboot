/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.document;


/**
 * @Description
 * @Data: 2020/2/27 13:58
 * @Author: chengqian
 * @Version: 1.0
 */
public enum DocumentEnum {

    /**
     * 入住须知
     */
    STORE_REGISTER("storeRegister"),

    /**
     * 用户协议
     */
    USER_PROTOCOL("registerCode"),

    /**
     * 注册协议
     */
    REGISTER_PROTOCOL("registerProtocol");

    private String value;

    DocumentEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
