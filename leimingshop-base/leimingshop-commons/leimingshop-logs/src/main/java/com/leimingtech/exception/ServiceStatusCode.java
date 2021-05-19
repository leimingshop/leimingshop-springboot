//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.exception;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class ServiceStatusCode implements ServiceErrorCode {
    public static final ServiceStatusCode SUCCESSFUL_OK = new ServiceStatusCode.InternalServiceStatusCode("200000", "处理成功", new Object[0]);
    public static final ServiceStatusCode CLIENT_BAD_REQUEST_ERROR = new ServiceStatusCode.InternalServiceStatusCode("4XX000", "非法请求", new Object[0]);
    public static final ServiceStatusCode CLIENT_VALIDATION_ERROR = new ServiceStatusCode.InternalServiceStatusCode("400001", "请求参数校验异常", new Object[0]);
    public static final ServiceStatusCode CLIENT_DUPLICATE_REQUEST_ERROR = new ServiceStatusCode.InternalServiceStatusCode("400002", "重复请求", new Object[0]);
    public static final ServiceStatusCode CLIENT_UNAUTHENTICATED_ERROR = new ServiceStatusCode.InternalServiceStatusCode("401000", "未认证的请求", new Object[0]);
    public static final ServiceStatusCode CLIENT_UNAUTHORIZED_ERROR = new ServiceStatusCode.InternalServiceStatusCode("403000", "未授权的请求", new Object[0]);
    public static final ServiceStatusCode CLIENT_REQUEST_NOT_FOUND_ERROR = new ServiceStatusCode.InternalServiceStatusCode("404000", "请求不存在", new Object[0]);
    public static final ServiceStatusCode CLIENT_REQUEST_TIMEOUT_ERROR = new ServiceStatusCode.InternalServiceStatusCode("408000", "请求超时", new Object[0]);
    public static final ServiceStatusCode SERVER_INTERNAL_ERROR = new ServiceStatusCode.InternalServiceStatusCode("5XX000", "系统异常", new Object[0]);
    public static final ServiceStatusCode SERVER_RESPONSE_VALIDATION_ERROR = new ServiceStatusCode.InternalServiceStatusCode("500001", "返回内容校验异常", new Object[0]);
    public static final ServiceStatusCode SERVER_DB_ERROR = new ServiceStatusCode.InternalServiceStatusCode("500002", "数据库异常", new Object[0]);
    public static final ServiceStatusCode SERVER_UNAVAILABLE_ERROR = new ServiceStatusCode.InternalServiceStatusCode("503000", "服务不可用", new Object[0]);
    public static final ServiceStatusCode SERVER_PROCESSING_TIMEOUT_ERROR = new ServiceStatusCode.InternalServiceStatusCode("504000", "系统处理超时", new Object[0]);
    public static final Pattern PATTERN = Pattern.compile("[{]([^}]*?)[}]");
    private static final long serialVersionUID = 1L;
    private final String code;
    private final String message;
    private final Object[] arguments;

    protected ServiceStatusCode(String code, String message, Object... arguments) {
        if (code != null && code.trim().length() == 6 && (code.startsWith("2") || code.startsWith("4") || code.startsWith("5")) && !"0".equals(code.substring(3, 4))) {
            this.code = code.trim();
            this.message = message;
            this.arguments = arguments;
        } else {
            throw new IllegalArgumentException("应用状态码不合法. 须是6位长度;2开头的为成功,4开头为客户端错误,5开头为服务器端错误;第4个字符应该是非0");
        }
    }

    private ServiceStatusCode(boolean internal, String code, String message, Object... arguments) {
        this.code = code.trim();
        this.message = message;
        this.arguments = arguments;
    }

    public static boolean isSuccessful(String code) {
        if (code == null) {
            throw new IllegalArgumentException("code can\'t be null!");
        } else {
            return code.startsWith("2");
        }
    }

    public static boolean isClientError(String code) {
        if (code == null) {
            throw new IllegalArgumentException("code can\'t be null!");
        } else {
            return code.startsWith("4");
        }
    }

    public static boolean isServerError(String code) {
        if (code == null) {
            throw new IllegalArgumentException("code can\'t be null!");
        } else {
            return code.startsWith("5");
        }
    }

    public abstract ServiceStatusCode copy(String var1, Object... var2);

    @Override
    public final String getCode() {
        return this.code;
    }

    @Override
    public final String getMessage() {
        if (this.message == null) {
            return null;
        } else {
            StringBuffer result = new StringBuffer();
            Matcher matcher = PATTERN.matcher(Matcher.quoteReplacement(this.message));

            Object value;
            for (int count = 0; matcher.find(); matcher.appendReplacement(result, value == null ? matcher.group() : value.toString())) {
                String key = matcher.group(1);
                value = null;
                if ((key == null || "".equals(key.trim())) && this.arguments != null && this.arguments.length > count) {
                    value = this.arguments[count];
                    ++count;
                }
            }

            matcher.appendTail(result);
            return result.toString();
        }
    }

    public final boolean isSuccessful() {
        return this.getCode().startsWith("2");
    }

    @Override
    public final boolean isClientError() {
        return this.getCode().startsWith("4");
    }

    @Override
    public final boolean isServerError() {
        return this.getCode().startsWith("5");
    }

    @Override
    public final int getHttpStatus() {
        return Integer.valueOf(this.getCode().substring(0, 3).replaceAll("[xX]", "0")).intValue();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof ServiceStatusCode)) {
            return false;
        } else {
            ServiceStatusCode other = (ServiceStatusCode) o;
            if (!other.canEqual(this)) {
                return false;
            } else {
                String this$code = this.code;
                String other$code = other.code;
                if (this$code == null) {
                    if (other$code != null) {
                        return false;
                    }
                } else if (!this$code.equals(other$code)) {
                    return false;
                }

                return true;
            }
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof ServiceStatusCode;
    }

    @Override
    public int hashCode() {
        boolean PRIME = true;
        byte result = 1;
        String $code = this.code;
        return result * 59 + ($code == null ? 43 : $code.hashCode());
    }

    @Override
    public String toString() {
        return "ServiceStatusCode(super=" + super.toString() + ", code=" + this.code + ", message=" + this.message + ", arguments=" + Arrays.deepToString(this.arguments) + ")";
    }

    public static class InternalServiceStatusCode extends ServiceStatusCode {
        private static final long serialVersionUID = 1L;

        public InternalServiceStatusCode(String code, String message, Object... arguments) {
            super(true, code, message, arguments, null);
        }

        @Override
        public ServiceStatusCode.InternalServiceStatusCode copy(String customMessage, Object... arguments) {
            return new ServiceStatusCode.InternalServiceStatusCode(this.getCode(), customMessage, arguments);
        }
    }
}
