package com.avit.warsipharmacy.ui.checkout;

public class UpiDetails {
    private String upiName;
    private String upiId;

    public UpiDetails(String upiName, String upiId) {
        this.upiName = upiName;
        this.upiId = upiId;
    }

    public String getUpiName() {
        return upiName;
    }

    public String getUpiId() {
        return upiId;
    }
}
