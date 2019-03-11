package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 22/2/18.
 */

public class PhoneVerificationResponse {

    @SerializedName("UserProfileId")
    @Expose
    private int userProfileId;

    @SerializedName("CallType")
    @Expose
    private int callType;

    @SerializedName("VerificationCode")
    @Expose
    private String verificationCode;

    @SerializedName("VerificationStatus")
    @Expose
    private boolean verificationStatus;

    @SerializedName("UserMobileNumber")
    @Expose
    private String userMobileNumber;

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

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public int getCallType() {
        return callType;
    }

    public void setCallType(int callType) {
        this.callType = callType;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public boolean isVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(boolean verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public String getUserMobileNumber() {
        return userMobileNumber;
    }

    public void setUserMobileNumber(String userMobileNumber) {
        this.userMobileNumber = userMobileNumber;
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
