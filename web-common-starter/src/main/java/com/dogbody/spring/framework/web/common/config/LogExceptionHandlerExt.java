package com.dogbody.spring.framework.web.common.config;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhangdd on 2024/8/9
 */
@Slf4j
public class LogExceptionHandlerExt implements ExceptionHandlerExt {


    @Override
    public void businessError(Integer code, String message, Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void systemError(Integer code,String message, Throwable throwable) {
        log.error(message, throwable);
    }

    @Override
    public void paramInvalidError(Integer code,Object requestObj, String message, Throwable throwable) {
        log.error(message, throwable);
    }
}
