package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 22/2/18.
 */

public class PhoneVerificationRequest {

    @Expose
    @SerializedName("CallType")
    private int callType;

    @Expose
    @SerializedName("UserMobileNumber")
    private String phoneNumber;

    public PhoneVerificationRequest(int callType, String phoneNumber) {
        this.callType = callType;
        this.phoneNumber = phoneNumber;
    }

    public int getCallType() {
        return callType;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}
