/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.report;

/**
 * 文章枚举
 *
 * @author chengqian
 */
public enum ReportFlagEnum {

    /**
     * 1，文章
     */
    ARTICLE_CODE(1);

    private int value;

    ReportFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
