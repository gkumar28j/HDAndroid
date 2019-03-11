package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 8/3/18.
 */

public class AddUpdateItemRequest {


    @SerializedName("ItemId")
    @Expose
    private int itemId;
    @SerializedName("ItemName")
    @Expose
    private String itemName;
    @SerializedName("ItemTime")
    @Expose
    private String itemTime;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("ListId")
    @Expose
    private int listId;
    @SerializedName("ListName")
    @Expose
    private String listName;
    @SerializedName("Location")
    @Expose
    private String location;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("Photo")
    @Expose
    private String photo;
    @SerializedName("StatusId")
    @Expose
    private int statusId;

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

    public String getItemTime() {
        return itemTime;
    }

    public void setItemTime(String itemTime) {
        this.itemTime = itemTime;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

}
