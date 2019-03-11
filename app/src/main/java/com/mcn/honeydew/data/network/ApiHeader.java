package com.mcn.honeydew.data.network;

import javax.inject.Singleton;

/**
 * Created by amit on 14/2/18.
 */

@Singleton
public class ApiHeader {

    public static final String API_AUTH_TYPE = "API_AUTH_TYPE";
    public static final String PUBLIC_API = "PUBLIC_API";
    public static final String PROTECTED_API = "PROTECTED_API";


    public static final String HEADER_PARAM_ACCESS_TOKEN = "access_token";
    public static final String HEADER_PARAM_TOKEN_TYPE = "token_type";
    public static final String HEADER_PARAM_REFRESH_TOKEN = "refresh_token";
    public static final String HEADER_PARAM_API_KEY = "X-HDApiKey";
    public static final String HEADER_PARAM_AUTHRIZATION = "Authorization";

    private String mApiKey;
    private String mAccessToken;
    private String mRefreshToken;
    private String mTokenType;

    public ApiHeader(String mApiKey, String mAccessToken, String mRefreshToken, String mTokenType) {
        this.mApiKey = mApiKey;
        this.mAccessToken = mAccessToken;
        this.mRefreshToken = mRefreshToken;
        this.mTokenType = mTokenType;
    }

    public String getmApiKey() {
        return mApiKey;
    }

    public void setmApiKey(String mApiKey) {
        this.mApiKey = mApiKey;
    }

    public String getmAccessToken() {
        return mAccessToken;
    }

    public void setmAccessToken(String mAccessToken) {
        this.mAccessToken = mAccessToken;
    }

    public String getmRefreshToken() {
        return mRefreshToken;
    }

    public void setmRefreshToken(String mRefreshToken) {
        this.mRefreshToken = mRefreshToken;
    }

    public String getmTokenType() {
        return mTokenType;
    }

    public void setmTokenType(String mTokenType) {
        this.mTokenType = mTokenType;
    }
}

