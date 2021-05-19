/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.brand;

/**
 * @Author: weixianchun
 * @Description:品牌提示信息枚举类
 * @Date :2019/5/20 14:09
 * @Version V1.0
 **/
public enum BrandErrorCodeEnum {

    /**
     * 201，品牌名称不能为空
     */
    ILLEGAL_IMAGES(201),
    /**
     * 申请状态 0申请中
     */
    BRAND_APPLYING(0),
    /**
     * 申请状态 1通过
     */
    BRAND_APPLY_PASS(1),
    /**
     * 301,品牌名称不能重复
     */
    CATEGORY_REPETITION(301),
    /**
     * 302,品牌关联商品不可删除
     */
    BRAND_GOODS(302),
    /**
     * 303,品牌英文名称必须是大写或小写英文字母
     */
    BRAND_BRANDNAMEEN_CHECK(303),
    /**
     * 304,品牌关联商品
     */
    BRAND_RELATION_GOODS(304);

    private int value;

    BrandErrorCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
