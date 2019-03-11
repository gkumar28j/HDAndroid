package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import java.util.List;

/**
 * Created by amit on 27/2/18.
 */

public class NotificationSettingsResponse {

    @Expose
    @SerializedName("ErrorObject")
    private ErrorObject errorObject;

    @Expose
    @SerializedName("result")
    private List<NotificationSettings> results;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<NotificationSettings> getResults() {
        return results;
    }

    public void setResults(List<NotificationSettings> results) {
        this.results = results;
    }

    public static class NotificationSettings {

        @SerializedName("IsBluetoothNotification")
        @Expose
        private boolean IsBluetoothNotification;

        @SerializedName("IsProximityNotification")
        @Expose
        private boolean IsProximityNotification;

        @SerializedName("ProximityRange")
        @Expose
        private String ProximityRange;

        @SerializedName("ProximityId")
        @Expose
        private int proximityId;

        @SerializedName("BluetoothReminderForExpired")
        @Expose
        private boolean bluetoothReminderForExpired;

        @SerializedName("BluetoothReminderForExpiring")
        @Expose
        private boolean bluetoothReminderForExpiring;

        @SerializedName("ReminderTimeOnExpiringItem")
        @Expose
        private String timeOnExpiring;

        @SerializedName("ReminderTimeOnExpiredItem")
        @Expose
        private String timeOnExpired;

        public boolean isBluetoothNotification() {
            return IsBluetoothNotification;
        }

        public void setBluetoothNotification(boolean bluetoothNotification) {
            IsBluetoothNotification = bluetoothNotification;
        }

        public boolean isProximityNotification() {
            return IsProximityNotification;
        }

        public void setProximityNotification(boolean proximityNotification) {
            IsProximityNotification = proximityNotification;
        }

        public String getProximityRange() {
            return ProximityRange;
        }

        public void setProximityRange(String proximityRange) {
            ProximityRange = proximityRange;
        }

        public int getProximityId() {
            return proximityId;
        }

        public void setProximityId(int proximityId) {
            this.proximityId = proximityId;
        }

        public boolean isBluetoothReminderForExpired() {
            return bluetoothReminderForExpired;
        }

        public void setBluetoothReminderForExpired(boolean bluetoothReminderForExpired) {
            this.bluetoothReminderForExpired = bluetoothReminderForExpired;
        }

        public boolean isBluetoothReminderForExpiring() {
            return bluetoothReminderForExpiring;
        }

        public void setBluetoothReminderForExpiring(boolean bluetoothReminderForExpiring) {
            this.bluetoothReminderForExpiring = bluetoothReminderForExpiring;
        }

        public String getTimeOnExpiring() {
            return timeOnExpiring;
        }

        public void setTimeOnExpiring(String timeOnExpiring) {
            this.timeOnExpiring = timeOnExpiring;
        }

        public String getTimeOnExpired() {
            return timeOnExpired;
        }

        public void setTimeOnExpired(String timeOnExpired) {
            this.timeOnExpired = timeOnExpired;
        }
    }
}
