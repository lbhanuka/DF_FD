package com.epic.fdservice.persistance.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Objects;

@Entity
@Table(name = "FD_DETAILS")
public class FdDetailsEntity {
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "REQUESTID")
    private String requestid;
    @Basic
    @Column(name = "FDACCOUNTNUMBER")
    private String fdaccountnumber;
    @Basic
    @Column(name = "SCHEMECODE")
    private String schemecode;
    @Basic
    @Column(name = "PRODUCTCODE")
    private String productcode;
    @Basic
    @Column(name = "AMOUNT")
    private BigDecimal amount;
    @Basic
    @Column(name = "CREATEDTIME",updatable = false,insertable = false)
    private Timestamp createdtime;
    @Basic
    @Column(name = "TENURE")
    private int tenure;
    @Basic
    @Column(name = "RATE")
    private String rate;
    @Basic
    @Column(name = "RENEWALINSTRUCTION")
    private String renewalinstruction;
    @Basic
    @Column(name = "INTERESTCREDITACCOUNT")
    private String interestcreditaccount;
    @Basic
    @Column(name = "MATURITYCREDITACCOUNT")
    private String maturitycreditaccount;
    @Basic
    @Column(name = "CIF")
    private String cif;
    @Basic
    @Column(name = "NIC")
    private String nic;

    public String getRequestid() {
        return requestid;
    }

    public void setRequestid(String requestid) {
        this.requestid = requestid;
    }

    public String getFdaccountnumber() {
        return fdaccountnumber;
    }

    public void setFdaccountnumber(String fdaccountnumber) {
        this.fdaccountnumber = fdaccountnumber;
    }

    public String getSchemecode() {
        return schemecode;
    }

    public void setSchemecode(String schemecode) {
        this.schemecode = schemecode;
    }

    public String getProductcode() {
        return productcode;
    }

    public void setProductcode(String productcode) {
        this.productcode = productcode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRenewalinstruction() {
        return renewalinstruction;
    }

    public void setRenewalinstruction(String renewalinstruction) {
        this.renewalinstruction = renewalinstruction;
    }

    public String getInterestcreditaccount() {
        return interestcreditaccount;
    }

    public void setInterestcreditaccount(String interestcreditaccount) {
        this.interestcreditaccount = interestcreditaccount;
    }

    public String getMaturitycreditaccount() {
        return maturitycreditaccount;
    }

    public void setMaturitycreditaccount(String maturitycreditaccount) {
        this.maturitycreditaccount = maturitycreditaccount;
    }

    public String getCif() {
        return cif;
    }

    public void setCif(String cif) {
        this.cif = cif;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FdDetailsEntity that = (FdDetailsEntity) o;
        return tenure == that.tenure && Objects.equals(requestid, that.requestid) && Objects.equals(fdaccountnumber, that.fdaccountnumber) && Objects.equals(schemecode, that.schemecode) && Objects.equals(productcode, that.productcode) && Objects.equals(amount, that.amount) && Objects.equals(createdtime, that.createdtime) && Objects.equals(rate, that.rate) && Objects.equals(renewalinstruction, that.renewalinstruction) && Objects.equals(interestcreditaccount, that.interestcreditaccount) && Objects.equals(maturitycreditaccount, that.maturitycreditaccount) && Objects.equals(cif, that.cif) && Objects.equals(nic, that.nic);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requestid, fdaccountnumber, schemecode, productcode, amount, createdtime, tenure, rate, renewalinstruction, interestcreditaccount, maturitycreditaccount, cif, nic);
    }
}
