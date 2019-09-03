package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

public class NotificationCountResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("SystemNotificationCount")
    @Expose
    private int SystemNotificationCount;


    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public int getSystemNotificationCount() {
        return SystemNotificationCount;
    }

    public void setSystemNotificationCount(int systemNotificationCount) {
        SystemNotificationCount = systemNotificationCount;
    }




}
