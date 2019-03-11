package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 23/3/18.
 */

public class BluetoothRequest {

    @SerializedName("NotificationStatus")
    @Expose
    private int NotificationStatus;


    @SerializedName("NotificationType")
    @Expose

    private int NotificationType;

    public int getNotificationStatus() {
        return NotificationStatus;
    }

    public void setNotificationStatus(int notificationStatus) {
        NotificationStatus = notificationStatus;
    }

    public int getNotificationType() {
        return NotificationType;
    }

    public void setNotificationType(int notificationType) {
        NotificationType = notificationType;
    }


}
