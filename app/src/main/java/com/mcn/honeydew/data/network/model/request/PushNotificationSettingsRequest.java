package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 7/3/18.
 */

public class PushNotificationSettingsRequest {

    @SerializedName("NotificationStatus")
    @Expose
    private int notificationStatus;

    @SerializedName("NotificationType")
    @Expose
    private int notificationType;

    public PushNotificationSettingsRequest(int notificationStatus, int notificationType) {
        this.notificationStatus = notificationStatus;
        this.notificationType = notificationType;
    }

    public int getNotificationStatus() {
        return notificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        this.notificationStatus = notificationStatus;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }
}
