package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 27/2/18.
 */

public class ChangePasswordRequest {
    @SerializedName("ConfirmPassword")
    @Expose
    private String confirmPassword;
    @SerializedName("OldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("NewPassword")
    @Expose
    private String newPassword;

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
