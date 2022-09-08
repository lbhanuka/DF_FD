package com.epic.common.models;

import java.util.HashMap;

public class OutboundSMSMessageRequest {

    String[] address = new String[1];
    String senderAddress;
    HashMap<String,String> outboundSMSTextMessage;
    HashMap<String,String> receiptRequest;

    public String[] getAddress() {
        return address;
    }

    public void setAddress(String[] address) {
        this.address = address;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public HashMap<String, String> getOutboundSMSTextMessage() {
        return outboundSMSTextMessage;
    }

    public void setOutboundSMSTextMessage(HashMap<String, String> outboundSMSTextMessage) {
        this.outboundSMSTextMessage = outboundSMSTextMessage;
    }

    public HashMap<String, String> getReceiptRequest() {
        return receiptRequest;
    }

    public void setReceiptRequest(HashMap<String, String> receiptRequest) {
        this.receiptRequest = receiptRequest;
    }
}
