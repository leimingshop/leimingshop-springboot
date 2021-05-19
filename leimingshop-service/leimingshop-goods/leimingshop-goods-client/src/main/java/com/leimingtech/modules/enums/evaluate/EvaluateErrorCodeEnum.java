/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.enums.evaluate;

/**
 * @Description 评论错误码
 * @Data: 2019/5/23 13:55
 * @Author: chengqian
 * @Version: 1.0
 */
public enum EvaluateErrorCodeEnum {

    /**
     * 上传失败
     */
    UPLOAD_ERROR(201),
    /**
     * 评论不存在
     */
    NOT_BLANK(301);

    private int value;

    EvaluateErrorCodeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
