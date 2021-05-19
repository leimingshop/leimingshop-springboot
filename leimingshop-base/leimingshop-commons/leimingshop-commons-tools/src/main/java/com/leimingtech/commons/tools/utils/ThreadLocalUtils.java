/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.utils;


import com.leimingtech.commons.tools.annotation.LogContext;
import com.leimingtech.commons.tools.annotation.LogContextParam;

/**
 * @Author: SWH ab4856812@163.com
 * @Description:
 * @Date: 2019/6/29 18:54
 * @Version: V1.0
 */
public class ThreadLocalUtils {

    /**
     * 声明一个上下文线程本地变量
     **/
    public static final ThreadLocal<LogContext> logContextThreadLocal = new ThreadLocal<>();
    /**
     * 声明一个sdk业务参数本地变量
     **/
    public static final ThreadLocal<LogContextParam> logContextServiceParamThreadLocal = new ThreadLocal<>();

    private ThreadLocalUtils() {
    }
}
