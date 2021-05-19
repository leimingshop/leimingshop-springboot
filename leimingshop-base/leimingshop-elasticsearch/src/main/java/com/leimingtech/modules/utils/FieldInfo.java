/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules.utils;

import java.lang.annotation.*;

/**
 * 自定义ES注解，配置实体类型、分词规则
 *
 * @author lixiangx@leimingtech.com
 * @date 2019/12/10 17:07
 **/
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FieldInfo {

    /**
     * @string text ,keyword
     * @Number long, integer, short, byte, double, float, half_float, scaled_float
     * @date date
     * @date_nanos date_nanos
     * @Range integer_range, float_range, long_range, double_range, date_range
     * @binary binary
     * @Nested nested
     */
    String type() default "string";

    /**
     * 分词器选择  0. not_analyzed   1. ik_smart 2. ik_max_word  3.ik-index(自定义分词器)
     */
    int participle() default 0;

    /**
     * 是否使用拼音分词 默认0 否、1 是
     */
    int pinyin() default 0;
}
