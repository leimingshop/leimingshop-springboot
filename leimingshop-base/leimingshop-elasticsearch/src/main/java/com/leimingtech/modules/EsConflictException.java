/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.modules;

/**
 * <es乐观锁冲突异常>
 *
 * @author 刘远杰
 * @email 2634443725@qq.com
 * @since 1.0 2020/4/14
 */
public class EsConflictException extends RuntimeException {

    static final long serialVersionUID = 1L;

    public EsConflictException() {
        super("索引乐观锁冲突，更新失败");
    }

    protected EsConflictException(String message, Throwable cause,
                                  boolean enableSuppression,
                                  boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
