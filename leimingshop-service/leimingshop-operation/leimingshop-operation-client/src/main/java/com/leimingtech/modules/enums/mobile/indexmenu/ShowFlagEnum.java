/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.mobile.indexmenu;

/**
 * 功能描述：
 * <移动首页菜单展示状态枚举>
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/11 16:03
 **/
public enum ShowFlagEnum {
    /**
     * 不显示
     */
    NO(0),
    /**
     * 显示
     */
    YES(1);

    private int value;

    ShowFlagEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
