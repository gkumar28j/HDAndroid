package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 23/2/18.
 */

public class HomeListCardDeleteRequest {


    @Expose
    @SerializedName("ListId")
    private int listIds;

    public HomeListCardDeleteRequest(int listId) {
        this.listIds = listId;
    }

    public int getListIds() {
        return listIds;
    }

}
