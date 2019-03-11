package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gkumar on 20/2/18.
 */

public class MyHomeListData {


    @Expose
    @SerializedName("ListOwnerId")
    private int listOwnerId;

    @Expose
    @SerializedName("ListOwnerName")
    private String listOwnerName;

    @Expose
    @SerializedName("CreatedDate")
    private String createdDate;

    @Expose
    @SerializedName("ListId")
    private int listId;

    @Expose
    @SerializedName("ListName")
    private String listName;

    @Expose
    @SerializedName("ListFontSize")
    private int listFontSize;

    @Expose
    @SerializedName("ListHeaderColor")
    private String listHeaderColor;


    @Expose
    @SerializedName("ToUserProfileId")
    private int toUserProfileId;

    @Expose
    @SerializedName("ListSharedOnDate")
    private String listSharedOnDate;

    @Expose
    @SerializedName("ToUserName")
    private String toUserName;

    @Expose
    @SerializedName("ListOrderNumber")
    private int listOrderNumber;

    @Expose
    @SerializedName("IsOwner")
    private boolean isOwner;

    @Expose
    @SerializedName("IsSharedByOwner")
    private boolean isSharedByOwner;

    @Expose
    @SerializedName("ItemsByList")
    private ArrayList<MyHomeChildData> myHomeChildData;

    private boolean isLongSelected;

    public boolean isLongSelected() {
        return isLongSelected;
    }

    public void setLongSelected(boolean longSelected) {
        isLongSelected = longSelected;
    }



    public int getListOwnerId() {
        return listOwnerId;
    }

    public void setListOwnerId(int listOwnerId) {
        this.listOwnerId = listOwnerId;
    }

    public String getListOwnerName() {
        return listOwnerName;
    }

    public void setListOwnerName(String listOwnerName) {
        this.listOwnerName = listOwnerName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getListFontSize() {
        return listFontSize;
    }

    public void setListFontSize(int listFontSize) {
        this.listFontSize = listFontSize;
    }

    public String getListHeaderColor() {
        return listHeaderColor;
    }

    public void setListHeaderColor(String listHeaderColor) {
        this.listHeaderColor = listHeaderColor;
    }

    public int getToUserProfileId() {
        return toUserProfileId;
    }

    public void setToUserProfileId(int toUserProfileId) {
        this.toUserProfileId = toUserProfileId;
    }

    public String getListSharedOnDate() {
        return listSharedOnDate;
    }

    public void setListSharedOnDate(String listSharedOnDate) {
        this.listSharedOnDate = listSharedOnDate;
    }

    public String getToUserName() {
        return toUserName;
    }

    public void setToUserName(String toUserName) {
        this.toUserName = toUserName;
    }

    public int getListOrderNumber() {
        return listOrderNumber;
    }

    public void setListOrderNumber(int listOrderNumber) {
        this.listOrderNumber = listOrderNumber;
    }

    public boolean isIsOwner() {
        return isOwner;
    }

    public void setIsOwner(boolean isOwner) {
        this.isOwner = isOwner;
    }

    public boolean isIsSharedByOwner() {
        return isSharedByOwner;
    }

    public void setIsSharedByOwner(boolean isSharedByOwner) {
        this.isSharedByOwner = isSharedByOwner;
    }

    public ArrayList<MyHomeChildData> getItemsByList() {
        return myHomeChildData;
    }

    public void setItemsByList(ArrayList<MyHomeChildData> itemsByList) {
        this.myHomeChildData = itemsByList;
    }




/*
----------------------------------- inner child data -----------------------------------
*/


    public static class MyHomeChildData {

        @Expose
        @SerializedName("Id")
        private int id;

        @Expose
        @SerializedName("ListId")
        private int listId;

        @Expose
        @SerializedName("ItemId")
        private int itemId;

        @Expose
        @SerializedName("ItemName")
        private String itemName;

        @Expose
        @SerializedName("ItemCreatedOn")
        private String itemCreatedOn;

        @Expose
        @SerializedName("ItemOrderNumber")
        private int itemOrderNumber;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getListId() {
            return listId;
        }

        public void setListId(int listId) {
            this.listId = listId;
        }

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

        public String getItemCreatedOn() {
            return itemCreatedOn;
        }

        public void setItemCreatedOn(String itemCreatedOn) {
            this.itemCreatedOn = itemCreatedOn;
        }

        public int getItemOrderNumber() {
            return itemOrderNumber;
        }

        public void setItemOrderNumber(int itemOrderNumber) {
            this.itemOrderNumber = itemOrderNumber;
        }

    }
}

