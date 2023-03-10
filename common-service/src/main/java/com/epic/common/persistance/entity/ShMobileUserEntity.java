package com.epic.common.persistance.entity;

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
