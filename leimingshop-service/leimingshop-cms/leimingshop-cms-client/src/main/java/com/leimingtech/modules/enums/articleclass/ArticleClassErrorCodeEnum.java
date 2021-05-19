/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.articleclass;

/**
 * @Author: yuhaifeng
 * @Description:服务指南分类提示信息枚举类
 * @Date :2020/3/11 16:09
 * @Version V1.0
 **/
public enum ArticleClassErrorCodeEnum {

    /**
     * 301,分类名称重复
     */
    ACNAME_REPEAT(301),
    /**
     * 302,分类存在关联文章
     */
    CLASS_RELATION_ARTICLE(302),
    /**
     * 303，分类存在关联话题
     */
    CLASS_RELATION_TOPIC(303),
    /**
     * 304，请先删除子分类后再进行删除
     */
    DELETE_CLASS_FAIL(304),
    /**
     * 0，删除成功
     */
    DELETE_CLASS_SUCCESS(0);

    private int value;

    ArticleClassErrorCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
