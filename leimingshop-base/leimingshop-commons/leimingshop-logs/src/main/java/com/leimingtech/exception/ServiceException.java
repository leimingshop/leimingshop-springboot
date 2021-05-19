//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.exception;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceException extends GenericException {
    private static final long serialVersionUID = 1L;
    private ServiceErrorCode errorCode;
    private Map<String, Object> context = new ConcurrentHashMap();

    public ServiceException(String message) {
        super(message);
        this.errorCode = null;
    }

    public ServiceException(String message, Throwable e) {
        super(message, e);
        this.errorCode = null;
    }

    public ServiceException(ServiceErrorCode errorCode) {
        super(errorCode.getCode(), errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ServiceException(ServiceErrorCode errorCode, Throwable e) {
        super(errorCode.getCode(), errorCode.getMessage(), e);
        this.errorCode = errorCode;
    }

    @Override
    public ServiceErrorCode getErrorCode() {
        return this.errorCode;
    }
}
