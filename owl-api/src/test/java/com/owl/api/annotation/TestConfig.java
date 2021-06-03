package com.owl.api.annotation;

import com.owl.api.SmartConfig;

import java.util.List;

public class TestConfig extends SmartConfig {
    @Parameter(
            name = "name1",
            display = "display1",
            description = "description1",
            placeholder = "placeholder1",
            candidates = {"op1","op2"},
            required = false,
            password = true
    )
    private String f1;
    @Parameter private Integer f2;
    @Parameter private Long f3;
    @Parameter private Float f4;
    @Parameter private Double f5;
    @Parameter private KeyValue f6;
    @Parameter private List<String> f7;
    @Parameter private List<Double> f8;

    public String getF1() {
        return f1;
    }

    public void setF1(String f1) {
        this.f1 = f1;
    }

    public Integer getF2() {
        return f2;
    }

    public void setF2(Integer f2) {
        this.f2 = f2;
    }

    public Long getF3() {
        return f3;
    }

    public void setF3(Long f3) {
        this.f3 = f3;
    }

    public Float getF4() {
        return f4;
    }

    public void setF4(Float f4) {
        this.f4 = f4;
    }

    public Double getF5() {
        return f5;
    }

    public void setF5(Double f5) {
        this.f5 = f5;
    }

    public KeyValue getF6() {
        return f6;
    }

    public void setF6(KeyValue f6) {
        this.f6 = f6;
    }

    public List<String> getF7() {
        return f7;
    }

    public void setF7(List<String> f7) {
        this.f7 = f7;
    }

    public List<Double> getF8() {
        return f8;
    }

    public void setF8(List<Double> f8) {
        this.f8 = f8;
    }
}
