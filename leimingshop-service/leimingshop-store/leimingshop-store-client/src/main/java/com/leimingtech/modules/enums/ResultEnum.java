/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * 结果集枚举
 *
 * @author chengqian
 */
public enum ResultEnum {

    /**
     * count结果集
     */
    RESULT_COUNT(0);


    private int value;

    ResultEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
