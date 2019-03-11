package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

public class GetBluetoothItemsListResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("result")
    @Expose
    private BluetoothItem[] result;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public BluetoothItem[] getResult() {
        return result;
    }

    public void setResult(BluetoothItem[] result) {
        this.result = result;
    }

    public class BluetoothItem {
        @SerializedName("NotificationId")
        @Expose
        private long notificationId;

        @SerializedName("itemid")
        @Expose
        private long itemId;

        @SerializedName("ListId")
        @Expose
        private long listId;

        @SerializedName("ListName")
        @Expose
        private String listName;

        @SerializedName("DeviceId")
        @Expose
        private String deviceId;

        @SerializedName("DeviceType")
        @Expose
        private String deviceType;

        @SerializedName("UserProfileIdTo")
        @Expose
        private int userProfileIdTo;

        @SerializedName("UserName")
        @Expose
        private String userName;

        @SerializedName("Message")
        @Expose
        private String message;

        @SerializedName("IsOwner")
        @Expose
        private boolean isOwner;

        @SerializedName("badge")
        @Expose
        private int badge;

        @SerializedName("ListHeaderColor")
        @Expose
        private String listHeaderColor;

        @SerializedName("ExpiredStatus")
        @Expose
        private int expiredStatus;

        @SerializedName("ItemExpireTime")
        @Expose
        private String itemExpireTime;

        @SerializedName("ItemName")
        @Expose
        private String itemName;

        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        @SerializedName("FromUserName")
        @Expose
        private String fromUserName;

        @SerializedName("StatusName")
        @Expose
        private String statusName;

        @SerializedName("IsRead")
        @Expose
        private boolean isRead;

        @SerializedName("InProgress")
        @Expose
        private int inProgress;

        private boolean isSent;


        public long getNotificationId() {
            return notificationId;
        }

        public void setNotificationId(long notificationId) {
            this.notificationId = notificationId;
        }

        public long getItemId() {
            return itemId;
        }

        public void setItemId(long itemId) {
            this.itemId = itemId;
        }

        public long getListId() {
            return listId;
        }

        public void setListId(long listId) {
            this.listId = listId;
        }

        public String getListName() {
            return listName;
        }

        public void setListName(String listName) {
            this.listName = listName;
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

        public int getUserProfileIdTo() {
            return userProfileIdTo;
        }

        public void setUserProfileIdTo(int userProfileIdTo) {
            this.userProfileIdTo = userProfileIdTo;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public boolean isOwner() {
            return isOwner;
        }

        public void setOwner(boolean owner) {
            isOwner = owner;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public String getListHeaderColor() {
            return listHeaderColor;
        }

        public void setListHeaderColor(String listHeaderColor) {
            this.listHeaderColor = listHeaderColor;
        }

        public int getExpiredStatus() {
            return expiredStatus;
        }

        public void setExpiredStatus(int expiredStatus) {
            this.expiredStatus = expiredStatus;
        }

        public String getItemExpireTime() {
            return itemExpireTime;
        }

        public void setItemExpireTime(String itemExpireTime) {
            this.itemExpireTime = itemExpireTime;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public String getStatusName() {
            return statusName;
        }

        public void setStatusName(String statusName) {
            this.statusName = statusName;
        }

        public boolean isRead() {
            return isRead;
        }

        public void setRead(boolean read) {
            isRead = read;
        }

        public int getInProgress() {
            return inProgress;
        }

        public void setInProgress(int inProgress) {
            this.inProgress = inProgress;
        }

        public boolean isSent() {
            return isSent;
        }

        public void setSent(boolean sent) {
            isSent = sent;
        }
    }
}
