package com.owl.web.common;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.owl.web.dao.entity.TbUser;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ContextHolder implements ApplicationContextAware {
    public static final ThreadLocal<RequestContext> holder = new TransmittableThreadLocal();
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static RequestContext requestContext() {
        return holder.get();
    }

    public static TbUser loginUser() {
        RequestContext ctx = requestContext();
        if(ctx == null) {
            return null;
        }
        return ctx.getLogin();
    }

    public static String loginAccount() {
        TbUser user = loginUser();
        if(user == null) {
            return null;
        }
        return user.getAccount();
    }
}
