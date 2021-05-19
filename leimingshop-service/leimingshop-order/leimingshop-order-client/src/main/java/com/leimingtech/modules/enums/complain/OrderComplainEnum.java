/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.complain;

/**
 * 投诉枚举
 */
public enum OrderComplainEnum {
    //是否投诉 0 未投诉 1 已投诉
    COMPLAIN_FLAG_NO(0),
    COMPLAIN_FLAG_YES(1),
    //是否展示 0 展示 1 不展示
    SHOW_FLAG_YES(0),
    SHOW_FLAG_NO(1),
    //投诉状态 10：待处理 20：已处理
    STATUS_NO(10),
    STATUS_YES(20);
    private Integer value;


    OrderComplainEnum(Integer value) {
        this.value = value;
    }

    public Integer value() {
        return this.value;
    }

}
