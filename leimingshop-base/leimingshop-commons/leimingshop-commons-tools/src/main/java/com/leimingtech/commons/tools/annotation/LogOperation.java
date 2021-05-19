/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 *
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String value() default "";
}