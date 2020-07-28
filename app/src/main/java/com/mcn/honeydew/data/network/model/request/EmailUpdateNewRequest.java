package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EmailUpdateNewRequest {

    @SerializedName("Email")
    @Expose
    String Email;


    @SerializedName("FacebookEmail")
    @Expose
    String FacebookEmail;

    @SerializedName("IsFacebookLogin")
    @Expose
    int IsFacebookLogin;



    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getFacebookEmail() {
        return FacebookEmail;
    }

    public void setFacebookEmail(String facebookEmail) {
        FacebookEmail = facebookEmail;
    }

    public int getIsFacebookLogin() {
        return IsFacebookLogin;
    }

    public void setIsFacebookLogin(int isFacebookLogin) {
        IsFacebookLogin = isFacebookLogin;
    }

}
