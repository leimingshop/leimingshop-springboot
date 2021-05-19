/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;

/**
 * 缩略图枚举
 *
 * @author chengqian
 */
public enum CustomPictureSizeEnum {

    //100像素
    ONE_HUNDRED_PX("_100x100."),
    //200像素
    TWO_HUNDRED_PX("_200x200."),
    //400像素
    FOUR_HUNDRED_PX("_400x400."),
    //800像素
    EIGHT_HUNDRED_PX("_800x800.");

    private String value;

    CustomPictureSizeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }


}
