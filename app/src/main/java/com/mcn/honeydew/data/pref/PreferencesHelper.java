package com.mcn.honeydew.data.pref;

import android.location.Location;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.HomeDetailListResponse;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;

import java.util.ArrayList;

/**
 * Created by amit on 14/2/18.
 */

public interface PreferencesHelper {

    int getCurrentUserLoggedInMode();

    void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode);

    String getAccessToken();

    void setAccessToken(String accessToken);

    String getTokenType();

    void setTokenType(String tokenType);

    String getRefreshToken();

    void setRefreshToken(String refreshToken);

    void setUserData(UserDetailResponse response);

    UserDetailResponse getUserData();

    void setProximitySettings(NotificationSettingsResponse response);

    NotificationSettingsResponse getProximitySettings();

    void setSelectedColor(String htmlColorCode);

    String getSavedColorCode();

    void saveList(MyHomeListData data);

    MyHomeListData getSavedList();

    void saveDeviceId(String refreshedToken);

    String getDeviceId();

    void sendDeviceIdToServer(boolean b);

    boolean IsDeviceIdSendToServer();

    void saveProximityItems(String itemsJsonString);

    String getSavedProximityItems();

    void setBluetoothNotificationsEnabled(boolean val);

    boolean getBluetoothNotificationEnabled();

    boolean getIsItemAdded();

    void setIsItemAdded(boolean isAdded);

    boolean isJustLoggedIn();

    void setIsJustLoggedIn(boolean isTrue);

    void setCurrentLocation(Location location);

    Location getCurrentLocation();

    void setIsFacebookLogin(boolean isTrue);

    boolean isFacebookLogin();

    void saveBluetoothItemList(String jsonData);

    ArrayList<GetBluetoothItemsListResponse.BluetoothItem> getSavedBluetoothItems();

    void setBluetoothDeviceConnected(boolean isConnected);

    boolean isBluetoothDeviceConnected();

    void setInProgressValue(boolean isInProgress);

    boolean isInProgressValue();

    void saveHomeResponseData(String response);

    ArrayList<MyHomeListData> getHomeResponseData();

    void saveNotificationResponseData(String response);

    ArrayList<NotificationListResponse.NotificationListData> getNotificationData();

    void saveAppSettings(String response);

    NotificationSettingsResponse getNotificationSettingResponse();

    void setFirstTimeLoggedIn(boolean isFirstTime);

    boolean isFirstTimeLoggedIn();


}
