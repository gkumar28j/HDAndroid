package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

/**
 * Created by gkumar on 23/3/18.
 */

public class BluetoothResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;
    @SerializedName("result")
    @Expose
    private BluetoothResult result;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public BluetoothResult getResult() {
        return result;
    }

    public void setResult(BluetoothResult result) {
        this.result = result;
    }


    public static class BluetoothResult {

        @SerializedName("Message")
        @Expose
        private String message;
        @SerializedName("Status")
        @Expose
        private int status;
        @SerializedName("UserId")
        @Expose
        private String userId;
        @SerializedName("NewItemId")
        @Expose
        private int newItemId;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getNewItemId() {
            return newItemId;
        }

        public void setNewItemId(int newItemId) {
            this.newItemId = newItemId;
        }

    }

}
