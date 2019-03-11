package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;

import java.util.List;

/**
 * Created by gkumar on 26/2/18.
 */

public class HomeDetailListResponse {

    @SerializedName("ErrorObject")
    @Expose
    private ErrorObject errorObject;

    @SerializedName("result")
    @Expose
    private List<MyListResponseData> myListResponseData = null;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public List<MyListResponseData> getMyListResponseData() {
        return myListResponseData;
    }

    public void setMyListResponseData(List<MyListResponseData> myListResponseData) {
        this.myListResponseData = myListResponseData;
    }


}
