package com.owl.web.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

public interface BaseSorter<T> {
    QueryWrapper<T> order(QueryWrapper<T> wrapper);
}
