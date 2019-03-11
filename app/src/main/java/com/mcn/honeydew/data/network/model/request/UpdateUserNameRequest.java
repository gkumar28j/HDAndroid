package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 26/2/18.
 */

public class UpdateUserNameRequest {

    @SerializedName("FirstName")
    @Expose
    private String firstName;

    public UpdateUserNameRequest(String firstName) {
        this.firstName=firstName;

    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
