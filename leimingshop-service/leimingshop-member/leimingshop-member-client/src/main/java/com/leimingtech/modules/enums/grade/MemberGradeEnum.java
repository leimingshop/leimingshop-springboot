/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.grade;

/**
 * 用户等级枚举
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
public enum MemberGradeEnum {
    /**
     * 是否是默认:非默认
     */
    DEFAULT_FLAG_DEFAULT(0),
    /**
     * 是否是默认:默认
     */
    DEFAULT_FLAG_UNDEFAULT(1);

    private int value;

    MemberGradeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
