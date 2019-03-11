package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 27/2/18.
 */

public class VerifyOtpRequest {

    @Expose
    @SerializedName("AuthenticateDetail")
    private String authenticationCode;

    @Expose
    @SerializedName("OTPCode")
    private String otp;

    public VerifyOtpRequest(String authenticationCode, String otp) {
        this.authenticationCode = authenticationCode;
        this.otp = otp;
    }

    public String getAuthenticationCode() {
        return authenticationCode;
    }

    public String getOtp() {
        return otp;
    }
}
