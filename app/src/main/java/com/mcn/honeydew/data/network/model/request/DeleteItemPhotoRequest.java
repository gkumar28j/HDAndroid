package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteItemPhotoRequest {

    public int getItemId() {
        return ItemId;
    }

    public void setItemId(int itemId) {
        ItemId = itemId;
    }

    @SerializedName("ItemId")
    @Expose
    int ItemId;


}
