package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 13/3/18.
 */

public class DeleteItemListRequest {

    @SerializedName("ItemId")
    @Expose
    private int itemId;

    @SerializedName("ListId")
    @Expose
    private int listId;

    @SerializedName("ListName")
    @Expose
    private String listName;

    public DeleteItemListRequest(int itemId, int listId, String listName) {
        this.itemId = itemId;
        this.listId = listId;
        this.listName = listName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
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
