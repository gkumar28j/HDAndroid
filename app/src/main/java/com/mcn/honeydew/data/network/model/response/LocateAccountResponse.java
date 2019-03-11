package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

/**
 * Created by atiwari on 26/2/18.
 */

public class LocateAccountResponse {
    @Expose
    @SerializedName("ErrorObject")
    private ErrorObject errorObject;

    @Expose
    @SerializedName("detail")
    private Detail details;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public Detail getDetails() {
        return details;
    }

    public void setDetails(Detail details) {
        this.details = details;
    }

    public static class Detail {

        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("MobileNumber")
        @Expose
        private String mobileNumber;
        @SerializedName("UserName")
        @Expose
        private String userName;
        @SerializedName("IsEmail")
        @Expose
        private boolean isEmail;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getMobileNumber() {
            return mobileNumber;
        }

        public void setMobileNumber(String mobileNumber) {
            this.mobileNumber = mobileNumber;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public boolean isIsEmail() {
            return isEmail;
        }

        public void setIsEmail(boolean isEmail) {
            this.isEmail = isEmail;
        }

    }

}
