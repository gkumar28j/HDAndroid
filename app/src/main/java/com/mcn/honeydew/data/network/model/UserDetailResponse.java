package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 14/2/18.
 */

public class UserDetailResponse {

    @SerializedName("UserProfileId")
    private int userProfileId;

    @SerializedName("FirstName")
    private String firstName;

    @SerializedName("LastName")
    private String lastName;

    @SerializedName("PrimaryEmail")
    private String primaryEmail;

    @SerializedName("PrimaryMobile")
    private String primaryMobile;

    @SerializedName("IsPhoneVerified")
    private int isPhoneVerified;

    @SerializedName("VerificationCode")
    private String verificationCode;

    @SerializedName("ListId")
    private int listId;

    @SerializedName("UserName")
    private String userName;

    @SerializedName("IsBluetoothNotification")
    private boolean isBluetoothNotification;

    @SerializedName("IsProximityNotification")
    private boolean isProximityNotification;

    @SerializedName("ProximityId")
    private int proximityId;

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public int getIsPhoneVerified() {
        return isPhoneVerified;
    }

    public void setIsPhoneVerified(int isPhoneVerified) {
        this.isPhoneVerified = isPhoneVerified;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isIsBluetoothNotification() {
        return isBluetoothNotification;
    }

    public void setIsBluetoothNotification(boolean isBluetoothNotification) {
        this.isBluetoothNotification = isBluetoothNotification;
    }

    public boolean isIsProximityNotification() {
        return isProximityNotification;
    }

    public void setIsProximityNotification(boolean isProximityNotification) {
        this.isProximityNotification = isProximityNotification;
    }

    public int getProximityId() {
        return proximityId;
    }

    public void setProximityId(int proximityId) {
        this.proximityId = proximityId;
    }

}
