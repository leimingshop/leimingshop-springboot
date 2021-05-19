/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.commons.tools.exception;

/**
 * sentinel自定义异常，用于服务端传递异常至客户端
 *
 * @author lixiang
 * @version V1.0
 * @date 2020/7/14 9:52
 **/
public class SentinelBadRequestException extends RuntimeException {

    private static final long serialVersionUID = 898368825979433545L;

    public SentinelBadRequestException(String message) {
        super(message);
    }

    public SentinelBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
