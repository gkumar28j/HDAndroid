package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 15/3/18.
 */

public class ReorderItemsMyList {


    @SerializedName("ItemId")
    @Expose
    private int ItemId;

    @SerializedName("UserProfileId")
    @Expose
    private int UserProfileId;

    @SerializedName("orderNumber")
    @Expose
    private int orderNumber;

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    public int getUserProfileId() {
        return UserProfileId;
    }

    public void setUserProfileId(int userProfileId) {
        UserProfileId = userProfileId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }


}
