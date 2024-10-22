package com.dogbody.spring.framework.web.common.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangdd on 2024/8/9
 */
@Slf4j
public class LogExceptionHandlerExt implements ExceptionHandlerExt {


    @Override
    public void handlerError(Integer code, String message, Throwable throwable) {
        log.error(message, throwable);
    }
}
