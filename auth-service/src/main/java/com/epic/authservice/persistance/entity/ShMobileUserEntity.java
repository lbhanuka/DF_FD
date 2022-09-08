package com.epic.authservice.persistance.entity;

import javax.persistence.*;
import java.sql.Timestamp;
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
    @Column(name = "IDNUMBER_APP")
    private String idnumberApp;
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
    @Column(name = "CREATEDTIME",updatable = false,insertable = false)
    private Timestamp createdtime;
    @Basic
    @Column(name = "LASTUPDATEDTIME",insertable = false)
    private Timestamp lastupdatedtime;
    @Basic
    @Column(name = "NAME")
    private String name;

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

    public String getIdnumberApp() {
        return idnumberApp;
    }

    public void setIdnumberApp(String idnumberApp) {
        this.idnumberApp = idnumberApp;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }

    public Timestamp getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(Timestamp lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
