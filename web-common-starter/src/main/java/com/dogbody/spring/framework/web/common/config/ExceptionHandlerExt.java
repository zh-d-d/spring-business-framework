package com.dogbody.spring.framework.web.common.config;

/**
 * {@link GlobalExceptionHandler}是全局异常处理器，拦截全局异常进行友好返回给UI。
 * 该接口用于扩展其他形式的异常信息处理
 * 1.将异常日志通过日志门面进行输出
 * 2.webhook通知
 * //其实日志也是一种形式的处理
 *
 * @author zhangdd on 2024/8/9
 */
public interface ExceptionHandlerExt {

    default void businessError(Integer code, String errorMsg, Throwable throwable) {
    }

    default void systemError(Integer code,String errorMsg, Throwable throwable) {

    }

    default void paramInvalidError(Integer code,Object requestObj, String errorMsg, Throwable throwable) {

    }

}
