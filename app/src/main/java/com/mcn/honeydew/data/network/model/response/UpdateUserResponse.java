package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 23/2/18.
 */

public class UpdateUserResponse {

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("Status")
    @Expose
    private int status;


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }


}
