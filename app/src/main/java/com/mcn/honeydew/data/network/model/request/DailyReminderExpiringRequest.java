package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DailyReminderExpiringRequest {

    @SerializedName("IsConnected")
    @Expose
    int IsConnected;

    public int getIsConnected() {
        return IsConnected;
    }

    public void setIsConnected(int isConnected) {
        IsConnected = isConnected;
    }

}
