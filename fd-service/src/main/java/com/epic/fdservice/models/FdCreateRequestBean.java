package com.epic.fdservice.models;

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
}
