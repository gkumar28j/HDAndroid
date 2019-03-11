package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by gkumar on 20/2/18.
 */

public class HomeResponse {

    @Expose
    @SerializedName("ErrorObject")
    private ErrorObject errorObject;

    @Expose
    @SerializedName("result")
    private ArrayList<MyHomeListData> results;

    public ErrorObject getErrorObject() {
        return errorObject;
    }

    public void setErrorObject(ErrorObject errorObject) {
        this.errorObject = errorObject;
    }

    public ArrayList<MyHomeListData> getResults() {
        return results;
    }

    public void setResults(ArrayList<MyHomeListData> results) {
        this.results = results;
    }
}
