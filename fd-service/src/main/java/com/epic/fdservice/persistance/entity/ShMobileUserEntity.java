package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SH_MOBILE_USER")
public class ShMobileUserEntity {
    @Id
    @Column(name = "DEVICEID")
    private String deviceid;
    @Basic
    @Column(name = "MOBILENUMBER")
    private String mobilenumber;
    @Basic
    @Column(name = "IDNUMBER")
    private String idnumber;
    @Basic
    @Column(name = "LANGUAGE")
    private String language;
    @Basic
    @Column(name = "TOKEN")
    private String token;
    @Basic
    @Column(name = "TOKENEXPIRATION")
    private long tokenexpiration;
    @Basic
    @Column(name = "SCOPE")
    private String scope;
    @Basic
    @Column(name = "EMAIL")
    private String email;
    @Basic
    @Column(name = "NAME")
    private String name;
    @Basic
    @Column(name = "DATEOFBIRTH")
    private String dateOfBirth;
    @Basic
    @Column(name = "ADDRESS1")
    private String address1;
    @Basic
    @Column(name = "ADDRESS2")
    private String address2;
    @Basic
    @Column(name = "ADDRESS3")
    private String address3;

    public String getDeviceid() {
        return deviceid;
    }

    public void setDeviceid(String deviceid) {
        this.deviceid = deviceid;
    }

    public String getMobilenumber() {
        return mobilenumber;
    }

    public void setMobilenumber(String mobilenumber) {
        this.mobilenumber = mobilenumber;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getTokenexpiration() {
        return tokenexpiration;
    }

    public void setTokenexpiration(long tokenexpiration) {
        this.tokenexpiration = tokenexpiration;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShMobileUserEntity that = (ShMobileUserEntity) o;
        return Objects.equals(deviceid, that.deviceid) && Objects.equals(mobilenumber, that.mobilenumber) && Objects.equals(idnumber, that.idnumber) && Objects.equals(language, that.language) && Objects.equals(token, that.token) && Objects.equals(tokenexpiration, that.tokenexpiration) && Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceid, mobilenumber, idnumber, language, token, tokenexpiration, scope);
    }
}
