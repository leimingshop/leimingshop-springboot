/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.goodsclass;

/**
 * @Author: weixianchun
 * @Description:商品分类提示信息枚举类
 * @Date :2019/5/20 14:09
 * @Version V1.0
 **/
public enum GoodsClassErrorCodeEnum {

    /**
     * 301,商品分类名称重复
     */
    GOODS_GCNAME_REPEAT(301),
    /**
     * 301,商品分类关联商品
     */
    GOODS_CLASS_RELATION_GOODS(302),
    /**
     * 500，请先删除子菜单后再进行删除
     */
    DELETE_GOODCLASS_FAIL(303),

    /**
     * 商品分类不能少于三个
     */
    GOODS_CLASS_THREE(3),

    /**
     * 店铺商品分类不能少于三个
     */
    STORE_GOODS_CLASS_SECOND(2),

    /**
     * 0，删除成功
     */
    DELETE_GOODCLASS_SUCCESS(0);

    private int value;

    GoodsClassErrorCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
