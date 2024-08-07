package com.dogbody.spring.framework.sequence.exception;

/**
 * @author zhangdd on 2024/8/6
 */
public class SequenceException extends RuntimeException {
    public SequenceException() {
    }

    public SequenceException(String message) {
        super(message);
    }

    public SequenceException(String message, Throwable cause) {
        super(message, cause);
    }

    public SequenceException(Throwable cause) {
        super(cause);
    }

    public SequenceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
