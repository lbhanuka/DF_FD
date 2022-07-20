package com.epic.common.models;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class CommonParamRequestBean {

    @NotNull(message = "Device Id cannot be null")
    @NotEmpty(message = "Device Id cannot be empty")
    String deviceId;

    @NotNull(message = "category cannot be null")
    @Pattern(regexp = "^FD$|^SP$|^CM$|^ALL$", message = "category : invalid ")
    String category;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String paramKey) {
        this.deviceId = paramKey;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
