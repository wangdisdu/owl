package com.owl.api.monitor;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.owl.api.reflect.ParamValueConverter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MetricUtil {

    public static String genGuid(String... field) {
        String str = StrUtil.join("/", field);
        return SecureUtil.md5(str);
    }

    public static Metric[] read(Object obj) {
        List<Metric> result = new ArrayList<>();
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
                Metric metric = new Metric();
                String name = va.name();
                if (StrUtil.isEmpty(name)) {
                    name = CharSequenceUtil.toUnderlineCase(field.getName());
                }
                String alias = va.alias();
                String unit = va.unit();
                Double value = ParamValueConverter.convertDouble(
                        ReflectUtil.getFieldValue(obj, field)
                );
                metric.setMetric(name);
                metric.setAlias(alias);
                metric.setUnit(unit);
                metric.setValue(value);
                result.add(metric);
            }
            if (field.getAnnotation(Time.class) != null) {
                time = ParamValueConverter.convertLong(
                        ReflectUtil.getFieldValue(obj, field)
                );
            }
            if (field.getAnnotation(Instance.class) != null) {
                instance = (instance == null ? "" : instance + "-") + ParamValueConverter.convertString(
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
        for (Metric metric : result) {
            if (time != null) {
                metric.setTime(time);
            }
            if (instance != null) {
                metric.setInstance(instance);
            }
            if (category != null) {
                metric.setCategory(category);
            }
            if (host != null) {
                metric.setHost(host);
            }
            if (tags.size() > 0) {
                metric.setTag1(tags.get(0));
            }
            if (tags.size() > 1) {
                metric.setTag2(tags.get(1));
            }
            if (tags.size() > 2) {
                metric.setTag3(tags.get(2));
            }
            if (tags.size() > 3) {
                metric.setTag4(tags.get(3));
            }
            if (tags.size() > 4) {
                metric.setTag5(tags.get(4));
            }
        }
        return result.toArray(new Metric[0]);
    }
}
