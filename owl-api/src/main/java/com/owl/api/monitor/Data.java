package com.owl.api.monitor;

public class Data {
    private Long time;
    private Double value;
    private String guid;
    private String metric;
    private String instance;
    private String category;
    private String host;
    private String tag1;
    private String tag2;
    private String tag3;
    private String tag4;
    private String tag5;

    public Long getTime() {
        return time;
    }

    public Data setTime(Long time) {
        this.time = time;
        return this;
    }

    public Double getValue() {
        return value;
    }

    public Data setValue(Double value) {
        this.value = value;
        return this;
    }

    public String getGuid() {
        return guid;
    }

    public Data setGuid(String guid) {
        this.guid = guid;
        return this;
    }

    public String getMetric() {
        return metric;
    }

    public Data setMetric(String metric) {
        this.metric = metric;
        return this;
    }

    public String getInstance() {
        return instance;
    }

    public Data setInstance(String instance) {
        this.instance = instance;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public Data setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Data setHost(String host) {
        this.host = host;
        return this;
    }

    public String getTag1() {
        return tag1;
    }

    public Data setTag1(String tag1) {
        this.tag1 = tag1;
        return this;
    }

    public String getTag2() {
        return tag2;
    }

    public Data setTag2(String tag2) {
        this.tag2 = tag2;
        return this;
    }

    public String getTag3() {
        return tag3;
    }

    public Data setTag3(String tag3) {
        this.tag3 = tag3;
        return this;
    }

    public String getTag4() {
        return tag4;
    }

    public Data setTag4(String tag4) {
        this.tag4 = tag4;
        return this;
    }

    public String getTag5() {
        return tag5;
    }

    public Data setTag5(String tag5) {
        this.tag5 = tag5;
        return this;
    }
}
