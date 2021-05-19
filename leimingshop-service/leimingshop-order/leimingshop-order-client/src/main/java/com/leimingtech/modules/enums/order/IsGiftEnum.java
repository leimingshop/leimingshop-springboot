/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.order;

/**
 * 功能描述：
 * <是否赠送商品枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/19 9:46
 **/
public enum IsGiftEnum {

    /**
     * 否
     */
    NO(0),

    /**
     * 是
     */
    YES(1);

    private int value;

    IsGiftEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
