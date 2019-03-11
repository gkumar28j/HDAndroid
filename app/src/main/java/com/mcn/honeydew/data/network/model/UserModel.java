package com.mcn.honeydew.data.network.model;

import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.utils.CommonUtils;

import static com.mcn.honeydew.utils.AppConstants.MIN_LENGTH_PASSWORD;

/**
 * Created by atiwari on 20/2/18.
 */

public class UserModel {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String dialCode;
    private String password;
    private String confirmPassword;
    private int deviceVersion;
    private String apiVersion;
    private String appVersion;
    private String deviceId;
    private String deviceType;
    private String profilePhoto;
    private double latitude;
    private double longitude;
    private String offsetTimeZone;
    private String timeZoneOffsetName;
    private String timeZone;

    private int messageResourceId;

    public UserModel() {
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public int getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(String profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getOffsetTimeZone() {
        return offsetTimeZone;
    }

    public void setOffsetTimeZone(String offsetTimeZone) {
        this.offsetTimeZone = offsetTimeZone;
    }

    public String getTimeZoneOffsetName() {
        return timeZoneOffsetName;
    }

    public void setTimeZoneOffsetName(String timeZoneOffsetName) {
        this.timeZoneOffsetName = timeZoneOffsetName;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getMessageResourceId() {
        return messageResourceId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDialCode() {
        return dialCode;
    }

    public void setDialCode(String dialCode) {
        this.dialCode = dialCode;
    }

    public boolean isValidUser() {
        if (TextUtils.isEmpty(firstName)) {
            messageResourceId = R.string.error_empty_first_name;
            return false;
        }

        if (TextUtils.isEmpty(lastName)) {
            messageResourceId = R.string.error_empty_last_name;
            return false;
        }

        if (!TextUtils.isEmpty(email) && !CommonUtils.isEmailValid(email)) {
            messageResourceId = R.string.error_invalid_email;
            return false;
        }

        if (TextUtils.isEmpty(phoneNumber)) {
            messageResourceId = R.string.error_empty_phone;
            return false;
        }

        if (phoneNumber.length() < 10) {
            messageResourceId = R.string.error_invalid_phone;
            return false;
        }

        if (!CommonUtils.isPhoneValid(phoneNumber)) {
            messageResourceId = R.string.error_invalid_phone;
            return false;
        }

        if (TextUtils.isEmpty(password)) {
            messageResourceId = R.string.error_empty_password;
            return false;
        }

        if (password.length() < MIN_LENGTH_PASSWORD) {
            messageResourceId = R.string.error_small_password;
            return false;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            messageResourceId = R.string.error_empty_confirm_password;
            return false;
        }

        if (!password.equals(confirmPassword)) {
            messageResourceId = R.string.error_mismatch_password;
            return false;
        }

        messageResourceId = 0;
        return true;
    }
}
