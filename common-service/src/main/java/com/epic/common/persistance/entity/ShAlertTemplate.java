package com.epic.common.persistance.entity;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "SH_ALERT_TEMPLATE")
public class ShAlertTemplate {
    @Id
    @Column(name = "ID", nullable = false, precision = 30)
    private BigDecimal id;

    @Column(name = "TITLE", length = 200)
    private String title;

    @Column(name = "EMAIL_SUBJECT")
    private String emailSubject;

    @Column(name = "TXN_TYPE")
    private int txnType;

    @Column(name = "TXN_MODE")
    private int txnMode;

    @Column(name = "ALERT_CAT")
    private int alertCat;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "MESSAGE")
    private String message;

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmailSubject() {
        return emailSubject;
    }

    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    public int getTxnType() {
        return txnType;
    }

    public void setTxnType(int txnType) {
        this.txnType = txnType;
    }

    public int getTxnMode() {
        return txnMode;
    }

    public void setTxnMode(int txnMode) {
        this.txnMode = txnMode;
    }

    public int getAlertCat() {
        return alertCat;
    }

    public void setAlertCat(int alertCat) {
        this.alertCat = alertCat;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}