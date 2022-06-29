package com.epic.fdservice.models;

import com.epic.fdservice.util.MessageVarList;

public class ResponseBean {

    String responseCode;
    String responseMsg;
    Object content;

    public Object getContent() {
        return content;
    }

    public void setContent(Object result) {
        this.content = result;
    }

    public ResponseBean() {

    }

    public void setResponse(String responseCode) {
        this.responseCode = responseCode;
        if(responseCode.equals(MessageVarList.RSP_SUCCESS)){
            this.responseMsg = "Success";
        } else if(responseCode.equals(MessageVarList.RSP_NO_DATA_FOUND)){
            this.responseMsg = "No Data Found";
        } else if(responseCode.equals(MessageVarList.RSP_NOT_AUTHORISED)){
            this.responseMsg = "Unauthorised Action";
        } else if(responseCode.equals(MessageVarList.RSP_TOKEN_EXPIRED)){
            this.responseMsg = "Token Expired";
        } else if(responseCode.equals(MessageVarList.RSP_TOKEN_INVALID)){
            this.responseMsg = "Token Invalid";
        } else if(responseCode.equals(MessageVarList.RSP_ERROR)){
            this.responseMsg = "Unknown Error";
        } else if(responseCode.equals(MessageVarList.RSP_FAIL)){
            this.responseMsg = "Fail";
        }else if(responseCode.equals(MessageVarList.RSP_REQUEST_INVALID)){
            this.responseMsg = "Invalid Request Parameters";
        }
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

}