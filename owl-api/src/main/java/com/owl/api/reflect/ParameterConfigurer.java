package com.owl.api.reflect;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ReflectUtil;
import com.owl.api.annotation.DataType;
import com.owl.api.annotation.KeyValue;
import com.owl.api.annotation.Parameter;
import com.owl.api.annotation.ParameterMeta;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParameterConfigurer {

    public static void config(Object configBean, Map<String, Object> configMap) {
        if (configMap == null) {
            configMap = new HashMap<>();
        }
        configParameters(configBean, configMap);
    }

    protected static void configParameters(Object configBean, Map<String, Object> configMap) {
        Class<?> configClass = configBean.getClass();
        Field[] fields = ReflectUtil.getFields(configClass);
        for (Field field : fields) {
            Parameter annotation = field.getAnnotation(Parameter.class);
            if (annotation == null) {
                continue;
            }
            ParameterMeta parameterMeta = ParameterScanner.getParameterMeta(field, annotation);
            Object value = configMap.get(parameterMeta.getName());
            if (value == null) {
                continue;
            }
            Object fieldValue = getParamValue(parameterMeta, value);
            BeanUtil.setProperty(configBean, field.getName(), fieldValue);
        }
    }

    protected static Object getParamValue(ParameterMeta parameterMeta, Object value) {
        DataType fieldDataType = parameterMeta.getType();
        if (DataType.LIST == fieldDataType) {
            List result = new ArrayList();
            List list = (List) value;
            for (Object item : list) {
                result.add(ParamValueConverter.convert(parameterMeta.getListParameter().getType(), item));
            }
            return result;
        } else if (DataType.KV == fieldDataType) {
            KeyValue result = new KeyValue();
            Map fromMap = (Map) value;
            for (Object k : fromMap.keySet()) {
                Object v = fromMap.get(k);
                result.put(ParamValueConverter.convertString(k), ParamValueConverter.convertString(v));
            }
            return result;
        }
        return ParamValueConverter.convert(fieldDataType, value);
    }
}
