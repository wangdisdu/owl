package com.owl.web.model;

import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public abstract class CommonSorter<T> implements BaseSorter<T> {
    private String key;
    private Boolean asc;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getAsc() {
        return asc;
    }

    public void setAsc(Boolean asc) {
        this.asc = asc;
    }

    //强制拼接最后一个排序条件
    public String promisedKey() {
        return "updateTime";
    }

    //强制拼接最后一个排序条件的顺序
    public Boolean promisedAsc() {
        return false;
    }

    public QueryWrapper<T> order(QueryWrapper<T> wrapper) {
        Type type = TypeUtil.getTypeArgument(this.getClass());
        Class<?> clazz = TypeUtil.getClass(type);
        Field[] fields = ReflectUtil.getFields(clazz);
        String keyColumnName = genColumnName(fields, key);
        String promisedColumnName = genColumnName(fields, promisedKey());
        wrapper.orderBy(StrUtil.isNotBlank(keyColumnName), Boolean.TRUE.equals(asc), keyColumnName);
        wrapper.orderBy(StrUtil.isNotBlank(promisedColumnName), Boolean.TRUE.equals(promisedAsc()), promisedColumnName);
        return wrapper;
    }

    private String genColumnName(Field[] fields, String key) {
        for (Field field : fields) {
            //强制只能使用范型类的属性作为排序字段，防止注入
            if (StrUtil.equals(field.getName(), key)) {
                return StrUtil.toUnderlineCase(key);
            }
        }
        return null;
    }
}
