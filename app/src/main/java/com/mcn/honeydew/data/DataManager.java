package com.mcn.honeydew.data;

import com.mcn.honeydew.data.db.DbHelper;
import com.mcn.honeydew.data.network.ApiHelper;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.pref.PreferencesHelper;

/**
 * Created by amit on 14/2/18.
 */

public interface DataManager extends PreferencesHelper, DbHelper, ApiHelper {

    void setUserAsLoggedOut();

    void updateUserCredential(String accessToken, String refreshToken, String tokenType);

    void updateUserData(UserDetailResponse data, LoggedInMode loggedInModeServer);

    void updateUserData(UserDetailResponse data);

    void updateNewCredential(String accessToken, String refreshToken, String tokenType);


    enum LoggedInMode {

        LOGGED_IN_MODE_LOGGED_OUT(0),
        LOGGED_IN_MODE_SERVER(1);

        private final int mType;

        LoggedInMode(int type) {
            mType = type;
        }

        public int getType() {
            return mType;
        }
    }

    String getCountryListFromAsset();
}

