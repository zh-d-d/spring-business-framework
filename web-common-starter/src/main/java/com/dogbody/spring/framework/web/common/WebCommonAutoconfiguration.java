package com.dogbody.spring.framework.web.common;

import com.dogbody.spring.framework.web.common.config.GlobalExceptionHandler;
import com.dogbody.spring.framework.web.common.servlet.RequestLifecycleListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

public class WebCommonAutoconfiguration {

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler defaultGlobalExceptionHandler() {
        return new GlobalExceptionHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLifecycleListener requestLifecycleListener() {
        return new RequestLifecycleListener();
    }
}
