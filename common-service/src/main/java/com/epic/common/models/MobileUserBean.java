package com.epic.common.models;

public class MobileUserBean {

    String nic;
    String mobileNumber;

    public MobileUserBean(String nic, String mobileNumber) {
        this.nic = nic;
        this.mobileNumber = mobileNumber;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
