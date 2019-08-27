package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateEmailRequest {

    @SerializedName("Email")
    @Expose
    private String Email;


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }


}
