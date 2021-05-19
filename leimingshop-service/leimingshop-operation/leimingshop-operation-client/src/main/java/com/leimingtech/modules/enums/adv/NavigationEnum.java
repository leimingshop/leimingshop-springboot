/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.adv;

/**
 * pc导航类型枚举
 *
 * @Description pc导航类型枚举
 * @Author chengqian
 * @Date 2020/9/17 09:16
 * Version 7.0
 **/
public enum NavigationEnum {

    /**
     * 关联链接
     */
    URL(1),
    /**
     * 关联分类
     */
    STORE_CLASS(2);

    private int value;

    NavigationEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }

}
