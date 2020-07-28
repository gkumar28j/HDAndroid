package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VerifyNewEmailOTPRequest {


    @SerializedName("AuthenticateDetail")
    @Expose
    String AuthenticateDetail;

    public String getAuthenticateDetail() {
        return AuthenticateDetail;
    }

    public void setAuthenticateDetail(String authenticateDetail) {
        AuthenticateDetail = authenticateDetail;
    }

    public String getOTPCode() {
        return OTPCode;
    }

    public void setOTPCode(String OTPCode) {
        this.OTPCode = OTPCode;
    }

    @SerializedName("OTPCode")
    @Expose
    String OTPCode;

}
