package com.owl.web.model;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.owl.web.common.config.AppConfig;

public abstract class BaseQuery<T> {
    private Integer page;
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public QueryWrapper<T> query() {
        QueryWrapper<T> wrapper = new QueryWrapper<>();
        BaseFilter<T> filter = getFilter();
        BaseSorter<T> sort = getSorter();
        if (filter != null) {
            filter.where(wrapper);
        }
        if (sort != null) {
            sort.order(wrapper);
        }
        return wrapper;
    }

    public Page<T> page() {
        //mybatis 分页 从1开始...
        if (page != null && size != null) {
            return new Page<>(page + 1L, size);
        }
        return new Page<>(1, AppConfig.MAX_PAGE_ROW_COUNT);
    }

    public abstract BaseFilter<T> getFilter();

    public abstract BaseSorter<T> getSorter();
}
