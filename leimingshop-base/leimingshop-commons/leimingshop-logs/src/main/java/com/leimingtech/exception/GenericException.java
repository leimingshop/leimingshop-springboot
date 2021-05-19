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

public class GenericException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    private final Object errorCode;
    private Map<String, Object> context = new ConcurrentHashMap();

    public GenericException(String message) {
        super(message);
        this.errorCode = null;
    }

    public GenericException(String message, Throwable e) {
        super(message, e);
        this.errorCode = null;
    }

    public GenericException(Object errorCode) {
        super("ErrorCode=" + String.valueOf(errorCode));
        this.errorCode = errorCode;
    }

    public GenericException(Object errorCode, Throwable e) {
        super("ErrorCode=" + String.valueOf(errorCode), e);
        this.errorCode = errorCode;
    }

    public GenericException(Object errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public GenericException(Object errorCode, String message, Throwable e) {
        super(message, e);
        this.errorCode = errorCode;
    }

    private static String buildMessage(String message, Throwable cause) {
        if (cause != null) {
            StringBuilder sb = new StringBuilder();
            if (message != null) {
                sb.append(message).append("; ");
            }

            sb.append("nested exception is ").append(cause);
            return sb.toString();
        } else {
            return message;
        }
    }

    public void addContextValue(String key, Object value) {
        if (value == null) {
            this.context.remove(key);
        } else {
            this.context.put(key, value);
        }

    }

    public <T> T getContextValue(String key) {
        Object t = this.context.get(key);
        return (T) t;
    }

    public boolean isClientError() {
        return this.getErrorCode() instanceof String ? ((String) this.getErrorCode()).startsWith("4") : (this.getErrorCode() instanceof ServiceErrorCode ? ((ServiceErrorCode) this.getErrorCode()).isClientError() : false);
    }

    public boolean isServerError() {
        return !this.isClientError();
    }

    @Override
    public String getMessage() {
        return buildMessage(super.getMessage(), this.getCause());
    }

    public String getRawMessage() {
        return super.getMessage();
    }

    public Throwable getRootCause() {
        Object rootCause = this;

        for (Throwable cause = this.getCause(); cause != null && cause != rootCause; cause = cause.getCause()) {
            rootCause = cause;
        }

        return (Throwable) rootCause;
    }

    public Object getErrorCode() {
        return this.errorCode;
    }
}
