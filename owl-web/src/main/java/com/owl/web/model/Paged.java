package com.owl.web.model;

import java.util.List;

public class Paged<T> {
    private long total;
    private List<T> records;

    public Paged() {
    }

    public Paged(long total, List<T> records) {
        this.total = total;
        this.records = records;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public List<T> getRecords() {
        return records;
    }

    public void setRecords(List<T> records) {
        this.records = records;
    }
}
