package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by atiwari on 21/3/18.
 */

public class GetProximityResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;
    @SerializedName("result")
    @Expose
    private List<Result> result = null;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }


    public static class Result {

        private int id;
        private boolean isAdded;

        @SerializedName("ItemId")
        @Expose
        private int itemId;

        @SerializedName("ListId")
        @Expose
        private int listId;

        @SerializedName("ItemName")
        @Expose
        private String itemName;

        @SerializedName("ListName")
        @Expose
        private String listName;

        @SerializedName("Latitude")
        @Expose
        private double latitude;

        @SerializedName("Longitude")
        @Expose
        private double longitude;

        @SerializedName("SharedTo")
        @Expose
        private int sharedTo;

        @SerializedName("SharedBy")
        @Expose
        private int sharedBy;

        @SerializedName("UserProfileId")
        @Expose
        private String userProfileId;

        @SerializedName("Location")
        @Expose
        private String location;

        @SerializedName("ProximityId")
        @Expose
        private String proximityId;

        @SerializedName("CreatedDate")
        @Expose
        private String createdDate;

        @SerializedName("ToUserProfileId")
        @Expose
        private String toUserProfileId;

        @SerializedName("FromUserProfileId")
        @Expose
        private String fromUserProfileId;

        @SerializedName("StatusId")
        @Expose
        private int statusId;

        @SerializedName("FromUserName")
        @Expose
        private String fromUserName;

        @SerializedName("ListHeaderColor")
        @Expose
        private String listHeaderColor;

        @SerializedName("ItemTime")
        @Expose
        private String itemTime;

        private double distanceFromCurrentLoc;

        public Result() {
        }


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }


        public boolean isAdded() {
            return isAdded;
        }

        public void setAdded(boolean added) {
            isAdded = added;
        }

        public int getStatusId() {
            return statusId;
        }

        public void setStatusId(int statusId) {
            this.statusId = statusId;
        }

        public String getFromUserName() {
            return fromUserName;
        }

        public void setFromUserName(String fromUserName) {
            this.fromUserName = fromUserName;
        }

        public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getListId() {
            return listId;
        }

        public void setListId(int listId) {
            this.listId = listId;
        }

        public String getItemName() {
            return itemName;
        }

        public void setItemName(String itemName) {
            this.itemName = itemName;
        }

        public String getListName() {
            return listName;
        }

        public void setListName(String listName) {
            this.listName = listName;
        }


        public void setLatitude(int latitude) {
            this.latitude = latitude;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public void setLongitude(int longitude) {
            this.longitude = longitude;
        }

        public int getSharedTo() {
            return sharedTo;
        }

        public void setSharedTo(int sharedTo) {
            this.sharedTo = sharedTo;
        }

        public int getSharedBy() {
            return sharedBy;
        }

        public void setSharedBy(int sharedBy) {
            this.sharedBy = sharedBy;
        }

        public String getUserProfileId() {
            return userProfileId;
        }

        public void setUserProfileId(String userProfileId) {
            this.userProfileId = userProfileId;
        }

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public String getProximityId() {
            return proximityId;
        }

        public void setProximityId(String proximityId) {
            this.proximityId = proximityId;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getToUserProfileId() {
            return toUserProfileId;
        }

        public void setToUserProfileId(String toUserProfileId) {
            this.toUserProfileId = toUserProfileId;
        }

        public String getFromUserProfileId() {
            return fromUserProfileId;
        }

        public void setFromUserProfileId(String fromUserProfileId) {
            this.fromUserProfileId = fromUserProfileId;
        }

        public String getListHeaderColor() {
            return listHeaderColor;
        }

        public void setListHeaderColor(String listHeaderColor) {
            this.listHeaderColor = listHeaderColor;
        }

        public String getItemTime() {
            return itemTime;
        }

        public void setItemTime(String itemTime) {
            this.itemTime = itemTime;
        }

        public double getDistanceFromCurrentLoc() {
            return distanceFromCurrentLoc;
        }

        public void setDistanceFromCurrentLoc(double distanceFromCurrentLoc) {
            this.distanceFromCurrentLoc = distanceFromCurrentLoc;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this).append("createdDate", createdDate).append("fromUserName", fromUserName).append("fromUserProfileId", fromUserProfileId).append("itemId", itemId).append("itemName", itemName).append("latitude", latitude).append("listHeaderColor", listHeaderColor).append("listId", listId).append("listName", listName).append("location", location).append("longitude", longitude).append("proximityId", proximityId).append("sharedBy", sharedBy).append("sharedTo", sharedTo).append("statusId", statusId).append("toUserProfileId", toUserProfileId).append("userProfileId", userProfileId).append("distanceFromCurrentLoc", distanceFromCurrentLoc).toString();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(sharedTo).append(fromUserProfileId).append(listName).append(itemName).append(sharedBy).append(location).append(itemId).append(statusId).append(toUserProfileId).append(listId).append(userProfileId).append(longitude).append(proximityId).append(latitude).append(fromUserName).append(createdDate).append(listHeaderColor).append(distanceFromCurrentLoc).toHashCode();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }
            if ((other instanceof Result) == false) {
                return false;
            }
            Result rhs = ((Result) other);
            return new EqualsBuilder().append(location, rhs.location).append(listId, rhs.listId).append(longitude, rhs.longitude).append(latitude, rhs.latitude).isEquals();
            //return new EqualsBuilder().append(sharedTo, rhs.sharedTo).append(fromUserProfileId, rhs.fromUserProfileId).append(listName, rhs.listName).append(itemName, rhs.itemName).append(sharedBy, rhs.sharedBy).append(location, rhs.location).append(itemId, rhs.itemId).append(statusId, rhs.statusId).append(toUserProfileId, rhs.toUserProfileId).append(listId, rhs.listId).append(userProfileId, rhs.userProfileId).append(longitude, rhs.longitude).append(proximityId, rhs.proximityId).append(latitude, rhs.latitude).append(fromUserName, rhs.fromUserName).append(createdDate, rhs.createdDate).append(listHeaderColor, rhs.listHeaderColor).append(distanceFromCurrentLoc, rhs.distanceFromCurrentLoc).isEquals();
        }
    }


}
