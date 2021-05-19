/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 提交订单状态码
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/4/1 14:35
 **/
public enum CodeStatusEnum {

    /**
     * 提货码状态（默认 0待核销，1:部分核销，2:已核销，3:已过期）
     */
    WATTING_CHECK(0),
    PART_CHECK(1),
    ALL_CHECK(2),
    EXPIRED(3);

    private int value;

    CodeStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
