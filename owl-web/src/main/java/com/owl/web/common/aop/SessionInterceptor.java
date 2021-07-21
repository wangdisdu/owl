package com.owl.web.common.aop;

import cn.hutool.core.util.StrUtil;
import com.owl.web.common.ContextHolder;
import com.owl.web.common.RequestContext;
import com.owl.web.common.ResponseCode;
import com.owl.web.common.ResponseHelper;
import com.owl.web.common.annotation.LoginRequired;
import com.owl.web.common.config.AppConfig;
import com.owl.web.dao.entity.TbUser;
import com.owl.web.model.ResponseResult;
import com.owl.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {
        if (!isNeedLogin(handler)) {
            return true;
        }
        TbUser user = readUser(request, response);
        if (user != null) {
            ContextHolder.holder.set(new RequestContext(user));
            return true;
        }
        ResponseHelper.writeJson(
                response,
                new ResponseResult(ResponseCode.FAILED),
                HttpServletResponse.SC_UNAUTHORIZED);
        return false;
    }

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response,
                                Object handler,
                                @Nullable Exception ex) {
        ContextHolder.holder.remove();
    }

    private boolean isNeedLogin(Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return false;
        }
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        LoginRequired methodLogin = handlerMethod.getMethodAnnotation(LoginRequired.class);
        if (methodLogin != null) {
            return methodLogin.required();
        }
        Class<?> clazz = handlerMethod.getBeanType();
        LoginRequired clazzLogin = clazz.getAnnotation(LoginRequired.class);
        if (clazzLogin != null) {
            return clazzLogin.required();
        }
        return false;
    }

    private TbUser readUser(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        String token = null;
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (AppConfig.COOKIE_NAME.equalsIgnoreCase(cookie.getName())) {
                    token = cookie.getValue();
                    //发现新Cookie跳出循环
                    break;
                }
            }
        }
        TbUser user = null;
        if (StrUtil.isNotEmpty(token)) {
            user = userService.getUserByToken(token);
        }
        if (user != null) {
            ResponseHelper.refreshCookie(response, token);
        }
        return user;

    }
}
