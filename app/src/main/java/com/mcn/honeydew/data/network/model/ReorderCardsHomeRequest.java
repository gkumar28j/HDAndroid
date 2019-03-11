package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by gkumar on 23/2/18.
 */

public class ReorderCardsHomeRequest {

    @SerializedName("ListId")
    @Expose
    private int listId;


    @SerializedName("OrderNumber")
    @Expose
    private int orderNumber;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

}


