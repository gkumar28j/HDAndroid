

package com.mcn.honeydew.data.network.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by amit on 2/1/18.
 */

public class LoginRequest {

    private LoginRequest() {
        // This class is not publicly instantiable
    }

    public static class ServerLoginRequest {

        @Expose
        @SerializedName("grant_type")
        private String grantType;

        @Expose
        @SerializedName("username")
        private String email;

        @Expose
        @SerializedName("password")
        private String password;


        public ServerLoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
            this.grantType = "password";
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getGrantType() {
            return grantType;
        }

        public void setGrantType(String grantType) {
            this.grantType = grantType;
        }
    }


    /*public static class FacebookLoginRequest {
        @Expose
        @SerializedName("fb_user_id")
        private String fbUserId;

        @Expose
        @SerializedName("fb_access_token")
        private String fbAccessToken;

        public FacebookLoginRequest(String fbUserId, String fbAccessToken) {
            this.fbUserId = fbUserId;
            this.fbAccessToken = fbAccessToken;
        }

        public String getFbUserId() {
            return fbUserId;
        }

        public void setFbUserId(String fbUserId) {
            this.fbUserId = fbUserId;
        }

        public String getFbAccessToken() {
            return fbAccessToken;
        }

        public void setFbAccessToken(String fbAccessToken) {
            this.fbAccessToken = fbAccessToken;
        }
    }*/
}
