package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 14/3/18.
 */

public class UpdateNotificationSettingRequest {

    @SerializedName("AllownotificationFromuser")
    @Expose
    private int allownotificationFromuser;
    @SerializedName("EmailOrPhoneNumber")
    @Expose
    private String emailOrPhoneNumber;

    public int getAllownotificationFromuser() {
        return allownotificationFromuser;
    }

    public void setAllownotificationFromuser(int allownotificationFromuser) {
        this.allownotificationFromuser = allownotificationFromuser;
    }

    public String getEmailOrPhoneNumber() {
        return emailOrPhoneNumber;
    }

    public void setEmailOrPhoneNumber(String emailOrPhoneNumber) {
        this.emailOrPhoneNumber = emailOrPhoneNumber;
    }
}
