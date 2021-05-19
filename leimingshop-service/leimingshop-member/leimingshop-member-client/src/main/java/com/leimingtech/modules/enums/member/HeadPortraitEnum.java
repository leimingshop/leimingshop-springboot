/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.member;

/**
 * 默认头像信息
 *
 * @author xuzhch
 * @date 2020年9月18日
 */
public enum HeadPortraitEnum {

    /**
     * 用户默认头像
     */
    MEMBER_HEADIMG("/group1/M00/00/04/wKgBbF0UX-SAYxbpAAKyM4Q0U_o788.png");

    private String value;

    HeadPortraitEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
