//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.logging;

import com.leimingtech.exception.ServiceErrorCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.Iterator;
import java.util.Map;

public class MonitorLogger {
    public static final Marker MONITOR_EVENT_MARKER = MarkerFactory.getMarker("MONITOR_EVENT");
    public static final String MONITOR_CODE_KEY = "Messagecode";
    public static final String MSG_KEY = "Message";
    public static final String APP_ID_KEY = "appID";
    private static final Logger log = LoggerFactory.getLogger(MonitorLogger.class);
    private final Logger wrapped;

    protected MonitorLogger(Logger wrapped) {
        this.wrapped = wrapped;
    }

    public static String quote(String string) {
        if (string != null && string.length() != 0) {
            boolean c = false;
            int len = string.length();
            StringBuilder sb = new StringBuilder(len + 4);
            sb.append('\"');

            for (int i = 0; i < len; ++i) {
                char var6 = string.charAt(i);
                switch (var6) {
                    case '\b':
                        sb.append("\\b");
                        break;
                    case '\t':
                        sb.append("\\t");
                        break;
                    case '\n':
                        sb.append("\\n");
                        break;
                    case '\f':
                        sb.append("\\f");
                        break;
                    case '\r':
                        sb.append("\\r");
                        break;
                    case '\"':
                    case '\\':
                        sb.append('\\');
                        sb.append(var6);
                        break;
                    case '/':
                        sb.append('\\');
                        sb.append(var6);
                        break;
                    default:
                        if (var6 < 32) {
                            String t = "000" + Integer.toHexString(var6);
                            sb.append("\\u").append(t.substring(t.length() - 4));
                        } else {
                            sb.append(var6);
                        }
                }
            }

            sb.append('\"');
            return sb.toString();
        } else {
            return "\"\"";
        }
    }

    public static String toJson(String monitorCode, String message, Map<String, String> context) {
        StringBuilder sb = new StringBuilder();
        if (monitorCode != null && monitorCode.trim().length() > 0) {
            if (sb.length() > 1) {
                sb.append(',');
            }

            sb.append(quote("Messagecode")).append(":").append(quote(monitorCode));
        }

        if (message != null && message.trim().length() > 0) {
            if (sb.length() > 1) {
                sb.append(',');
            }

            sb.append(quote("Message")).append(":").append(quote(message));
        }

        String key;
        if (context != null) {
            Iterator content = context.keySet().iterator();

            label92:
            while (true) {
                String i$;
                do {
                    do {
                        do {
                            do {
                                if (!content.hasNext()) {
                                    break label92;
                                }

                                i$ = (String) content.next();
                                key = (String) context.get(i$);
                            } while (key == null);
                        } while ("Messagecode".equals(i$));
                    } while ("Message".equals(i$));
                }
                while (!"responseTime".equalsIgnoreCase(i$) && !"appID".equalsIgnoreCase(i$) && !"BASY-ID".equalsIgnoreCase(i$) && !"SEQ-ID".equalsIgnoreCase(i$) && !"SeqNo".equalsIgnoreCase(i$));

                if (sb.length() > 1) {
                    sb.append(',');
                }

                sb.append(quote(i$));
                sb.append(':');
                sb.append(quote(key));
                content.remove();
                context.remove(i$);
            }
        }

        if (sb.length() > 1) {
            sb.append(',');
        }

        sb.append("\"content\"").append(":{");
        StringBuilder content1 = new StringBuilder();
        if (context != null) {
            Iterator i$1 = context.keySet().iterator();

            while (i$1.hasNext()) {
                key = (String) i$1.next();
                String value = (String) context.get(key);
                if (value != null && !"Messagecode".equals(key) && !"Message".equals(key)) {
                    if (content1.length() > 1) {
                        content1.append(',');
                    }

                    content1.append(quote(key));
                    content1.append(':');
                    content1.append(quote(value));
                }
            }
        }

        sb.append(content1);
        sb.append('}');
        return sb.toString();
    }

    private void selfError(String monitorCode, String oriMessage, Map<String, String> context, Throwable throwable) {
        log.error(MONITOR_EVENT_MARKER, "{\"{}\":\"{}\",\"{}\":\"打印监控日志的时候出错\"}", new Object[]{"Messagecode", monitorCode, "Message", throwable});
    }

    public boolean isInfoEnabled() {
        return this.wrapped.isInfoEnabled(MONITOR_EVENT_MARKER);
    }

    public void info(String monitorCode, String message) {
        this.info(monitorCode, message, (Map) null, (Throwable) null);
    }

    public void info(String monitorCode, String message, Throwable throwable) {
        this.info(monitorCode, message, (Map) null, throwable);
    }

