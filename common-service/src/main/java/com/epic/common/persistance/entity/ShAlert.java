package com.epic.common.persistance.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;

@Entity
@Table(name = "SH_ALERT")
public class ShAlert {
    @Id
    @Column(name = "ALERT_ID", nullable = false)
    private Long id;

    @Column(name = "ALERT_DATA", length = 2000)
    private String alertData;

    @Column(name = "STATUS", length = 20)
    private String status;

    @Column(name = "DATE_TIME")
    private Instant dateTime;

    @Column(name = "TXN_TYPE", length = 3)
    private String txnType;

    @Column(name = "SESSION_ID", length = 200)
    private String sessionId;

    @Column(name = "TXN_MODE")
    private Long txnMode;

    @Column(name = "EMAIL_STATUS", length = 10)
    private String emailStatus;

    @Column(name = "SMS_STATUS", length = 10)
    private String smsStatus;

    @Column(name = "PUSH_STATUS", length = 10)
    private String pushStatus;

    @Column(name = "LAST_UPDATE_DATE_TIME")
    private Instant lastUpdateDateTime;

    @Column(name = "DESCRIPTION", length = 2000)
    private String description;

    @Column(name = "IS_SENT_PUSH", length = 10)
    private String isSentPush;

    @Column(name = "IS_SENT_SMS", length = 10)
    private String isSentSms;

    @Column(name = "IS_SENT_EMAIL", length = 10)
    private String isSentEmail;

    @Column(name = "SERVER_NODE", length = 10)
    private String serverNode;

    @Column(name = "IS_SME_ALERT", length = 10)
    private String isSmeAlert;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlertData() {
        return alertData;
    }

    public void setAlertData(String alertData) {
        this.alertData = alertData;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getDateTime() {
        return dateTime;
    }

    public void setDateTime(Instant dateTime) {
        this.dateTime = dateTime;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public Long getTxnMode() {
        return txnMode;
    }

    public void setTxnMode(Long txnMode) {
        this.txnMode = txnMode;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getSmsStatus() {
        return smsStatus;
    }

    public void setSmsStatus(String smsStatus) {
        this.smsStatus = smsStatus;
    }

    public String getPushStatus() {
        return pushStatus;
    }

    public void setPushStatus(String pushStatus) {
        this.pushStatus = pushStatus;
    }

    public Instant getLastUpdateDateTime() {
        return lastUpdateDateTime;
    }

    public void setLastUpdateDateTime(Instant lastUpdateDateTime) {
        this.lastUpdateDateTime = lastUpdateDateTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIsSentPush() {
        return isSentPush;
    }

    public void setIsSentPush(String isSentPush) {
        this.isSentPush = isSentPush;
    }

    public String getIsSentSms() {
        return isSentSms;
    }

    public void setIsSentSms(String isSentSms) {
        this.isSentSms = isSentSms;
    }

    public String getIsSentEmail() {
        return isSentEmail;
    }

    public void setIsSentEmail(String isSentEmail) {
        this.isSentEmail = isSentEmail;
    }

    public String getServerNode() {
        return serverNode;
    }

    public void setServerNode(String serverNode) {
        this.serverNode = serverNode;
    }

    public String getIsSmeAlert() {
        return isSmeAlert;
    }

    public void setIsSmeAlert(String isSmeAlert) {
        this.isSmeAlert = isSmeAlert;
    }

}