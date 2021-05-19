/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums;

/**
 * 系统设置的模块名称的枚举类
 */
public enum SettingsEnum {

    /**
     * 站点设置
     */
    SITE("site"),

    /**
     * 商品审核设置
     */
    GOODS_AUDIT("goodsAudit"),

    /**
     * 短信设置
     */
    MESSAGE("message"),

    /**
     * 公告提示设置
     */
    ANNOUNCEMENT_TIPS("announcementTips"),

    /**
     * 高级设置
     */
    SENIOR("senior_setting"),

    /**
     * 银行名称列表
     */
    BANK_NAME_LIST("bankNameList"),

    /**
     * 售后设置
     */
    AFTERSALE("aftersale"),

    /**
     * 快递设置
     */
    EXPRESS("express"),

    /**
     * 结算设置
     */
    SETTLEMENT("settlement"),

    /**
     * 对账设置
     */
    ORDER_BILL("orderBill"),

    /**
     * 发票设置
     */
    INVOICE("invoice"),

    /**
     * 秒杀设置
     */
    SECKILL("seckill_setting"),

    /**
     * 默认头像设置
     */
    DEFAULT_AVATARS("defaultAvatars"),


    /**
     * 购物车公告提示开启
     */
    CART_TIPS_OPEN("1"),

    /**
     * 商品审核设置开启1是0否
     */
    GOODS_AUDIT_OPEN("1"),
    GOODS_AUDIT_CLOSE("0");

    private String value;

    SettingsEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
