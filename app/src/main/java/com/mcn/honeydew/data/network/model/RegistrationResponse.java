package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 20/2/18.
 */

public class RegistrationResponse {

    @Expose
    @SerializedName("Message")
    private String message;

    @Expose
    @SerializedName("Status")
    private int status;

    @Expose
    @SerializedName("UserId")
    private String userId;

    @Expose
    @SerializedName("NewItemId")
    private String newItemId;

    public RegistrationResponse(String message, int status, String userId, String newItemId) {
        this.message = message;
        this.status = status;
        this.userId = userId;
        this.newItemId = newItemId;
    }

    public String getMessage() {
        return message;
    }

    public int getStatus() {
        return status;
    }

    public String getUserId() {
        return userId;
    }

    public String getNewItemId() {
        return newItemId;
    }
}
