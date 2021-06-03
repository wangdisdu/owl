package com.owl.web.common.config;

public class AppConfig {
    public static final Integer MAX_PAGE_ROW_COUNT = 10000;
    public static final Integer MAX_QUERY_ROW_COUNT = 10000;
    public static final String SALT = "448DDD517D3ABB70045AEA6929F02367";
    public static final String COOKIE_NAME = "OWLTOKEN";
    public static final int COOKIE_TTL = 7 * 24 * 3600;
    public static final int COOKIE_TTL_MS = COOKIE_TTL * 1000;
    public static final String APPLICATION_JSON_UTF8_VALUE = "application/json;charset=UTF-8";

}
