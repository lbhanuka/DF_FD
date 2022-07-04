package com.epic.common.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CommonParamRequestBean {

    @NotNull(message = "paramKey cannot be null")
    String paramKey;

    @NotNull(message = "category cannot be null")
    @Pattern(regexp = "^FD$|^SP$", message = "category : invalid ")
    String category;

    public String getParamKey() {
        return paramKey;
    }

    public void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
