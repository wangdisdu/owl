package com.owl.web.model;

import com.owl.web.common.ResponseCode;

public class ResponseResult {
    private String retCode;
    private String retMsg;
    private Object result;
    private Long total;

    public ResponseResult() {
        this.retCode = ResponseCode.SUCCESS.code;
        this.retMsg = ResponseCode.SUCCESS.message;
    }

    public ResponseResult(ResponseCode code) {
        this.retCode = code.code;
        this.retMsg = code.message;
    }

    public String getRetCode() {
        return retCode;
    }

    public ResponseResult setRetCode(String retCode) {
        this.retCode = retCode;
        return this;
    }

    public String getRetMsg() {
        return retMsg;
    }

    public ResponseResult setRetMsg(String retMsg) {
        this.retMsg = retMsg;
        return this;
    }

    public Object getResult() {
        return result;
    }

    public ResponseResult setResult(Object result) {
        this.result = result;
        return this;
    }

    public Long getTotal() {
        return total;
    }

    public ResponseResult setTotal(Long total) {
        this.total = total;
        return this;
    }
}
