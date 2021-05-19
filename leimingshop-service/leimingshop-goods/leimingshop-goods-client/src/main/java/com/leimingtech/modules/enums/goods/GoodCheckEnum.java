/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;


/**
 * 商品审核状态枚举类
 *
 * @author huangjianeng
 * @since v1.0.0 2020-03-05
 */
public enum GoodCheckEnum {
    /**
     * 商户提交发布商品
     */
    GOOD_CHECK_SUB(1, "发布商品", "商品上架申请"),
    /**
     * 商户提交发布商品
     */
    GOOD_CHECK_EDIT(2, "更新商品", "商品上架申请"),

    /**
     * 平台审核通过
     */
    GOOD_CHECK_PASS(3, "审核", "审核通过"),

    /**
     * 平台审核拒绝
     */
    GOOD_CHECK_FAIL(4, "审核", "审核拒绝");

    private int value;
    private String type;
    private String content;

    GoodCheckEnum(int value, String type, String content) {
        this.value = value;
        this.type = type;
        this.content = content;
    }

    public int getValue() {
        return this.value;
    }

    public String getType() {
        return this.type;
    }

    public String getContent() {
        return this.content;
    }
}
