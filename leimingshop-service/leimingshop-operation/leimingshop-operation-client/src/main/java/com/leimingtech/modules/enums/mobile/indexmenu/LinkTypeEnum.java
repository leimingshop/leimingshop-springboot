/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.mobile.indexmenu;

/**
 * 移动首页菜单跳转类型枚举
 *
 * @author 刘远杰
 * @version 1.0
 * @Date 2019/6/11 16:03
 **/
public enum LinkTypeEnum {
    /**
     * 外部链接
     */
    URL("url"),
    /**
     * 优惠券
     */
    COUPON("coupon"),
    /**
     * 拼团
     */
    GROUP("group"),
    /**
     * 秒杀
     */
    SECKILL("seckill"),
    /**
     * 停用
     */
    FLASHSALE("flashsale");

    private String value;

    LinkTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
