package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 16/3/18.
 */

public class UpdateDeviceInfoRequest {

    @SerializedName("Latitude")
    @Expose
    private double latitude;

    @SerializedName("Longitude")
    @Expose
    private double longitude;

    @SerializedName("DeviceVersion")
    @Expose
    private String deviceVersion;

    @SerializedName("ApiVersion")
    @Expose
    private String apiVersion;

    @SerializedName("AppVersion")
    @Expose
    private String appVersion;

    @SerializedName("DeviceId")
    @Expose
    private String deviceId;

    @SerializedName("DeviceType")
    @Expose
    private String deviceType;

    public UpdateDeviceInfoRequest(double latitude, double longitude, String deviceVersion, String apiVersion, String appVersion, String deviceId, String deviceType) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.deviceVersion = deviceVersion;
        this.apiVersion = apiVersion;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.deviceType = deviceType;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setDeviceVersion(String deviceVersion) {
        this.deviceVersion = deviceVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }
}
