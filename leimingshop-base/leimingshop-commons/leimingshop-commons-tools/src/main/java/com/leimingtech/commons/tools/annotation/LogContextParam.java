/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.annotation;

import java.lang.annotation.*;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/6/29 18:37
 * @Version: V1.0
 */
@Documented
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface LogContextParam {

    /**
     * @return
     */
    String name() default "";
}
