//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.logging;

import ch.qos.logback.classic.pattern.ThrowableHandlingConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.CoreConstants;
import ch.qos.logback.core.LayoutBase;
import com.leimingtech.system.ApplicationPid;
import com.leimingtech.tracing.ServiceContext;
import org.springframework.boot.logging.logback.ExtendedWhitespaceThrowableProxyConverter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

public class LogStandardLayout extends LayoutBase<ILoggingEvent> {
    private final String pid = (new ApplicationPid()).toString();
    private final ThrowableHandlingConverter throwableProxyConverter = new ExtendedWhitespaceThrowableProxyConverter();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private String format;

    public LogStandardLayout() {
        this.throwableProxyConverter.start();
    }

    @Override
    public String doLayout(ILoggingEvent event) {
        LinkedHashMap fields = new LinkedHashMap();
        fields.put("timeStamp", dateFormat.format(new Date(event.getTimeStamp())));
        fields.put("traceId", ServiceContext.getInstance().getTraceId());
        fields.put("spanId", ServiceContext.getInstance().getSpanId());
        fields.put("parentSpanId", ServiceContext.getInstance().getParentSpanId());
        fields.put("requestId", ServiceContext.getInstance().getRequestId());
        fields.put("invocationId", ServiceContext.getInstance().getInvocationId());
        fields.put("pid", this.pid);
        fields.put("level", event.getLevel().levelStr);
        fields.put("threadName", event.getThreadName());
        fields.put("loggerName", event.getLoggerName());
        fields.put("appID", System.getProperty("SYSTEM_ID"));
        if (MonitorLogger.MONITOR_EVENT_MARKER.equals(event.getMarker())) {
            fields.put("type", "M");
        } else {
            fields.put("type", (Object) null);
        }

        return "text".equalsIgnoreCase(this.format) ? this.applyTextFormat(fields.entrySet().iterator(), event) : this.applyJsonFormat(fields.entrySet().iterator(), event);
    }

    protected String applyTextFormat(Iterator<Entry<String, String>> entries, ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();

        while (entries.hasNext()) {
            String value = (String) ((Entry) entries.next()).getValue();
            sb.append("[").append(value == null ? "" : value).append("]");
        }

        sb.append(event.getFormattedMessage());
        sb.append(CoreConstants.LINE_SEPARATOR);
        if (event.getThrowableProxy() != null) {
            sb.append(this.throwableProxyConverter.convert(event));
        }

        return sb.toString();
    }

    protected String applyJsonFormat(Iterator<Entry<String, String>> entries, ILoggingEvent event) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        while (entries.hasNext()) {
            Entry entry = (Entry) entries.next();
            if (entry.getValue() != null) {
                this.appendValue(sb, (String) entry.getKey(), (String) entry.getValue(), sb.length() != 1);
            }
        }

        if (MonitorLogger.MONITOR_EVENT_MARKER.equals(event.getMarker())) {
            sb.append(",");
            sb.append(event.getFormattedMessage());
        } else {
            this.appendValue(sb, "Message", event.getFormattedMessage(), true);
        }

        if (event.getThrowableProxy() != null) {
            this.appendValue(sb, "exception", this.throwableProxyConverter.convert(event), true);
        }

        sb.append("}").append(CoreConstants.LINE_SEPARATOR);
        return sb.toString();
    }

    private StringBuilder appendValue(StringBuilder sb, String key, String value, boolean addPrefix) {
        if (value == null) {
            return sb;
        } else {
            if (addPrefix) {
                sb.append(",");
            }

            sb.append(MonitorLogger.quote(key)).append(":");
            sb.append(MonitorLogger.quote(value));
            return sb;
        }
    }

    public String getFormat() {
        return this.format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
