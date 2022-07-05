package com.epic.common.persistance.entity;

import javax.persistence.*;
import java.time.Instant;


@Table(name = "MT_COMMON_PARAM")
@Entity
public class MtCommonParam {
    @javax.persistence.Id
    @javax.persistence.Column(name = "PARAM_CODE", nullable = false, length = 50)
    private String id;

    @javax.persistence.Column(name = "PARAM_VALUE", length = 200)
    private String paramValue;

    @javax.persistence.Column(name = "DESCRIPTION", length = 100)
    private String description;

    @javax.persistence.Column(name = "CREATEDTIME", nullable = false)
    private Instant createdtime;

    @javax.persistence.Column(name = "LASTUPDATEDTIME", nullable = false)
    private Instant lastupdatedtime;

    @javax.persistence.Column(name = "LASTUPDATEDUSER", length = 64)
    private String lastupdateduser;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Instant createdtime) {
        this.createdtime = createdtime;
    }

    public Instant getLastupdatedtime() {
        return lastupdatedtime;
    }

    public void setLastupdatedtime(Instant lastupdatedtime) {
        this.lastupdatedtime = lastupdatedtime;
    }

    public String getLastupdateduser() {
        return lastupdateduser;
    }

    public void setLastupdateduser(String lastupdateduser) {
        this.lastupdateduser = lastupdateduser;
    }

}