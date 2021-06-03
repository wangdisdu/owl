package com.owl.api.reflect;

import cn.hutool.core.util.TypeUtil;
import com.owl.api.annotation.Integration;
import com.owl.api.annotation.IntegrationMeta;
import com.owl.api.annotation.ParameterMeta;

import java.lang.reflect.Type;
import java.util.List;

public class IntegrationScanner {

    public static IntegrationMeta scan(Class<?> targetClass) {
        Integration annotation = targetClass.getAnnotation(Integration.class);
        IntegrationMeta meta = get(targetClass, annotation);
        Type configType = TypeUtil.getTypeArgument(targetClass);
        Class<?> configClass = TypeUtil.getClass(configType);
        List<ParameterMeta> parameters = ParameterScanner.scan(configClass);
        meta.setParameters(parameters);
        return meta;
    }

    protected static IntegrationMeta get(Class<?> clazz, Integration annotation) {
        IntegrationMeta meta = new IntegrationMeta();
        meta.setBuilder(clazz.getCanonicalName());
        meta.setDisplay(clazz.getSimpleName());
        if(annotation != null) {
            meta.setDisplay(annotation.display());
            meta.setDescription(annotation.description());
            meta.setIcon(annotation.icon());
            meta.setDocument(annotation.document());
        }
        return meta;
    }
}
