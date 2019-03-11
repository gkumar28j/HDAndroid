package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 22/2/18.
 */

public class UpdateVerificationStatusRequest {

    public UpdateVerificationStatusRequest() {
    }

    @Expose
    @SerializedName("Status")
    private int status;

    public UpdateVerificationStatusRequest(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
