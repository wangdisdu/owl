package com.owl.web.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface BaseFilter<T> {
    QueryWrapper<T> where(QueryWrapper<T> wrapper);
}
