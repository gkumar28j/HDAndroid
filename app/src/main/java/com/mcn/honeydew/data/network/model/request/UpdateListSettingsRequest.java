package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateListSettingsRequest {
    @SerializedName("ListId")
    @Expose
    private int listId;

    @SerializedName("UserProfileId")
    @Expose
    private int userProfileId;

    @SerializedName("status")
    @Expose
    private boolean status;

    public UpdateListSettingsRequest(int listId, int userProfileId, boolean status) {
        this.listId = listId;
        this.userProfileId = userProfileId;
        this.status = status;
    }

    public int getListId() {
        return listId;
    }

    public int getUserProfileId() {
        return userProfileId;
    }

    public boolean isStatus() {
        return status;
    }
}
