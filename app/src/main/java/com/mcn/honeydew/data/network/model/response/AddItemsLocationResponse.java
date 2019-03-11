package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import java.util.List;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddItemsLocationResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("result")
    @Expose
    private List<AddItemsLocationData> result = null;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<AddItemsLocationData> getResult() {
        return result;
    }

    public void setResult(List<AddItemsLocationData> result) {
        this.result = result;
    }


    public static class AddItemsLocationData {

        @SerializedName("Location")
        @Expose
        private String location;

        public String getLocation() {
            return location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

    }

}
