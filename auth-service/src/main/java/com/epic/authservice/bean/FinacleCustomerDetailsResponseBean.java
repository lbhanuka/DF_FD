package com.epic.authservice.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

public class FinacleCustomerDetailsResponseBean {

    @JsonProperty("STATUS")
    String STATUS;
    @JsonProperty("RESPONSE_DATA")
    Map<String,Object> RESPONSE_DATA;

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
}
