/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.webfloor;

/**
 * @Description h5楼层图片是否展示枚举
 * @Author 刘远杰
 * @Date 2019/5/13 12:12
 * Version 7.0
 **/
public enum IsShowEnum {
    /**
     * 1 h5楼层 2 pc 楼层
     */
    PC_FLOOR(2),
    H5_FLOOR(1),
    /***
     * 展示样式1 图片2 商品
     */
    PIC_STYLE(1),
    GOODS_STYLE(2),
    YES(1),
    NO(0);

    private int value;

    IsShowEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
