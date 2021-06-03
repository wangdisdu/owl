package com.owl.web.common.exception;

import com.owl.web.common.ResponseCode;
import com.owl.web.model.ResponseResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;

@ResponseBody
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @Autowired
    private HttpServletRequest request;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult exceptionHandler(Exception exception) {
        logger.debug("trace error, uri: {}", request.getRequestURI(), exception);
        if (exception instanceof MethodArgumentTypeMismatchException) {
            throw (MethodArgumentTypeMismatchException) exception;
        } else if (exception instanceof BizException) {
            BizException ex = (BizException) exception;
            return new ResponseResult().setRetCode(ex.getCode()).setRetMsg(ex.getMessage());
        } else {
            logger.error("trace error, uri: {}", request.getRequestURI(), exception);
            return new ResponseResult()
                    .setRetCode(ResponseCode.UNDEFINED.code)
                    .setRetMsg(ResponseCode.UNDEFINED.message);
        }
    }
}
