package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ReminderTimeRequest {


    @SerializedName("ReminderTime")
    @Expose
    String ReminderTime;

    @SerializedName("IsExpired")
    @Expose
    int IsExpired;

    public String getReminderTime() {
        return ReminderTime;
    }

    public void setReminderTime(String reminderTime) {
        ReminderTime = reminderTime;
    }

    public int getIsExpired() {
        return IsExpired;
    }

    public void setIsExpired(int isExpired) {
        IsExpired = isExpired;
    }



}
