package com.epic.fdservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FdCreateRequestBean {

    String schmCode;
    String mainCif;
    String intrestRate;
    String depAmnt;
    String depPerdInMths;
    String depPerdInDays;
    String renewInstructions;
    String operativeAcid;
    String repaymentAcid;
    String deviceId;
    @JsonProperty("TCVersion")
    String TCVersion;
    String maturityAmount;
    String maturityDate;
    String interestAmount;

    public String getSchmCode() {
        return schmCode;
    }

    public void setSchmCode(String schmCode) {
        this.schmCode = schmCode;
    }

    public String getMainCif() {
        return mainCif;
    }

    public void setMainCif(String mainCif) {
        this.mainCif = mainCif;
    }

    public String getIntrestRate() {
        return intrestRate;
    }

    public void setIntrestRate(String intrestRate) {
        this.intrestRate = intrestRate;
    }

    public String getDepAmnt() {
        return depAmnt;
    }

    public void setDepAmnt(String depAmnt) {
        this.depAmnt = depAmnt;
    }

    public String getDepPerdInMths() {
        return depPerdInMths;
    }

    public void setDepPerdInMths(String depPerdInMths) {
        this.depPerdInMths = depPerdInMths;
    }

    public String getDepPerdInDays() {
        return depPerdInDays;
    }

    public void setDepPerdInDays(String depPerdInDays) {
        this.depPerdInDays = depPerdInDays;
    }

    public String getOperativeAcid() {
        return operativeAcid;
    }

    public void setOperativeAcid(String operativeAcid) {
        this.operativeAcid = operativeAcid;
    }

    public String getRepaymentAcid() {
        return repaymentAcid;
    }

    public void setRepaymentAcid(String repaymentAcid) {
        this.repaymentAcid = repaymentAcid;
    }

    public String getRenewInstructions() {
        return renewInstructions;
    }

    public void setRenewInstructions(String renewInstructions) {
        this.renewInstructions = renewInstructions;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getTCVersion() {
        return TCVersion;
    }

    public void setTCVersion(String TCVersion) {
        this.TCVersion = TCVersion;
    }

    public String getMaturityAmount() {
        return maturityAmount;
    }

    public void setMaturityAmount(String maturityAmount) {
        this.maturityAmount = maturityAmount;
    }

    public String getMaturityDate() {
        return maturityDate;
    }

    public void setMaturityDate(String maturityDate) {
        this.maturityDate = maturityDate;
    }

    public String getInterestAmount() {
        return interestAmount;
    }

    public void setInterestAmount(String interestAmount) {
        this.interestAmount = interestAmount;
    }
}
