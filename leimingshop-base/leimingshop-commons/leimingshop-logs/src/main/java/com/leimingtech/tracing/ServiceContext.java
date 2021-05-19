//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

/**
 *  http://www.leimingtech.com 雷铭科技
 */

package com.leimingtech.tracing;

import org.slf4j.MDC;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ServiceContext {
    public static final String KEY_REQUEST_ID = "requestId";
    public static final String KEY_INVOCATION_ID = "invocationId";
    private static final String KEY_PREFIX = "serviceContext.";
    private static ServiceContext instance = new ServiceContext();

    private ServiceContext() {
    }

    public static ServiceContext getInstance() {
        return instance;
    }

    public String getRequestId() {
        return this.get("requestId");
    }

    public void setRequestId(String value) {
        this.set("requestId", value);
    }

    public String getInvocationId() {
        return this.get("invocationId");
    }

    public void setInvocationId(String value) {
        this.set("invocationId", value);
    }

    public String getTraceId() {
        return TraceUtil.getTraceId();
    }

    public String getSpanId() {
        return TraceUtil.getSpanId();
    }

    public String getParentSpanId() {
        return TraceUtil.getParentSpanId();
    }

    public String get(String key) {
        return MDC.get("serviceContext." + key);
    }

    public void set(String key, String value) {
        key = "serviceContext." + key;
        if (value == null) {
            MDC.remove(key);
        } else {
            MDC.put(key, value);
        }

    }

    public void clear() {
        Map context = MDC.getCopyOfContextMap();
        if (context != null) {
            Iterator iterator = context.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.startsWith("serviceContext.")) {
                    MDC.remove(key);
                }
            }
        }

    }

    public Map<String, String> getCopyOfContextMap() {
        HashMap context = new HashMap();
        Map mdcContext = MDC.getCopyOfContextMap();
        if (mdcContext != null) {
            Iterator iterator = mdcContext.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                if (key.startsWith("serviceContext.")) {
                    context.put(key.substring("serviceContext.".length()), mdcContext.get(key));
                }
            }
        }

        return context;
    }

    public void setContextMap(Map<String, String> contextMap) {
        if (contextMap != null) {
            Iterator iterator = contextMap.keySet().iterator();

            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                this.set(key, (String) contextMap.get(key));
            }
        }

    }
}
