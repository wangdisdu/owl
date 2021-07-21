package com.owl.web.model;

import cn.hutool.core.util.StrUtil;

public abstract class CommonFilter<T> implements BaseFilter<T> {

    public String escapeLike(String before) {
        if (StrUtil.isBlank(before)) {
            return before;
        }
        before = before.replace("\\", "\\\\");
        before = before.replace("_", "\\_");
        before = before.replace("%", "\\%");
        return before;
    }
}
