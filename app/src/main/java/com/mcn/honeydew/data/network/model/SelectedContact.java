package com.mcn.honeydew.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by atiwari on 13/3/18.
 */

public class SelectedContact implements Parcelable {

    @SerializedName("EmailorPhoneNumber")
    private String emailorPhoneNumber;

    @SerializedName("LabelText")
    private String labelText;

    private boolean isLoading;
    private boolean isChecked;
    private int status;
    private String message;


    public SelectedContact(String emailorPhoneNumber, String labelText, boolean isChecked) {
        this.emailorPhoneNumber = emailorPhoneNumber;
        this.labelText = labelText;
        this.isChecked = isChecked;
    }

    protected SelectedContact(Parcel in) {
        emailorPhoneNumber = in.readString();
        labelText = in.readString();
        isLoading = in.readByte() == 1;
        status = in.readInt();
        message = in.readString();
        isChecked = in.readByte() == 1;
    }

    public static final Creator<SelectedContact> CREATOR = new Creator<SelectedContact>() {
        @Override
        public SelectedContact createFromParcel(Parcel in) {
            return new SelectedContact(in);
        }

        @Override
        public SelectedContact[] newArray(int size) {
            return new SelectedContact[size];
        }
    };

    public String getEmailorPhoneNumber() {
        return emailorPhoneNumber;
    }

    public String getLabelText() {
        return labelText;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(emailorPhoneNumber);
        dest.writeString(labelText);
        dest.writeByte((byte) (isLoading ? 1 : 0));
        dest.writeInt(status);
        dest.writeString(message);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else if (obj instanceof SelectedContact) {
            SelectedContact cust = (SelectedContact) obj;
            if ((cust.getEmailorPhoneNumber() == null && emailorPhoneNumber == null) ||
                    (cust.getEmailorPhoneNumber().equals(emailorPhoneNumber) && ((cust.getLabelText() == null && labelText == null)
                            || cust.getLabelText().equals(labelText)))) {

                return true;
            }
        }
        return false;
    }
}
