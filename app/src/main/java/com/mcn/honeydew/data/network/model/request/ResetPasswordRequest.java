package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 27/2/18.
 */

public class ResetPasswordRequest {


    @SerializedName("ConfirmPassword")
    @Expose
    private String confirmPassword;
    @SerializedName("EmailOrNumber")
    @Expose
    private String emailOrNumber;
    @SerializedName("NewPassword")
    @Expose
    private String newPassword;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getEmailOrNumber() {
        return emailOrNumber;
    }

    public void setEmailOrNumber(String emailOrNumber) {
        this.emailOrNumber = emailOrNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