    public void info(String monitorCode, String message, Map<String, String> context) {
        this.info(monitorCode, message, context, (Throwable) null);
    }

    public void info(String monitorCode, String message, Map<String, String> context, Throwable throwable) {
        if (this.isInfoEnabled()) {
            monitorCode = this.resolveMonitorCode(monitorCode, "I");

            try {
                this.wrapped.info(MONITOR_EVENT_MARKER, toJson(monitorCode, message, context), throwable);
            } catch (Exception e) {
                this.selfError(monitorCode, message, context, throwable);
            }
        }

    }

    public boolean isWarnEnabled() {
        return this.wrapped.isWarnEnabled(MONITOR_EVENT_MARKER);
    }

    public void warn(String monitorCode, String message) {
        this.warn(monitorCode, message, (Map) null, (Throwable) null);
    }

    public void warn(String monitorCode, String message, Throwable throwable) {
        this.warn(monitorCode, message, (Map) null, throwable);
    }

    public void warn(String monitorCode, String message, Map<String, String> context) {
        this.warn(monitorCode, message, context, (Throwable) null);
    }

    public void warn(String monitorCode, String message, Map<String, String> context, Throwable throwable) {
        if (this.isWarnEnabled()) {
            monitorCode = this.resolveMonitorCode(monitorCode, "W");

            try {
                this.wrapped.warn(MONITOR_EVENT_MARKER, toJson(monitorCode, message, context), throwable);
            } catch (Exception e) {
                this.selfError(monitorCode, message, context, throwable);
            }
        }

    }

    public boolean isErrorEnabled() {
        return this.wrapped.isErrorEnabled(MONITOR_EVENT_MARKER);
    }

    public void error(String monitorCode, String message) {
        this.error(monitorCode, message, (Map) null, (Throwable) null);
    }

    public void error(String monitorCode, String message, Throwable throwable) {
        this.error(monitorCode, message, (Map) null, throwable);
    }

    public void error(String monitorCode, String message, Map<String, String> context) {
        this.error(monitorCode, message, context, (Throwable) null);
    }

    public void error(String monitorCode, String message, Map<String, String> context, Throwable throwable) {
        if (this.isErrorEnabled()) {
            monitorCode = this.resolveMonitorCode(monitorCode, "E");

            try {
                this.wrapped.error(MONITOR_EVENT_MARKER, toJson(monitorCode, message, context), throwable);
            } catch (Exception e) {
                this.selfError(monitorCode, message, context, throwable);
            }
        }

    }

    public void logEvent(ServiceErrorCode statusCode) {
        this.logEvent(statusCode, (Map) null, (Throwable) null);
    }

    public void logEvent(ServiceErrorCode statusCode, Throwable throwable) {
        this.logEvent(statusCode, (Map) null, throwable);
    }

    public void logEvent(ServiceErrorCode statusCode, Map<String, String> context) {
        this.logEvent(statusCode, context, (Throwable) null);
    }

    public void logEvent(ServiceErrorCode statusCode, Map<String, String> context, Throwable throwable) {
        if (statusCode != null && statusCode.isClientError()) {
            this.warn(statusCode.getCode(), statusCode.getMessage(), context, throwable);
        } else if (statusCode != null && statusCode.isServerError()) {
            this.error(statusCode.getCode(), statusCode.getMessage(), context, throwable);
        } else {
            this.info(statusCode == null ? null : statusCode.getCode(), statusCode == null ? null : statusCode.getMessage(), context, throwable);
        }

    }

//    public void logEvent(Map<String, String> context, ServiceException serviceException) {
//        this.logEvent(ServiceErrorCodeResolver.getInstance().resolve(serviceException), context, serviceException);
//    }
//
//    public void logEvent(ServiceException serviceException) {
//        this.logEvent(ServiceErrorCodeResolver.getInstance().resolve(serviceException), (Map)null, serviceException);
//    }
//
//    public void logMonitorCode(MonitorErrorCode monitorErrorCode, Map<String, String> context, Throwable throwable) {
//        this.error(monitorErrorCode.getCode(), monitorErrorCode.getMessage(), context, throwable);
//    }

    private String resolveMonitorCode(String monitorCode, String prefix) {
        if (monitorCode != null && monitorCode.length() == 6) {
            String errorType = "S";
            if (monitorCode.startsWith("4")) {
                errorType = "B";
            } else if (monitorCode.startsWith("5")) {
                errorType = "S";
            } else if (monitorCode.startsWith("2")) {
                errorType = "B";
            }

            monitorCode = prefix + errorType + System.getProperty("SYSTEM_ID") + monitorCode;
        }

        return monitorCode;
    }
}
