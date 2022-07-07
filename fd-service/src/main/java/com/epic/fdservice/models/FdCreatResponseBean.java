package com.epic.fdservice.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class FdCreatResponseBean {
    @JsonProperty("STATUS")
    String STATUS;
    @JsonProperty("RESPONSE_DATA")
    LinkedHashMap<String,String> RESPONSE_DATA;

    public String getSTATUS() {
        return STATUS;
    }

    public void setSTATUS(String STATUS) {
        this.STATUS = STATUS;
    }

    public LinkedHashMap<String,String> getRESPONSE_DATA() {
        return RESPONSE_DATA;
    }

    public void setRESPONSE_DATA(LinkedHashMap<String,String> RESPONSE_DATA) {
        this.RESPONSE_DATA = RESPONSE_DATA;
    }
}
