package com.epic.fdservice.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Table(name = "SH_COMMON_PARAM")
@Entity
public class ShCommonParam {
    @Id
    @Column(name = "PARAM_CODE", nullable = false, length = 50)
    private String id;

    @Column(name = "PARAM_VALUE", length = 200)
    private String paramValue;

    @Column(name = "DESCRIPTION", length = 100)
    private String description;

    @Column(name = "CATEGORY", length = 10)
    private String category;

    @Column(name = "CREATEDTIME", nullable = false)
    private Instant createdtime;

    @Column(name = "LASTUPDATEDTIME", nullable = false)
    private Instant lastupdatedtime;

    @Column(name = "LASTUPDATEDUSER", length = 64)
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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