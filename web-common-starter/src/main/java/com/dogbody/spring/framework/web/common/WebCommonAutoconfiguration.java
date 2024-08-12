package com.dogbody.spring.framework.web.common;

import com.dogbody.spring.framework.web.common.config.ExceptionHandlerExt;
import com.dogbody.spring.framework.web.common.config.GlobalExceptionHandler;
import com.dogbody.spring.framework.web.common.config.LogExceptionHandlerExt;
import com.dogbody.spring.framework.web.common.servlet.RequestLifecycleListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class WebCommonAutoconfiguration {

    @Bean
    public LogExceptionHandlerExt logExceptionHandlerExt() {
        return new LogExceptionHandlerExt();
    }


    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler defaultGlobalExceptionHandler(List<ExceptionHandlerExt> exceptionHandlerExtList) {
        return new GlobalExceptionHandler(exceptionHandlerExtList);
    }

    @Bean
    @ConditionalOnMissingBean
    public RequestLifecycleListener requestLifecycleListener() {
        return new RequestLifecycleListener();
    }
}
