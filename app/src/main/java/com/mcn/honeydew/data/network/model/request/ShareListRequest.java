package com.mcn.honeydew.data.network.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 12/3/18.
 */

public class ShareListRequest {

    @SerializedName("ListId")
    @Expose
    private int listId;
    @SerializedName("EmailorPhoneNumber")
    @Expose
    private String emailorPhoneNumber;
    @SerializedName("LabelText")
    @Expose
    private String labelText;

    public int getListId() {
        return listId;
    }

    public void setListId(int listId) {
        this.listId = listId;
    }

    public String getEmailorPhoneNumber() {
        return emailorPhoneNumber;
    }

    public void setEmailorPhoneNumber(String emailorPhoneNumber) {
        this.emailorPhoneNumber = emailorPhoneNumber;
    }

    public String getLabelText() {
        return labelText;
    }

    public void setLabelText(String labelText) {
        this.labelText = labelText;
    }
}
