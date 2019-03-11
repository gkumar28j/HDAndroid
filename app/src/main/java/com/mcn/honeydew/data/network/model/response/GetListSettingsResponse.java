package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

public class GetListSettingsResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("result")
    @Expose
    private ListSettings[] result;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public ListSettings[] getResult() {
        return result;
    }

    public void setResult(ListSettings[] result) {
        this.result = result;
    }

    public class ListSettings {
        @SerializedName("UserProfileId")
        @Expose
        private int userProfileId;

        @SerializedName("ListId")
        @Expose
        private int listId;

        @SerializedName("InProgress")
        @Expose
        private boolean inProgress;

        public int getUserProfileId() {
            return userProfileId;
        }

        public void setUserProfileId(int userProfileId) {
            this.userProfileId = userProfileId;
        }

        public int getListId() {
            return listId;
        }

        public void setListId(int listId) {
            this.listId = listId;
        }

        public boolean isInProgress() {
            return inProgress;
        }

        public void setInProgress(boolean inProgress) {
            this.inProgress = inProgress;
        }
    }
}
