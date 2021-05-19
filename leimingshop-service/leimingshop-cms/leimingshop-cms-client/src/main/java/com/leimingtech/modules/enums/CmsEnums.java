/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums;

/**
 * @author lixiang
 * @version V1.0
 * @我的类型枚举
 * @date 2019/12/26 17:58
 **/
public enum CmsEnums {

    /**
     * 艾特我的全部文章信息
     */
    ALL_ARTICLE("1"),

    /**
     * 艾特我的，我所关注的用户发布的文章
     */
    ALL_NWE_ARTICLE("2"),

    /**
     * 艾特我的所有评论
     */
    ALL_EVALUATE("3"),

    /**
     * 艾特我的，我所关注用户发布的评论
     */
    ALL_NEW_EVALUATE("4");

    private String value;

    CmsEnums(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }
}
