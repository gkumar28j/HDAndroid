package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UnshareListRequest {

    @SerializedName("ListId")
    @Expose
    private int ListId;

    @SerializedName("ListName")
    @Expose
    private String ListName;


    @SerializedName("ListOwnerId")
    @Expose
    private int ListOwnerId;


    @SerializedName("ToUserName")
    @Expose
    private String ToUserName;

    @SerializedName("ToUserProfileId")
    @Expose
    private int ToUserProfileId;



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

    public int getListOwnerId() {
        return ListOwnerId;
    }

    public void setListOwnerId(int listOwnerId) {
        ListOwnerId = listOwnerId;
    }

    public String getToUserName() {
        return ToUserName;
    }

    public void setToUserName(String toUserName) {
        ToUserName = toUserName;
    }

    public int getToUserProfileId() {
        return ToUserProfileId;
    }

    public void setToUserProfileId(int toUserProfileId) {
        ToUserProfileId = toUserProfileId;
    }



}
