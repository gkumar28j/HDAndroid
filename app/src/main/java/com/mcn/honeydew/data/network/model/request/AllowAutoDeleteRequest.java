package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 14/3/18.
 */

public class AllowAutoDeleteRequest {
    @SerializedName("IsAllowDeleteItemOnCompletion")
    @Expose
    private int isAllowDeleteItemOnCompletion;

    @SerializedName("TouserEmailOrPhonenumber")
    @Expose
    private String touserEmailOrPhonenumber;

    @SerializedName("ListId")
    @Expose
    private int listId;



    public int getIsAllowDeleteItemOnCompletion() {
        return isAllowDeleteItemOnCompletion;
    }

    public void setIsAllowDeleteItemOnCompletion(int isAllowDeleteItemOnCompletion) {
        this.isAllowDeleteItemOnCompletion = isAllowDeleteItemOnCompletion;
    }

    public String getTouserEmailOrPhonenumber() {
        return touserEmailOrPhonenumber;
    }

    public void setTouserEmailOrPhonenumber(String touserEmailOrPhonenumber) {
        this.touserEmailOrPhonenumber = touserEmailOrPhonenumber;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }
}
