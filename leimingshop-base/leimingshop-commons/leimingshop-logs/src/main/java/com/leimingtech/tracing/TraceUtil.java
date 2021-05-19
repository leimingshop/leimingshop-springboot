//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.tracing;

import org.slf4j.MDC;

public class TraceUtil {
    private static final String KEY_TRACE_ID = "trace-id";
    private static final String KEY_SPAN_ID = "span-id";
    private static final String KEY_PARENT_SPAN_ID = "parent-id";

    public TraceUtil() {
    }

    public static String getTraceId() {
        return get("trace-id");
    }

    public static String getSpanId() {
        return get("span-id");
    }

    public static String getParentSpanId() {
        return get("parent-id");
    }

    public static String get(String key) {
        return MDC.get(key);
    }
}
