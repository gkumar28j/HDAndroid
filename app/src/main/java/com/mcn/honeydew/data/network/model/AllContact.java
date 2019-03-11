package com.mcn.honeydew.data.network.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by atiwari on 10/3/18.
 */

public class AllContact implements Parcelable {
    private String contactName;
    private String contactNumber;
    private List<String> contactList;
    private boolean isChecked;

    public AllContact() {
    }

    protected AllContact(Parcel in) {
        contactName = in.readString();
        contactNumber = in.readString();
        contactList = in.createStringArrayList();
        isChecked = in.readByte() == 1;
    }

    public static final Creator<AllContact> CREATOR = new Creator<AllContact>() {
        @Override
        public AllContact createFromParcel(Parcel in) {
            return new AllContact(in);
        }

        @Override
        public AllContact[] newArray(int size) {
            return new AllContact[size];
        }
    };

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getContactName() {
        return contactName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public List<String> getContactList() {
        return contactList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public void setContactList(List<String> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(contactName);
        dest.writeString(contactNumber);
        dest.writeStringList(contactList);
        dest.writeByte((byte) (isChecked ? 1 : 0));
    }
}
