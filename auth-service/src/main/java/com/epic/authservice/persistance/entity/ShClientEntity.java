package com.epic.authservice.persistance.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SH_CLIENT")
public class ShClientEntity {
    @Id
    @Column(name = "CLIENTID")
    private String clientid;
    @Basic
    @Column(name = "SECRET")
    private String secret;
    @Basic
    @Column(name = "AUTHTYPE")
    private String authtype;
    @Basic
    @Column(name = "SCOPE")
    private String scope;
    @Basic
    @Column(name = "TOEKNEXPIRETIME")
    private long tokenexpiretime;

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getAuthtype() {
        return authtype;
    }

    public void setAuthtype(String authtype) {
        this.authtype = authtype;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public long getTokenexpiretime() {
        return tokenexpiretime;
    }

    public void setTokenexpiretime(long tokenexpiretime) {
        this.tokenexpiretime = tokenexpiretime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShClientEntity that = (ShClientEntity) o;
        return Objects.equals(clientid, that.clientid) && Objects.equals(secret, that.secret);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientid, secret, authtype);
    }
}