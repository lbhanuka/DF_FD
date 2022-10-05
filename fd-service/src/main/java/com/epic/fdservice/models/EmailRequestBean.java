package com.epic.fdservice.models;

import java.util.HashMap;

public class EmailRequestBean {

    String emailType;
    String[] emailTo;
    HashMap<String,String> parameters;

    public String getEmailType() {
        return emailType;
    }

    public void setEmailType(String emailType) {
        this.emailType = emailType;
    }

    public String[] getEmailTo() {
        return emailTo;
    }

    public void setEmailTo(String[] emailTo) {
        this.emailTo = emailTo;
    }

    public HashMap<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(HashMap<String, String> parameters) {
        this.parameters = parameters;
    }
}