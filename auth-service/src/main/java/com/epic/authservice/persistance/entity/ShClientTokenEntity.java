package com.epic.authservice.persistance.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "SH_CLIENT_TOKEN")
public class ShClientTokenEntity {
    @Id
    @Column(name = "ACCESSTOKEN")
    private String accesstoken;
    @Basic
    @Column(name = "REFRESHTOKEN")
    private String refreshtoken;
    @Basic
    @Column(name = "CLIENTID")
    private String clientid;
    @Basic
    @Column(name = "EXPIRATION")
    private long expiration;

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getClientid() {
        return clientid;
    }

    public void setClientid(String clientid) {
        this.clientid = clientid;
    }

    public long getExpiration() {
        return expiration;
    }

    public void setExpiration(long expiration) {
        this.expiration = expiration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShClientTokenEntity that = (ShClientTokenEntity) o;
        return expiration == that.expiration && Objects.equals(accesstoken, that.accesstoken) && Objects.equals(refreshtoken, that.refreshtoken) && Objects.equals(clientid, that.clientid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accesstoken, refreshtoken, clientid, expiration);
    }
}
