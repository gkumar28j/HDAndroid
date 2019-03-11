package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 13/3/18.
 */

public class ChangeItemStatusRequest {

    @SerializedName("ItemId")
    @Expose
    private int itemId;

    @SerializedName("StatusId")
    @Expose
    private int statusId;

    @SerializedName("ListId")
    @Expose
    private int listId;

    @SerializedName("ListName")
    @Expose
    private String listName;

    public ChangeItemStatusRequest(int itemId, int statusId, int listId, String listName) {
        this.itemId = itemId;
        this.statusId = statusId;
        this.listId = listId;
        this.listName = listName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
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
}
