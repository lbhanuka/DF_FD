package com.epic.authservice.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JwtRequestBean {
    @NotNull(message = "mobileNumber cannot be null")
    @Size(min = 9, max = 9, message = "mobileNumber : size must be 9")
    @Pattern(regexp = "^[0-9]*$", message = "mobileNumber : invalid value")
    String mobileNumber;
    @NotNull(message = "deviceId cannot be null")
    @Size(min = 1, max = 50, message = "deviceId : size must be between 1 and 50")
    String deviceId;
    @NotNull(message = "customerNic cannot be null")
    @Size(min = 9, max = 12, message = "customerNic : size must be between 9 and 12")
    @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{9}|[0-9]{12})$", message = "customerNic : invalid value")
    String customerNic;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCustomerNic() {
        return customerNic;
    }

    public void setCustomerNic(String customerNic) {
        this.customerNic = customerNic;
    }
}
