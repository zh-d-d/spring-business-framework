package com.dogbody.spring.framework.web.common.config;

import cn.hutool.core.util.StrUtil;
import com.dogbody.spring.framework.web.common.exception.BaseException;
import com.dogbody.spring.framework.web.common.response.ResponseModule;
import com.dogbody.spring.framework.web.common.util.EnvUtils;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.engine.path.PathImpl;
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
import java.util.*;

import static com.dogbody.spring.framework.web.common.constant.ResponseStatusConstant.PARAM_INVALID;
import static com.dogbody.spring.framework.web.common.constant.ResponseStatusConstant.SERVER_ERROR;


/**
 * @author zhangdd on 2023/6/6
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final List<ExceptionHandlerExt> exceptionHandlerExtList;

    public GlobalExceptionHandler(List<ExceptionHandlerExt> exceptionHandlerExtList) {
        this.exceptionHandlerExtList = exceptionHandlerExtList;
    }

    /*************************************  Business Exception Handing  *************************************/
    @ExceptionHandler(BaseException.class)
    public ResponseModule<String> handleBusinessException(BaseException ex) {
        exceptionHandlerExtList.forEach(item -> item.handlerError(ex.getStatus(), ex.getMessage(), ex));
        return ResponseModule.fail(ex.getStatus(), ex.getMessage());
    }

    /*************************************  Common Exception Handing  *************************************/
    @ExceptionHandler(Throwable.class)
    public ResponseModule<String> handleAll(Throwable ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseModule<String> handleNullPointerException(NullPointerException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseModule<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseModule<String> handleIllegalStateException(IllegalStateException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseModule<String> handleNoHandlerFoundException(NoHandlerFoundException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(AsyncRequestTimeoutException.class)
    public ResponseModule<String> handleAsyncRequestTimeoutException(AsyncRequestTimeoutException ex) {
        return buildResponse(ex);
    }

    /************************************* SQL Exception Handing  *************************************/
    @ExceptionHandler(SQLException.class)
    public ResponseModule<String> handleSQLException(SQLException ex) {
        return buildResponse(ex);
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseModule<String> handleDataAccessException(DataAccessException ex) {
        return buildResponse(ex);
    }

    /************************************* Http Request Exception Handing  *************************************/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseModule<String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException ex) {
        String errorMsg = StrUtil.format("不支持该HTTP方法: {}, 请使用 {}", ex.getMethod(), Arrays.toString(ex.getSupportedMethods()));
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseModule<String> handleHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException ex) {
        String errorMsg = StrUtil.format("不支持该媒体类型: {}, 请使用 {}", ex.getContentType(), ex.getSupportedMediaTypes());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseModule<String> handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex) {
        String errorMsg = StrUtil.format("不支持的媒体类型, 请使用 {}", ex.getSupportedMediaTypes());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    /************************************* Method Parameter Exception Handing  *************************************/

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseModule<String> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String errorMsg = StrUtil.format("参数解析失败, {}", ex.getMessage());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseModule<String> handlerMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Object value = ex.getValue();
        String variableName = ex.getName();
        Class<?> requiredTypeClass = ex.getRequiredType();
        String requiredType = ClassUtils.getQualifiedName(requiredTypeClass == null ? Object.class : requiredTypeClass);
        String providedType = ClassUtils.getDescriptiveType(value);
        String errorMsg = StrUtil.format("参数类型不匹配, 参数名: {}, 需要: {}, 传入: {} 的 {}", variableName, requiredType, providedType, value);
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseModule<String> handleMissingPathVariableException(MissingPathVariableException ex) {
        String errorMsg = StrUtil.format("丢失路径参数, 参数名: {}, 参数类型: {}", ex.getVariableName(), ex.getParameter().getParameterType());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MissingRequestCookieException.class)
    public ResponseModule<String> handleMissingRequestCookieException(MissingRequestCookieException ex) {
        String errorMsg = StrUtil.format("丢失Cookie参数, 参数名: {}, 参数类型: {}", ex.getCookieName(), ex.getParameter().getParameterType());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseModule<String> handleMissingRequestHeaderException(MissingRequestHeaderException ex) {
        String errorMsg = StrUtil.format("丢失Header参数, 参数名: {}, 参数类型: {}", ex.getHeaderName(), ex.getParameter().getParameterType());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseModule<String> handleMissingServletRequestPartException(MissingServletRequestPartException ex) {
        String errorMsg = StrUtil.format("丢失参数: {}", ex.getRequestPartName());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseModule<String> handleServletRequestParameterException(MissingServletRequestParameterException ex) {
        String errorMsg = StrUtil.format("丢失Query参数, 参数名: {}, 参数类型: {}", ex.getParameterName(), ex.getParameterType());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ResponseModule<String> handleServletRequestBindingException(ServletRequestBindingException ex) {
        String errorMsg = StrUtil.format("参数绑定失败: {}", ex.getMessage());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }


    /*************************************  Parameter Binding Exception Handing *************************************/

    /**
     * Valid 不加 @RequestBody 注解，校验失败抛出的则是 BindException
     */
    @ExceptionHandler(BindException.class)
    public ResponseModule<String> handleBindException(BindException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getBindingResult());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    /**
     * Valid 不加 @RequestBody  注解，校验失败抛出的则是 BindException
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseModule<String> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getBindingResult());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
    }

    /**
     * Valid 使用 @RequestParam 上校验失败后抛出的异常是 ConstraintViolationException
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseModule<String> handleConstraintViolationException(ConstraintViolationException ex) {
        String errorMsg = buildBindingErrorMsg(ex.getConstraintViolations());
        return buildResponse(PARAM_INVALID, errorMsg, ex);
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

    private ResponseModule<String> buildResponse(Throwable throwable) {
        return buildResponse(SERVER_ERROR, "", throwable);
    }

    private ResponseModule<String> buildResponse(int code, String message, Throwable throwable) {
        exceptionHandlerExtList.forEach(item -> item.handlerError(code, message, throwable));
        if (EnvUtils.isProduction()) {
            return ResponseModule.fail(code, "系统错误");
        }
        return ResponseModule.fail(code, message);
    }
}
