/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goods;

/**
 * @ClassName GoodsStatusEnum
 * @Description 商品枚举
 * @Author DY
 * @Date 2019/6/4 18:12
 * @Version 1.0
 **/
public enum GoodsStatusEnum {


    /**
     * 商品上架
     */
    GOODS_SHOW_ON(1),

    /**
     * 商品下架
     */
    GOODS_SHOW_DOWN(0),

    /**
     * 商品未上架
     */
    GOODS_SHOW_NO(2),

    /**
     * 商品状态:待审核
     */
    GOODS_AUDIT_WAIT(10),

    /**
     * 商品状态:审核未通过
     */
    GOODS_AUDIT_NOTPASS(20),

    /**
     * 商品状态:审核通过
     */
    GOODS_AUDIT_PASS(30),

    /**
     * 商品状态:违规下架
     */
    GOODS_AUDIT_VIOLATION(40),

    /**
     * 商品状态:未发布
     */
    GOODS_AUDIT_UNPUBLISHED(50),

    /**
     * 商品状态:草稿
     */
    GOODS_DRAFT(60),

    /**
     * 运费承担方式:买家承担
     */
    GOODS_TRANSFEE_CHARGE_BUYER(0),

    /**
     * 运费承担方式:卖家承担
     */
    GOODS_TRANSFEE_CHARGE_SELLER(1),

    /**
     * 商品上下架类型:定时上下架
     */
    GOODS_SHOW_TYPE_TIMING(1),

    /**
     * 商品上下架类型:立即上下架
     */
    GOODS_SHOW_TYPE_NOW(0),

    /**
     * 商品发布类型;立即发布
     */
    GOODS_PUBLISH_NOW(0),

    /**
     * 商品发布类型:定时发布
     */
    GOODS_PUBLISH_TIMING(1),
    /**
     * 商品发布类型:草稿
     */
    GOODS_PUBLISH_DRAFT(2),
    /**
     * 新增属性值
     */
    GOODS_ADD_NEW(1),

    /**
     * 原有
     */
    GOODS_ADD_OLD(0),

    /**
     * 商品规格属性 选中
     */
    SPEC_ATTR_CHECKED(1),

    /**
     * 商品规格属性 未选中
     */
    SPEC_ATTR_UNCHECKED(0),

    /**
     * 新增属性和属性值
     */
    GOODS_ADD_NEWS(2),

    /**
     * 主图
     */
    IS_MAIN_PICTURE(1),

    /**
     * 操作对象: 0平台,1商户
     */
    IS_ADMIN(0),
    IS_SELLER(1),
    /**
     * 是否补充商品（0：不补充 1：补充）
     */
    ADD_FLAG_NO(0),
    ADD_FLAG_YES(1),

    /**
     * 草是否发布(上架)过（默认0:不是，1:是）
     */
    NO_PUBLISH_FLAG(0),
    IS_PUBLISH_FLAG(1),
    /**
     * 是否是虚拟订单
     */
    IS_VIRTUAL_FLAG(0),
    NO_VIRTUAL_FLAG(1);

    private int value;

    GoodsStatusEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
