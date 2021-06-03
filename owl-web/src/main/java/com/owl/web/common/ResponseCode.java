package com.owl.web.common;

public enum ResponseCode {
    SUCCESS("0000", "成功"),
    FAILED("1000", "失败"),
    INVALID_PARAM("1001", "非法参数"),
    EXIST("1002", "已存在，无法继续"),
    NOT_EXIST("1003", "不存在，无法继续"),
    USED("1004", "使用中，无法继续"),
    UNDEFINED("9999", "内部错误，请联系管理员");

    public final String code;
    public final String message;

    ResponseCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
