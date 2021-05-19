/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.remind;

/**
 * 提醒枚举
 *
 * @author chengqian
 */
public enum RemindTypeEnum {

    /**
     * 1，文章
     */
    ARTICLE_CODE(1),
    /**
     * 文章点赞
     */
    ARTICLE_REMIND(3),
    /**
     * 2，评论
     */
    COMMENT_CODE(2);

    private int value;

    RemindTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
