package com.epic.fdservice.models;

public class FdRatesResponseBean {

    String period;

    String monthly;

    String maturity;

    public FdRatesResponseBean(String period, String monthly, String maturity) {
        this.period = period;
        this.monthly = monthly;
        this.maturity = maturity;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
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
}
