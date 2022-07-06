package com.epic.fdservice.persistance.entity;

import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class FdRateEntityId implements Serializable {
    private static final long serialVersionUID = 8910224281304382856L;
    @Column(name = "FD_TYPE", nullable = false, length = 20)
    private String fdType;

    @Column(name = "PERIOD", nullable = false, length = 2)
    private String period;

    public String getFdType() {
        return fdType;
    }

    public void setFdType(String fdType) {
        this.fdType = fdType;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FdRateEntityId entity = (FdRateEntityId) o;
        return Objects.equals(this.period, entity.period) &&
                Objects.equals(this.fdType, entity.fdType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(period, fdType);
    }

}