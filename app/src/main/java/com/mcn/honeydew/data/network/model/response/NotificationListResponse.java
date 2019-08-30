package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import java.util.Comparator;
import java.util.List;

public class NotificationListResponse {


    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("result")
    @Expose
    private List<NotificationListData> result;


    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<NotificationListData> getResult() {
        return result;
    }

    public void setResult(List<NotificationListData> result) {
        this.result = result;
    }


    public static class NotificationListData  {

        @SerializedName("NotificationId")
        @Expose
        int NotificationId;

        @SerializedName("itemid")
        @Expose
        int itemid;

        @SerializedName("ListId")
        @Expose
        int ListId;

        @SerializedName("ListName")
        @Expose
        String ListName;


        @SerializedName("DeviceId")
        @Expose
        String DeviceId;


        @SerializedName("DeviceType")
        @Expose
        String DeviceType;


        @SerializedName("UserProfileIdTo")
        @Expose
        int UserProfileIdTo;

        @SerializedName("UserName")
        @Expose
        String UserName;


        @SerializedName("Message")
        @Expose
        String Message;


        @SerializedName("IsOwner")
        @Expose
        boolean IsOwner;


        @SerializedName("badge")
        @Expose
        int badge;


        @SerializedName("ListHeaderColor")
        @Expose
        String ListHeaderColor;

        @SerializedName("ExpiredStatus")
        @Expose
        int ExpiredStatus;


        @SerializedName("ItemExpireTime")
        @Expose
        String ItemExpireTime;


        @SerializedName("ItemName")
        @Expose
        String ItemName;


        @SerializedName("CreatedDate")
        @Expose
        String CreatedDate;


        @SerializedName("FromUserName")
        @Expose
        String FromUserName;


        @SerializedName("StatusName")
        @Expose
        String StatusName;


        @SerializedName("IsRead")
        @Expose
        boolean IsRead;

        @SerializedName("InProgress")
        @Expose
        int InProgress;

        public int getNotificationId() {
            return NotificationId;
        }

        public void setNotificationId(int notificationId) {
            NotificationId = notificationId;
        }

        public int getItemid() {
            return itemid;
        }

        public void setItemid(int itemid) {
            this.itemid = itemid;
        }

        public int getListId() {
            return ListId;
        }

        public void setListId(int listId) {
            ListId = listId;
        }

        public String getListName() {
            return ListName;
        }

        public void setListName(String listName) {
            ListName = listName;
        }

        public String getDeviceId() {
            return DeviceId;
        }

        public void setDeviceId(String deviceId) {
            DeviceId = deviceId;
        }

        public String getDeviceType() {
            return DeviceType;
        }

        public void setDeviceType(String deviceType) {
            DeviceType = deviceType;
        }

        public int getUserProfileIdTo() {
            return UserProfileIdTo;
        }

        public void setUserProfileIdTo(int userProfileIdTo) {
            UserProfileIdTo = userProfileIdTo;
        }

        public String getUserName() {
            return UserName;
        }

        public void setUserName(String userName) {
            UserName = userName;
        }

        public String getMessage() {
            return Message;
        }

        public void setMessage(String message) {
            Message = message;
        }

        public boolean isOwner() {
            return IsOwner;
        }

        public void setOwner(boolean owner) {
            IsOwner = owner;
        }

        public int getBadge() {
            return badge;
        }

        public void setBadge(int badge) {
            this.badge = badge;
        }

        public String getListHeaderColor() {
            return ListHeaderColor;
        }

        public void setListHeaderColor(String listHeaderColor) {
            ListHeaderColor = listHeaderColor;
        }

        public int getExpiredStatus() {
            return ExpiredStatus;
        }

        public void setExpiredStatus(int expiredStatus) {
            ExpiredStatus = expiredStatus;
        }

        public String getItemExpireTime() {
            return ItemExpireTime;
        }

        public void setItemExpireTime(String itemExpireTime) {
            ItemExpireTime = itemExpireTime;
        }

        public String getItemName() {
            return ItemName;
        }

        public void setItemName(String itemName) {
            ItemName = itemName;
        }

        public String getCreatedDate() {
            return CreatedDate;
        }

        public void setCreatedDate(String createdDate) {
            CreatedDate = createdDate;
        }

        public String getFromUserName() {
            return FromUserName;
        }

        public void setFromUserName(String fromUserName) {
            FromUserName = fromUserName;
        }

        public String getStatusName() {
            return StatusName;
        }

        public void setStatusName(String statusName) {
            StatusName = statusName;
        }

        public boolean isRead() {
            return IsRead;
        }

        public void setRead(boolean read) {
            IsRead = read;
        }

        public int getInProgress() {
            return InProgress;
        }

        public void setInProgress(int inProgress) {
            InProgress = inProgress;
        }

        public static Comparator<NotificationListData> notificationComparator = new Comparator<NotificationListData>() {

            @Override
            public int compare(NotificationListData lhs, NotificationListData rhs) {
                return rhs.getNotificationId() - lhs.getNotificationId();
            }
        };

    }

}
