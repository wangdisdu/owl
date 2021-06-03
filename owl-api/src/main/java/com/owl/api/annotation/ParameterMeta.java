package com.owl.api.annotation;

public class ParameterMeta implements Comparable<ParameterMeta> {
    private DataType type;
    private String name;
    private String display;
    private String description;
    private String placeholder;
    private String[] candidates;
    private boolean required;
    private int order;
    private boolean password;
    private ParameterMeta listParameter;

    public DataType getType() {
        return type;
    }

    public void setType(DataType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String[] getCandidates() {
        return candidates;
    }

    public void setCandidates(String[] candidates) {
        this.candidates = candidates;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean getPassword() {
        return password;
    }

    public void setPassword(boolean password) {
        this.password = password;
    }

    public ParameterMeta getListParameter() {
        return listParameter;
    }

    public void setListParameter(ParameterMeta listParameter) {
        this.listParameter = listParameter;
    }

    @Override
    public int compareTo(ParameterMeta o) {
        return this.getOrder() - o.getOrder();
    }
}
