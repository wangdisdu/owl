package com.owl.web.common.exception;

import com.owl.web.common.ResponseCode;

public class BizException extends RuntimeException {
    private final String code;
    private final String message;
    private Throwable cause;

    public BizException(ResponseCode code) {
        this(code.code, code.message);
    }

    public BizException(ResponseCode code, Throwable cause) {
        this(code.code, code.message, cause);
    }

    public BizException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BizException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    public BizException setCause(Throwable cause) {
        this.cause = cause;
        return this;
    }
}
