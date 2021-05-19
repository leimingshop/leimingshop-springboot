/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.util.http;


import java.io.IOException;
import java.util.Map;

/**
 * http请求执行器.
 *
 * @param <T> 返回值类型
 * @param <E> 请求参数类型
 * @author Daniel Qian
 */
public interface RequestExecutor<T, E> {

    /**
     * 执行http请求.
     *
     * @param uri  uri
     * @param data 数据
     * @return 响应结果
     * @throws IOException io异常
     */
    T execute(String uri, E data) throws IOException;

    /**
     * 执行http请求.
     *
     * @param uri     uri
     * @param data    数据
     * @param handler http响应处理器
     * @throws IOException io异常
     */
    void execute(String uri, E data, ResponseHandler<T> handler) throws IOException;

    /**
     * 执行http请求.
     *
     * @param uri  uri
     * @param data 数据
     * @throws IOException io异常
     */
    T execute(String uri, E data, Map<String, String> headers) throws IOException;

    /**
     * 执行http请求.
     *
     * @param uri     uri
     * @param data    数据
     * @param handler http响应处理器
     * @throws IOException io异常
     */
    void execute(String uri, E data, Map<String, String> headers, ResponseHandler<T> handler) throws IOException;


}
