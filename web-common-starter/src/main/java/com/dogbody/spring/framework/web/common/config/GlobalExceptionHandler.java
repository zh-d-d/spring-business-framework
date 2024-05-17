package com.dogbody.spring.framework.web.common.config;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import com.dogbody.spring.framework.web.common.exception.BaseException;
import com.dogbody.spring.framework.web.common.response.ResponseModule;
import com.dogbody.spring.framework.web.common.util.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.ClassUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;

import static com.dogbody.spring.framework.web.common.constant.ResponseStatusConstant.PARAM_INVALID;
import static com.dogbody.spring.framework.web.common.constant.ResponseStatusConstant.SERVER_ERROR;


/**
 * @author zhangdd on 2023/6/6
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /*************************************  Business Exception Handing  *************************************/
    @ExceptionHandler(BaseException.class)
    public ResponseModule<String> handleBusinessException(BaseException ex) {
        logError(ex);
        return ResponseModule.fail(ex.getStatus(), ex.getMessage());
    }

    /*************************************  Common Exception Handing  *************************************/
    @ExceptionHandler(Throwable.class)
    public ResponseModule<String> handleAll(Throwable ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseModule<String> handleNullPointerException(NullPointerException ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseModule<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseModule<String> handleIllegalStateException(IllegalStateException ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseModule<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseModule<String> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        return loggingThenBuildResponse(ex);
    }

    /************************************* SQL Exception Handing  *************************************/
    @ExceptionHandler(SQLException.class)
    public ResponseModule<String> handleSQLException(SQLException ex) {
        return loggingThenBuildResponse(ex);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseModule<String> handleDataAccessException(DataAccessException ex) {
        return loggingThenBuildResponse(ex);
    }

    /************************************* Http Request Exception Handing  *************************************/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseModule<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String errorMsg = StrUtil.format("不支持该HTTP方法: {}, 请使用 {}", ex.getMethod(), Arrays.toString(ex.getSupportedMethods()));
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseModule<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String errorMsg = StrUtil.format("不支持该媒体类型: {}, 请使用 {}", ex.getContentType(), ex.getSupportedMediaTypes());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseModule<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        String errorMsg = StrUtil.format("不支持的媒体类型, 请使用 {}", ex.getSupportedMediaTypes());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    /************************************* Method Parameter Exception Handing  *************************************/

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseModule<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMsg = StrUtil.format("参数解析失败, {}", ex.getMessage());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseModule<String> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Object value = ex.getValue();
        String variableName = ex.getName();
        Class<?> requiredTypeClass = ex.getRequiredType();
        String requiredType = ClassUtils.getQualifiedName(requiredTypeClass == null ? Object.class : requiredTypeClass);
        String providedType = ClassUtils.getDescriptiveType(value);
        String errorMsg = StrUtil.format("参数类型不匹配, 参数名: {}, 需要: {}, 传入: {} 的 {}", variableName, requiredType, providedType, value);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseModule<String> handleMissingPathVariableException(MissingPathVariableException ex) {
        String errorMsg = StrUtil.format("丢失路径参数, 参数名: {}, 参数类型: {}", ex.getVariableName(), ex.getParameter().getParameterType());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseModule<String> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        String errorMsg = StrUtil.format("丢失Cookie参数, 参数名: {}, 参数类型: {}", ex.getCookieName(), ex.getParameter().getParameterType());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseModule<String> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        String errorMsg = StrUtil.format("丢失Header参数, 参数名: {}, 参数类型: {}", ex.getHeaderName(), ex.getParameter().getParameterType());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseModule<String> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        String errorMsg = StrUtil.format("丢失参数: {}", ex.getRequestPartName());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseModule<String> handleServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMsg = StrUtil.format("丢失Query参数, 参数名: {}, 参数类型: {}", ex.getParameterName(), ex.getParameterType());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseModule<String> handleServletRequestBindingException(ServletRequestBindingException ex) {
        String errorMsg = StrUtil.format("参数绑定失败: {}", ex.getMessage());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }


    /*************************************  Parameter Binding Exception Handing *************************************/

    /**
     * Valid 不加 @RequestBody 注解，校验失败抛出的则是 BindException
     */
    @ExceptionHandler(BindException.class)
    public ResponseModule<String> handleBindException(BindException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getBindingResult());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    /**
     * Valid 不加 @RequestBody  注解，校验失败抛出的则是 BindException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseModule<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getBindingResult());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    /**
     * Valid 使用 @RequestParam 上校验失败后抛出的异常是 ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseModule<String> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getConstraintViolations());
        log.error(errorMsg);
        return buildResponse(errorMsg, PARAM_INVALID);
    }

    private String buildBindingErrorMsg(BindingResult bindingResult) {
        String prefix = "参数验证失败: ";
        StringJoiner joiner = new StringJoiner(", ");
        for (ObjectError error : bindingResult.getAllErrors()) {
            String errorMessage = Optional.ofNullable(error.getDefaultMessage()).orElse("验证失败");
            String source;
            if (error instanceof FieldError) {
                source = ((FieldError) error).getField();
            } else {
                source = error.getObjectName();
            }
            joiner.add(source + " " + errorMessage);
        }
        return prefix + joiner;
    }

    private String buildBindingErrorMsg(Set<ConstraintViolation<?>> constraintViolations) {
        String prefix = "参数验证失败: ";
        StringJoiner joiner = new StringJoiner(", ");
        for (ConstraintViolation<?> violation : constraintViolations) {
            PathImpl propertyPath = (PathImpl) violation.getPropertyPath();
            joiner.add(propertyPath.asString() + " " + violation.getMessage());
        }
        return prefix + joiner;
    }

    private ResponseModule<String> loggingThenBuildResponse(Throwable throwable) {
        Throwable rootCause = ExceptionUtil.getRootCause(throwable);
        logError(rootCause);
        return buildResponse(rootCause.getMessage(), SERVER_ERROR);
    }

    private ResponseModule<String> buildResponse(String message, int code) {
        if (EnvUtils.isProduction()) {
            return ResponseModule.fail(code, "系统错误");
        }
        return ResponseModule.fail(code, message);
    }

    private void logError(Throwable throwable) {
        String exMsg = ExceptionUtil.getMessage(throwable);
        log.error(exMsg, throwable);
    }

}
