package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "FD_RATES")
public class FdRateEntity {
    @EmbeddedId
    private FdRateEntityId id;

    @Column(name = "MONTHLY", length = 15)
    private String monthly;

    @Column(name = "MATURITY", length = 15)
    private String maturity;

    @Column(name = "CREATEDTIME", nullable = false)
    private Instant createdtime;

    @Column(name = "LASTUPDATEDTIME", nullable = false)
    private Instant lastupdatedtime;

    @Column(name = "LASTUPDATEDUSER", length = 64)
    private String lastupdateduser;

    @Basic
    @Column(name = "STATUS")
    private String status;

    public FdRateEntityId getId() {
        return id;
    }

    public void setId(FdRateEntityId id) {
        this.id = id;
    }

    public String getMonthly() {
        return monthly;
    }

    public void setMonthly(String monthly) {
        this.monthly = monthly;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}