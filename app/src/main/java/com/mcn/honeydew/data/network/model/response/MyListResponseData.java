package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 27/2/18.
 */

public class MyListResponseData {

    @SerializedName("ItemId")
    @Expose
    private int itemId;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("StatusId")
    @Expose
    private int statusId;
    @SerializedName("CreatedDate")
    @Expose
    private String createdDate;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("UserName")
    @Expose
    private String userName;
    @SerializedName("UserProfileId")
    @Expose
    private int userProfileId;
    @SerializedName("ListId")
    @Expose
    private int listId;
    @SerializedName("OrderNumber")
    @Expose
    private int orderNumber;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("IsOwner")
    @Expose
    private boolean isOwner;
    @SerializedName("ItemTime")
    @Expose
    private String itemTime;
    @SerializedName("IsAllowDeleteItemOnCompletion")
    @Expose
    private boolean isAllowDeleteItemOnCompletion;
    @SerializedName("ListName")
    @Expose
    private String listName;
    @SerializedName("ListFontSize")
    @Expose
    private int listFontSize;

    @SerializedName("ListHeaderColor")
    @Expose
    private String listHeaderColor;

    @SerializedName("IsAllowDelete")
    @Expose
    private boolean isAllowDelete;

    @SerializedName("ShowExpired")
    @Expose
    private boolean ShowExpired;

    public boolean isShowExpired() {
        return ShowExpired;
    }

    public void setShowExpired(boolean showExpired) {
        ShowExpired = showExpired;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserProfileId() {
        return userProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        this.userProfileId = userProfileId;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        isOwner = owner;
    }

    public String getItemTime() {
        return itemTime;
    }

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    public boolean isAllowDeleteItemOnCompletion() {
        return isAllowDeleteItemOnCompletion;
    }

    public void setAllowDeleteItemOnCompletion(boolean allowDeleteItemOnCompletion) {
        isAllowDeleteItemOnCompletion = allowDeleteItemOnCompletion;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getListFontSize() {
        return listFontSize;
    }

    public void setListFontSize(int listFontSize) {
        this.listFontSize = listFontSize;
    }

    public String getListHeaderColor() {
        return listHeaderColor;
    }

    public void setListHeaderColor(String listHeaderColor) {
        this.listHeaderColor = listHeaderColor;
    }

    public boolean isAllowDelete() {
        return isAllowDelete;
    }

    public void setAllowDelete(boolean allowDelete) {
        isAllowDelete = allowDelete;
    }
}
