package com.dogbody.spring.framework.web.common.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author zhangdd on 2023/6/7
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BaseException extends RuntimeException {

    protected final int status;

    public BaseException(int status, String message) {
        this(status, message, null);
    }

    public BaseException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

}
