package com.epic.authservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserAvailibilityRequestBean {

    @NotNull(message = "mobileNumber cannot be null")
    @Size(min = 9, max = 9, message = "mobileNumber : size must be 9")
    @Pattern(regexp = "^[0-9]*$", message = "mobileNumber : invalid value")
    String mobileNumber;
    @NotNull(message = "deviceId cannot be null")
    @Size(min = 1, max = 50, message = "deviceId : size must be between 1 and 50")
    String deviceId;
    @NotNull(message = "language cannot be null")
    @Size(min = 1, max = 1, message = "language : size must be 1")
    @Pattern(regexp = "^E$|^S$|^T$", message = "language : invalid value")
    String language;
    @NotNull(message = "customerNic cannot be null")
    @Size(min = 9, max = 12, message = "customerNic : size must be between 9 and 12")
    @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{9}|[0-9]{12})$", message = "customerNic : invalid value")
    String customerNic;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }
}
