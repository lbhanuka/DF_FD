package com.epic.common.models;

import java.util.HashMap;

public class PushNotificationRequestBean {

    String mobileNumber;
    String messageType;
    HashMap<String,String> messageParams;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public HashMap<String, String> getMessageParams() {
        return messageParams;
    }

    public void setMessageParams(HashMap<String, String> messageParams) {
        this.messageParams = messageParams;
    }
}
