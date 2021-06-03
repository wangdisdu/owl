package com.owl.api.annotation;

import java.util.List;

public class IntegrationMeta {
    private String builder;
    private String display;
    private String description;
    private String icon;
    private String document;
    private List<ParameterMeta> parameters;

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public List<ParameterMeta> getParameters() {
        return parameters;
    }

    public IntegrationMeta setParameters(List<ParameterMeta> parameters) {
        this.parameters = parameters;
        return this;
    }
}
