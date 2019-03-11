package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 7/3/18.
 */

public class UpdateHeaderColorRequest {
    @SerializedName("ListId")
    @Expose
    private int listId;
    @SerializedName("ListHeaderColor")
    @Expose
    private String listHeaderColor;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListHeaderColor() {
        return listHeaderColor;
    }

    public void setListHeaderColor(String listHeaderColor) {
        this.listHeaderColor = listHeaderColor;
    }
}
