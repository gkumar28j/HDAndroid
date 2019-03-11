package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import java.util.List;

/**
 * Created by gkumar on 7/3/18.
 */

public class RecentItemsResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;
    @SerializedName("result")
    @Expose
    private List<RecentItemsData> result = null;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<RecentItemsData> getResult() {
        return result;
    }

    public void setResult(List<RecentItemsData> result) {
        this.result = result;
    }


    public static class RecentItemsData {

        @SerializedName("ItemId")
        @Expose
        private int itemId;
        @SerializedName("ItemName")
        @Expose
        private String itemName;
        @SerializedName("ListId")
        @Expose
        private int listId;
        @SerializedName("Location")
        @Expose
        private String location;
        @SerializedName("LocationId")
        @Expose
        private int locationId;

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public int getListId() {
            return listId;
        }

        public void setListId(int listId) {
            this.listId = listId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public int getLocationId() {
            return locationId;
        }

        public void setLocationId(int locationId) {
            this.locationId = locationId;
        }

    }
}