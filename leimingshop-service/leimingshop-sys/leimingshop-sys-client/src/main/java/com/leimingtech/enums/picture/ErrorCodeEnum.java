/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.enums.picture;

import java.io.Serializable;

/**
 * @ClassName:ErrorCodeEnum
 * @Data:2019/5/13 9:28
 * @Author：chengqian
 * @Version 1.0
 */
public enum ErrorCodeEnum implements Serializable {

    /**
     * 0，上传成功
     */
    SUCCESS(0),
    /**
     * 201，非法图片
     */
    ILLEGAL_IMAGES(201),
    /**
     * 301分组名称重复
     */
    CATEGORY_REPETITION(301),
    /**
     * 图片分组不能为空
     */
    PICTURE_CATEGORY_IS_NULL(302),
    /**
     * 图片名称限制
     */
    PICTURE_NAME_SIZE(100),

    /**
     * 图片不能为空
     */
    PICTURE_IS_NULL(202),

    NO_CATEGORY_PICTURE("未分组图片");


    private int value;
    private String typeValue;

    ErrorCodeEnum(String typeValue) {
        this.typeValue = typeValue;
    }

    ErrorCodeEnum(int value) {
        this.value = value;
    }

    public String typeValue() {
        return this.typeValue;
    }

    public int value() {
        return this.value;
    }
}
