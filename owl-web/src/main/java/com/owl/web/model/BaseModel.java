package com.owl.web.model;

import org.springframework.beans.BeanUtils;

public interface BaseModel<T> {
    default BaseModel copyFrom(T t) {
        BeanUtils.copyProperties(t, this);
        return this;
    }

    default T copyTo(T t) {
        BeanUtils.copyProperties(this, t);
        return t;
    }
}
