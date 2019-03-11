package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

/**
 * Created by atiwari on 26/2/18.
 */

public class SendOtpResponse {
    @Expose
    @SerializedName("ErrorObject")
    private ErrorObject errorObject;

    @Expose
    @SerializedName("result")
    private Detail result;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public Detail getDetails() {
        return result;
    }

    public void setDetails(Detail details) {
        this.result = details;
    }

    public static class Detail {

        @SerializedName("Email")
        @Expose
        private String email;
        @SerializedName("OTP")
        @Expose
        private String oTP;
        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("Status")
        @Expose
        private int status;
        @SerializedName("UserId")
        @Expose
        private Object userId;
        @SerializedName("NewItemId")
        @Expose
        private int newItemId;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getOTP() {
            return oTP;
        }

        public void setOTP(String oTP) {
            this.oTP = oTP;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public Object getUserId() {
            return userId;
        }

        public void setUserId(Object userId) {
            this.userId = userId;
        }

        public int getNewItemId() {
            return newItemId;
        }

        public void setNewItemId(int newItemId) {
            this.newItemId = newItemId;
        }

    }

}
