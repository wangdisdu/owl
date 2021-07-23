package com.owl.api.reflect;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.owl.api.annotation.DataType;
import com.owl.api.annotation.Parameter;
import com.owl.api.annotation.ParameterMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParameterScanner {

    public static List<ParameterMeta> scan(Class<?> configClass) {
        Map<String, ParameterMeta> params = scanParameters(configClass);
        List<ParameterMeta> list = new ArrayList<>(params.values());
        return list.stream().sorted().collect(Collectors.toList());
    }

    protected static Map<String, ParameterMeta> scanParameters(Class<?> configClass) {
        Map<String, ParameterMeta> result = new LinkedHashMap<>();
        List<Field> fields = listFields(configClass);
        for (Field field : fields) {
            Parameter annotation = field.getAnnotation(Parameter.class);
            if (annotation == null) {
                continue;
            }
            ParameterMeta meta = getParameterMeta(field, annotation);
            result.put(meta.getName(), meta);
        }
        return result;
    }

    protected static ParameterMeta getParameterMeta(Field field, Parameter annotation) {
        Class<?> fieldClass = TypeUtil.getClass(field);
        DataType fieldDataType = ParamTypeAnalyzer.analyze(fieldClass);
        ParameterMeta meta = new ParameterMeta();
        meta.setType(fieldDataType);
        meta.setName(StrUtil.isNotEmpty(annotation.name()) ? annotation.name() : field.getName());
        meta.setDisplay(StrUtil.isNotEmpty(annotation.display()) ? annotation.display() : field.getName());
        meta.setDescription(StrUtil.isNotEmpty(annotation.description()) ? annotation.description() : field.getName());
        meta.setPlaceholder(annotation.placeholder());
        meta.setCandidates(annotation.candidates());
        meta.setRequired(annotation.required());
        meta.setOrder(annotation.order());
        meta.setPassword(annotation.password());
        if (DataType.LIST == fieldDataType) {
            Type fieldType = TypeUtil.getType(field);
            Type actualType = TypeUtil.getTypeArgument(fieldType);
            Class<?> actualClass = TypeUtil.getClass(actualType);
            DataType actualDataType = ParamTypeAnalyzer.analyze(actualClass);
            ParameterMeta actualMeta = new ParameterMeta();
            actualMeta.setType(actualDataType);
            meta.setListParameter(actualMeta);
        }
        return meta;
    }

    private static List<Field> listFields(Class<?> beanClass) {
        if (beanClass == null) {
            return new ArrayList<>();
        }

        List<Field> list = new ArrayList<>();

        Class<?> superClass = beanClass.getSuperclass();
        if (superClass != null) {
            List<Field> superList = listFields(superClass);
            list.addAll(superList);
        }

        Field[] declaredFields = beanClass.getDeclaredFields();
        Collections.addAll(list, declaredFields);
        return list;
    }
}
