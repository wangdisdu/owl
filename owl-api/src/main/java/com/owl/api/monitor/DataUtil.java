package com.owl.api.monitor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.owl.api.reflect.ParamValueConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    public static String genGuid(String... field) {
        String str = StrUtil.join("/", field);
        return SecureUtil.md5(str);
    }

    public static Data[] read(Object obj) {
        List<Data> result = new ArrayList<>();
        Class<?> clazz = obj.getClass();
        Field[] fields = ReflectUtil.getFields(clazz);
        Long time = null;
        String instance = null;
        String category = null;
        String host = null;
        List<String> tags = new ArrayList<>();
        for (Field field : fields) {
            Value va = field.getAnnotation(Value.class);
            if (va != null) {
                Data data = new Data();
                data.setValue(ParamValueConverter.convertDouble(
                        ReflectUtil.getFieldValue(obj, field)
                ));
                data.setMetric(
                        CharSequenceUtil.toUnderlineCase(field.getName())
                );
                result.add(data);
            }
            if (field.getAnnotation(Time.class) != null) {
                time = ParamValueConverter.convertLong(
                        ReflectUtil.getFieldValue(obj, field)
                );
            }
            if (field.getAnnotation(Instance.class) != null) {
                instance = ParamValueConverter.convertString(
                        ReflectUtil.getFieldValue(obj, field)
                );
            }
            if (field.getAnnotation(Category.class) != null) {
                category = ParamValueConverter.convertString(
                        ReflectUtil.getFieldValue(obj, field)
                );
            }
            if (field.getAnnotation(Host.class) != null) {
                host = ParamValueConverter.convertString(
                        ReflectUtil.getFieldValue(obj, field)
                );
            }
            if (field.getAnnotation(Tag.class) != null) {
                tags.add(ParamValueConverter.convertString(
                        ReflectUtil.getFieldValue(obj, field)
                ));
            }
        }
        for (Data data : result) {
            if (time != null) {
                data.setTime(time);
            }
            if (instance != null) {
                data.setInstance(instance);
            }
            if (category != null) {
                data.setCategory(category);
            }
            if (host != null) {
                data.setHost(host);
            }
            if (tags.size() > 0) {
                data.setTag1(tags.get(0));
            }
            if (tags.size() > 1) {
                data.setTag2(tags.get(1));
            }
            if (tags.size() > 2) {
                data.setTag3(tags.get(2));
            }
            if (tags.size() > 3) {
                data.setTag4(tags.get(3));
            }
            if (tags.size() > 4) {
                data.setTag5(tags.get(4));
            }
        }
        return result.toArray(new Data[0]);
    }
}
