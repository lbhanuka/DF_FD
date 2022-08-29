package com.epic.fdservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.LinkedHashMap;
import java.util.Map;

public class FinacleFdDetailsResponseBean {

    @JsonProperty("STATUS")
    String STATUS;
    @JsonProperty("RESPONSE_DATA")
    Map<String,Object> RESPONSE_DATA;
    @JsonProperty("MESSAGE")
    String MESSAGE;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public Map<String, Object> getRESPONSE_DATA() {
        return RESPONSE_DATA;
    }

    public void setRESPONSE_DATA(Map<String, Object> RESPONSE_DATA) {
        this.RESPONSE_DATA = RESPONSE_DATA;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }
}
