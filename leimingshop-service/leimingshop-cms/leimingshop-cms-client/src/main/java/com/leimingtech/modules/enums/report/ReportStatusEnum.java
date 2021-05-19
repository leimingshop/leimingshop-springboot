/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.report;

/**
 * 文章状态枚举
 *
 * @author chengqian
 */
public enum ReportStatusEnum {

    /**
     * 1,待处理
     */
    DISPOSE_CODE(1),
    /**
     * 2,通过
     */
    SUCCESS_CODE(2),
    /**
     * 3,不通过
     */
    FAIL_CODE(3);
    private int value;

    ReportStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
