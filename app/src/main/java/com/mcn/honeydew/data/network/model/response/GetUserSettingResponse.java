package com.mcn.honeydew.data.network.model.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mcn.honeydew.data.network.model.ErrorObject;

import java.util.List;

/**
 * Created by atiwari on 10/3/18.
 */

public class GetUserSettingResponse {

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
        @SerializedName("IsSendPushNotification")
        @Expose
        private boolean isSendPushNotification;

        @SerializedName("IsAllowDeleteItemOnCompletion")
        @Expose
        private boolean isAllowDeleteItemOnCompletion;

        @SerializedName("MyEmail")
        @Expose
        private String myEmail;

        @SerializedName("SharedUserEmail")
        @Expose
        private String sharedUserEmail;

        @SerializedName("IsOwner")
        @Expose
        private boolean isOwner;

        @SerializedName("LabelText")
        @Expose
        private String labelText;

        @SerializedName("ToUserIsSendPushNotification")
        @Expose
        private boolean toUserIsSendPushNotification;

        @SerializedName("ShareUserMobile")
        @Expose
        private String shareUserMobile;

        @SerializedName("LoginUserId")
        @Expose
        private String loginUserId;

        public boolean isIsSendPushNotification() {
            return isSendPushNotification;
        }

        public void setIsSendPushNotification(boolean isSendPushNotification) {
            this.isSendPushNotification = isSendPushNotification;
        }

        public boolean isIsAllowDeleteItemOnCompletion() {
            return isAllowDeleteItemOnCompletion;
        }

        public void setIsAllowDeleteItemOnCompletion(boolean isAllowDeleteItemOnCompletion) {
            this.isAllowDeleteItemOnCompletion = isAllowDeleteItemOnCompletion;
        }

        public String getMyEmail() {
            return myEmail;
        }

        public void setMyEmail(String myEmail) {
            this.myEmail = myEmail;
        }

        public String getSharedUserEmail() {
            return sharedUserEmail;
        }

        public void setSharedUserEmail(String sharedUserEmail) {
            this.sharedUserEmail = sharedUserEmail;
        }

        public boolean isIsOwner() {
            return isOwner;
        }

        public void setIsOwner(boolean isOwner) {
            this.isOwner = isOwner;
        }

        public String getLabelText() {
            return labelText;
        }

        public void setLabelText(String labelText) {
            this.labelText = labelText;
        }

        public boolean isToUserIsSendPushNotification() {
            return toUserIsSendPushNotification;
        }

        public void setToUserIsSendPushNotification(boolean toUserIsSendPushNotification) {
            this.toUserIsSendPushNotification = toUserIsSendPushNotification;
        }

        public String getShareUserMobile() {
            return shareUserMobile;
        }

        public void setShareUserMobile(String shareUserMobile) {
            this.shareUserMobile = shareUserMobile;
        }

        public String getLoginUserId() {
            return loginUserId;
        }

        public void setLoginUserId(String loginUserId) {
            this.loginUserId = loginUserId;
        }
    }
}
