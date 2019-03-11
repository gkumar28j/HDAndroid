

package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 2/1/18.
 */

public class RegistrationRequest {

    private RegistrationRequest() {
        // This class is not publicly instantiable
    }

    @Expose
    @SerializedName("FirstName")
    private String firstName;

    @Expose
    @SerializedName("LastName")
    private String lastName;

    @Expose
    @SerializedName("Email")
    private String email;

    @Expose
    @SerializedName("Password")
    private String password;

    @Expose
    @SerializedName("MobileNumber")
    private String mobileNumber;

    @Expose
    @SerializedName("DeviceVersion")
    private String deviceVersion;

    @Expose
    @SerializedName("APIVersion")
    private String apiVersion;

    @Expose
    @SerializedName("AppVersion")
    private String appVersion;

    @Expose
    @SerializedName("DeviceId")
    private String deviceId;

    @Expose
    @SerializedName("DeviceType")
    private String deviceType;

    @Expose
    @SerializedName("ProfilePhoto")
    private String profilePhoto;

    @Expose
    @SerializedName("Latitude")
    private String latitude;

    @Expose
    @SerializedName("Longitude")
    private String longitude;

    @Expose
    @SerializedName("OffsetTimeZone")
    private String offsetTimeZone;

    @Expose
    @SerializedName("TimeZoneOffsetName")
    private String timeZoneOffsetName;

    @Expose
    @SerializedName("TimeZone")
    private String timeZone;


    public RegistrationRequest(String firstName, String lastName, String email, String password, String mobileNumber, String deviceVersion, String apiVersion, String appVersion, String deviceId, String deviceType, String profilePhoto, String latitude, String longitude, String offsetTimeZone, String timeZoneOffsetName, String timeZone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.mobileNumber = mobileNumber;
        this.deviceVersion = deviceVersion;
        this.apiVersion = apiVersion;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
        this.profilePhoto = profilePhoto;
        this.latitude = latitude;
        this.longitude = longitude;
        this.offsetTimeZone = offsetTimeZone;
        this.timeZoneOffsetName = timeZoneOffsetName;
        this.timeZone = timeZone;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getDeviceVersion() {
        return deviceVersion;
    }

    public String getApiVersion() {
        return apiVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getProfilePhoto() {
        return profilePhoto;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getOffsetTimeZone() {
        return offsetTimeZone;
    }

    public String getTimeZoneOffsetName() {
        return timeZoneOffsetName;
    }

    public String getTimeZone() {
        return timeZone;
    }
}
