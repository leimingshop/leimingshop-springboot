/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.webfloor;

/**
 * h5楼层图片链接关联类型
 *
 * @Description h5楼层图片链接关联类型
 * @Author 刘远杰
 * @Date 2019/5/13 12:12
 * Version 7.0
 **/
public enum LinkTypeEnum {
    /**
     * 链接
     */
    LINK("link"),
    /**
     * 分类
     */
    SEARCHGOODSBYCLASS("searchGoodsByClass"),
    /**
     * 关键字
     */
    SEARCHGBYKEYWORD("searchByKeyWord"),
    /**
     * 商品详情
     */
    GOODSDETAIL("goodsDetail"),
    /**
     * 限时抢购
     */
    XIANSHIQIANGLIST("xianshiqiangList");

    private String value;

    LinkTypeEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}
