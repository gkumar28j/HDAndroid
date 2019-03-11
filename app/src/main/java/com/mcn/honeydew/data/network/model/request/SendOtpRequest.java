package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 26/2/18.
 */

public class SendOtpRequest {
    @SerializedName("AuthenticateDetail")
    @Expose
    private String authenticateDetail;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("IsEmail")
    @Expose
    private int isEmail;

    public String getAuthenticateDetail() {
        return authenticateDetail;
    }

    public void setAuthenticateDetail(String authenticateDetail) {
        this.authenticateDetail = authenticateDetail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getIsEmail() {
        return isEmail;
    }

    public void setIsEmail(int isEmail) {
        this.isEmail = isEmail;
    }
}
