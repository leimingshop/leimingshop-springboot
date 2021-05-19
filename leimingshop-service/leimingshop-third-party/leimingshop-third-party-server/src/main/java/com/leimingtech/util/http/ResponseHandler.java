/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;

/**
 * @param <T> 返回值类型
 */
public interface ResponseHandler<T> {
    /**
     * 响应结果处理.
     *
     * @param t 要处理的对象
     */
    void handle(T t);
}
