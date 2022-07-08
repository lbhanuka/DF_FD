package com.epic.fdservice.models;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class FdDetailsRequestBean {

    @NotNull(message = "identificationNumber cannot be null")
    @Size(min = 6, max = 12, message = "identificationNumber : size must be between 6 and 12")
    @Pattern(regexp = "^([0-9]{9}[x|X|v|V]|[0-9]{6,12})$", message = "identificationNumber : invalid value")
    String identificationNumber;

    @NotNull(message = "identificationType cannot be null")
    @Pattern(regexp = "^CIF$|^NIC$", message = "identificationType : invalid ")
    String identificationType;

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getIdentificationType() {
        return identificationType;
    }

    public void setIdentificationType(String identificationType) {
        this.identificationType = identificationType;
    }
}
