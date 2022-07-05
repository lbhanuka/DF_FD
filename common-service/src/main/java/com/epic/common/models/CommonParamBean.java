package com.epic.common.models;

public class CommonParamBean {

    String key;
    String value;

    String description;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CommonParamBean(String key, String value, String description) {
        this.key = key;
        this.value = value;
        this.description = description;
    }
}
