package com.epic.fdservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PushNotificationResponseBean {

    @JsonProperty("MESSAGE")
    String MESSAGE;
    @JsonProperty("STATUS")
    String STATUS;

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }
}
