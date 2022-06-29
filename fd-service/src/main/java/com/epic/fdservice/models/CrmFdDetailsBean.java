package com.epic.fdservice.models;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CrmFdDetailsBean {

    String fdAccountNumber;
    BigDecimal amount;
    String openDate;
    int tenure;
    String interestRate;
    String renewalInstructions;
    String interestCreditAccount;
    String maturityCreditAccount;

    public CrmFdDetailsBean(String fdAccountNumber, BigDecimal amount, Date openDate, int tenure, String interestRate, String renewalInstructions, String interestCreditAccount, String maturityCreditAccount) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String timestampAsString = formatter.format(openDate);

        this.fdAccountNumber = fdAccountNumber;
        this.amount = amount.setScale(2, RoundingMode.HALF_EVEN);
        this.openDate = timestampAsString;
        this.tenure = tenure;
        this.interestRate = interestRate;
        this.renewalInstructions = renewalInstructions;
        this.interestCreditAccount = interestCreditAccount;
        this.maturityCreditAccount = maturityCreditAccount;
    }

    public String getFdAccountNumber() {
        return fdAccountNumber;
    }

    public void setFdAccountNumber(String fdAccountNumber) {
        this.fdAccountNumber = fdAccountNumber;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public int getTenure() {
        return tenure;
    }

    public void setTenure(int tenure) {
        this.tenure = tenure;
    }

    public String getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(String interestRate) {
        this.interestRate = interestRate;
    }

    public String getRenewalInstructions() {
        return renewalInstructions;
    }

    public void setRenewalInstructions(String renewalInstructions) {
        this.renewalInstructions = renewalInstructions;
    }

    public String getInterestCreditAccount() {
        return interestCreditAccount;
    }

    public void setInterestCreditAccount(String interestCreditAccount) {
        this.interestCreditAccount = interestCreditAccount;
    }

    public String getMaturityCreditAccount() {
        return maturityCreditAccount;
    }

    public void setMaturityCreditAccount(String maturityCreditAccount) {
        this.maturityCreditAccount = maturityCreditAccount;
    }
}
