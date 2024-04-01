package com.dogbody.spring.framework.web.common.servlet;


import com.dogbody.spring.framework.web.common.util.TraceIdUtil;
import org.springframework.web.context.request.RequestContextListener;

import javax.servlet.ServletRequestEvent;

/**
 * @author liaozan
 * @since 2021/12/8
 */
public class RequestLifecycleListener extends RequestContextListener {

    @Override
    public void requestInitialized(ServletRequestEvent event) {
        super.requestInitialized(event);
        // make sure the traceId exist
        TraceIdUtil.get();
    }

    @Override
    public void requestDestroyed(ServletRequestEvent event) {
        super.requestDestroyed(event);
        // make sure the traceId can be cleared
        TraceIdUtil.clear();
    }

}