//package com.leimingtech.modules.utils;
//
//import java.lang.annotation.*;
//
//@Target({ElementType.FIELD,ElementType.METHOD})
//@Retention(RetentionPolicy.RUNTIME)
//@Documented
//public @interface FieldInfo {
//
//
//    /**
//     * @string    text ,keyword
//     * @Numeric   long, integer, short, byte, double, float, half_float, scaled_float
//     * @date      date
//     * @date_nanos date_nanos
//     * @Range      integer_range, float_range, long_range, double_range, date_range
//     * @binary    binary
//     */
//     String type() default "string";
//
//    /**
//     * 分词器选择  0. not_analyzed   1. ik_smart 2. ik_max_word
//     */
//     int participle() default 0;
//
//
//}
