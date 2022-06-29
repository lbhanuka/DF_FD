package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "FD_RENEWAL_TYPES")
public class FdRenewalTypesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "RENEWALTYPECODE")
    private String renewaltypecode;
    @Basic
    @Column(name = "DESCRIPTION")
    private String description;

    public String getRenewaltypecode() {
        return renewaltypecode;
    }

    public void setRenewaltypecode(String renewaltypecode) {
        this.renewaltypecode = renewaltypecode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FdRenewalTypesEntity that = (FdRenewalTypesEntity) o;
        return Objects.equals(renewaltypecode, that.renewaltypecode) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(renewaltypecode, description);
    }
}