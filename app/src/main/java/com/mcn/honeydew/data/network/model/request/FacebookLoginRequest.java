package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 23/2/18.
 */

public class FacebookLoginRequest {

    @SerializedName("ApiVersion")
    @Expose
    private String apiVersion;

    @SerializedName("AppVersion")
    @Expose
    private String appVersion;

    @SerializedName("DeveiceId")
    @Expose
    private String deviceId;

    @SerializedName("DeviceType")
    @Expose
    private String deviceType;

    @SerializedName("DeviceVersion")
    @Expose
    private int deviceVersion;

    @SerializedName("FacebookAccessToken")
    @Expose
    private String facebookAccessToken;

    @SerializedName("PrimaryMobile")
    @Expose
    private String primaryMobile;

    @SerializedName("country")
    @Expose
    private String country;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeveiceId() {
        return deviceId;
    }

    public void setDeveiceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getDeviceVersion() {
        return deviceVersion;
    }

    public void setDeviceVersion(int deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public String getFacebookAccessToken() {
        return facebookAccessToken;
    }

    public void setFacebookAccessToken(String facebookAccessToken) {
        this.facebookAccessToken = facebookAccessToken;
    }

    public String getPrimaryMobile() {
        return primaryMobile;
    }

    public void setPrimaryMobile(String primaryMobile) {
        this.primaryMobile = primaryMobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
