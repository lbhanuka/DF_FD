package com.epic.fdservice.persistance.entity;

import javax.persistence.*;

@Entity
@Table(name = "FD_PRODUCTS")
public class FdProductsEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "PRODUCT_CODE")
    private String productCode;
    @Basic
    @Column(name = "INTEREST_TYPE")
    private String interestType;
    @Basic
    @Column(name = "PRODUCT_TYPE")
    private String productType;
    @Basic
    @Column(name = "ALLOWEDVIAAPP")
    private String allowedviaapp;

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

    public String getAllowedviaapp() {
        return allowedviaapp;
    }

    public void setAllowedviaapp(String allowedviaapp) {
        this.allowedviaapp = allowedviaapp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FdProductsEntity that = (FdProductsEntity) o;

        if (productCode != null ? !productCode.equals(that.productCode) : that.productCode != null) return false;
        if (interestType != null ? !interestType.equals(that.interestType) : that.interestType != null) return false;
        if (productType != null ? !productType.equals(that.productType) : that.productType != null) return false;
        if (allowedviaapp != null ? !allowedviaapp.equals(that.allowedviaapp) : that.allowedviaapp != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = productCode != null ? productCode.hashCode() : 0;
        result = 31 * result + (interestType != null ? interestType.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (allowedviaapp != null ? allowedviaapp.hashCode() : 0);
        return result;
    }
}
