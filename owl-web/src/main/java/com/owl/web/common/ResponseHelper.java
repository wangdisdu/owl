package com.owl.web.common;

import com.owl.common.JsonUtil;
import com.owl.web.common.config.AppConfig;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

public class ResponseHelper {

    public static void writeJsonString(HttpServletResponse response, String json, Integer status) throws IOException {
        if (response == null) {
            throw new IOException("response is null");
        }
        response.setStatus(status);
        response.setContentType(AppConfig.APPLICATION_JSON_UTF8_VALUE);
        response.setHeader(HttpHeaders.CONTENT_TYPE, AppConfig.APPLICATION_JSON_UTF8_VALUE);
        response.getWriter().write(json);
        response.flushBuffer();
    }

    public static void writeJson(HttpServletResponse response, Object json, Integer status) throws IOException {
        writeJsonString(response, JsonUtil.encode(json), status);
    }

    public static void refreshCookie(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie(AppConfig.COOKIE_NAME, token);
        cookie.setMaxAge(AppConfig.COOKIE_TTL);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void clearCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(AppConfig.COOKIE_NAME, "invalid");
        cookie.setMaxAge(0);
        cookie.setPath("/api/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }
}
