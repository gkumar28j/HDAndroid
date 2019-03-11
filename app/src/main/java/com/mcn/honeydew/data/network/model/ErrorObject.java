package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 21/2/18.
 */

public class ErrorObject {

    @Expose
    @SerializedName("Status")
    private int Status;

    @Expose
    @SerializedName("ErrorCode")
    private int ErrorCode;

    @Expose
    @SerializedName("ErrorMessage")
    private String ErrorMessage;

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public void setErrorCode(int errorCode) {
        ErrorCode = errorCode;
    }

    public String getErrorMessage() {
        return ErrorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        ErrorMessage = errorMessage;
    }
}
