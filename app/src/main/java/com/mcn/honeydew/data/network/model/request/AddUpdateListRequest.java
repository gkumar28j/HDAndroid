package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 28/2/18.
 */

public class AddUpdateListRequest {

    @SerializedName("ListId")
    @Expose
    private int listId;

    @SerializedName("ListName")
    @Expose
    private String listName;

    @SerializedName("ListFontSize")
    @Expose
    private int listFontSize;

    @SerializedName("ListHeaderColor")
    @Expose
    private String listHeaderColor;

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


}
