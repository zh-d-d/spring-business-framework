package com.dogbody.spring.framework.web.common.util;

import cn.hutool.core.util.IdUtil;
import org.slf4j.MDC;

/**
 * @author liaozan
 * @since 2021/10/10
 */
public class TraceIdUtil {

    public static final String TRACE_ID = "traceId";

    private static final ThreadLocal<String> TRACE_ID_CONTAINER = InheritableThreadLocal.withInitial(TraceIdUtil::create);

    public static String get() {
        return TRACE_ID_CONTAINER.get();
    }

    public static void set(String traceId) {
        if (traceId == null) {
            return;
        }
        MDC.put(TRACE_ID, traceId);
        TRACE_ID_CONTAINER.set(traceId);
    }

    public static void clear() {
        MDC.remove(TRACE_ID);
        TRACE_ID_CONTAINER.remove();
    }

    private static String create() {
        String traceId = MDC.get(TRACE_ID);
        if (traceId == null) {
            traceId = IdUtil.randomUUID().toLowerCase();
            MDC.put(TRACE_ID, traceId);
        }
        return traceId;
    }

}