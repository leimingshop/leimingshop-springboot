/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.comment;

/**
 * @Author: yuhaifeng
 * @Description:评论提示信息枚举类
 * @Date :2020/3/11 16:09
 * @Version V1.0
 **/
public enum CommentErrorCodeEnum {

    /**
     * 304，删除失败
     */
    DELETE_CLASS_FAIL(304),
    /**
     * 0，删除成功
     */
    DELETE_CLASS_SUCCESS(0);

    private int value;

    CommentErrorCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
