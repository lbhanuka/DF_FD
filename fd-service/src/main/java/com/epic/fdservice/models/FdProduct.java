package com.epic.fdservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class FdProduct {

    String productCode;
    String interestType;
    String productType;
    @JsonIgnore
    String allowedViaApp;

    public FdProduct(String productCode, String interestType, String productType, String allowedViaApp) {
        this.productCode = productCode;
        this.interestType = interestType;
        this.productType = productType;
        this.allowedViaApp = allowedViaApp;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getInterestType() {
        return interestType;
    }

    public void setInterestType(String interestType) {
        this.interestType = interestType;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getAllowedViaApp() {
        return allowedViaApp;
    }

    public void setAllowedViaApp(String allowedViaApp) {
        this.allowedViaApp = allowedViaApp;
    }
}
