package com.mcn.honeydew.data.pref;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.di.ApplicationContext;
import com.mcn.honeydew.di.PreferenceInfo;

import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by amit on 14/2/18.
 */

@Singleton
public class AppPreferencesHelper implements PreferencesHelper {

    private static final String PREF_KEY_USER_LOGGED_IN_MODE = "PREF_KEY_USER_LOGGED_IN_MODE";
    private static final String PREF_KEY_CURRENT_USER_ID = "PREF_KEY_CURRENT_USER_ID";
    private static final String PREF_KEY_CURRENT_USER_NAME = "PREF_KEY_CURRENT_USER_NAME";
    private static final String PREF_KEY_CURRENT_USER_MOBILE = "PREF_KEY_CURRENT_USER_MOBILE";
    private static final String PREF_KEY_CURRENT_USER_PROFILE_PIC_URL
            = "PREF_KEY_CURRENT_USER_PROFILE_PIC_URL";
    private static final String PREF_KEY_ACCESS_TOKEN = "PREF_KEY_ACCESS_TOKEN";
    private static final String PREF_KEY_SAVE_FORMATTED_ADD = "PREF_KEY_SAVE_FORMATTED_ADD";
    private static final String PREF_KEY_SAVE_COUNTRY_CODE = "PREF_KEY_SAVE_COUNTRY_CODE";
    private static final String PREF_KEY_CART = "PREF_KEY_CART";

    private static final String PREF_KEY_TOKEN_TYPE = "token_type";
    private static final String PREF_KEY_REFRESH_TOKEN = "refresh_token";
    private static final String PREF_KEY_USER_DATA = "user_data";
    private static final String PREF_KEY_PROXIMITY_DATA = "proximity_data";
    private static final String PREF_KEY_COLOR_CODE = "color_code";
    private static final String PREF_KEY_SELECTED_LIST = "selected_list";
    private static final String PREF_KEY_DEVICE_ID = "device_id";
    private static final String PREF_KEY_DEVICE_SEND_TO_SERVER = "device_send_to_server";
    private static final String PREF_KEY_PROXIMITY_ITEMS = "proximity_items";
    private static final String PREF_KEY_BLUETOOTH_NOTIFICATIONS = "bluetooth_notifications";
    private static final String PREF_KEY_IS_ITEM_ADDED = "is_proximity_item_added";
    private static final String PREF_KEY_JUST_LOGGED_IN = "is_just_logged_in";
    private static final String PREF_KEY_CURRENT_LOCATION = "current_location";
    private static final String PREF_KEY_IS_FB_LOGIN = "is_fb_login";
    private static final String PREF_KEY_BLUETOOTH_ITEM_LIST = "bluetooth_items";
    private static final String PREF_KEY_BLUETOOTH_DEVICE_CONNECTED = "bluetooth_device_connected";
    private static final String PREF_KEY_SAVE_IN_PROGRESS_VALUE = "is_in_progress";


    private final SharedPreferences mPrefs;
    private SharedPreferences.Editor mEditor;

    @Inject
    public AppPreferencesHelper(@ApplicationContext Context context,
                                @PreferenceInfo String prefFileName) {
        mPrefs = context.getSharedPreferences(prefFileName, Context.MODE_PRIVATE);
        mEditor = mPrefs.edit();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPrefs.getInt(PREF_KEY_USER_LOGGED_IN_MODE,
                DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType());
    }

    @Override
    public void setCurrentUserLoggedInMode(DataManager.LoggedInMode mode) {
        mEditor.putInt(PREF_KEY_USER_LOGGED_IN_MODE, mode.getType()).apply();
    }

    @Override
    public String getAccessToken() {
        return mPrefs.getString(PREF_KEY_ACCESS_TOKEN, null);
    }

    @Override
    public void setAccessToken(String accessToken) {
        mEditor.putString(PREF_KEY_ACCESS_TOKEN, accessToken).apply();
    }

    @Override
    public String getTokenType() {
        return mPrefs.getString(PREF_KEY_TOKEN_TYPE, null);
    }

    @Override
    public void setTokenType(String tokenType) {
        mEditor.putString(PREF_KEY_TOKEN_TYPE, tokenType).apply();
    }

    @Override
    public String getRefreshToken() {
        return mPrefs.getString(PREF_KEY_REFRESH_TOKEN, null);
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        mEditor.putString(PREF_KEY_REFRESH_TOKEN, refreshToken).apply();
    }

    @Override
    public void setUserData(UserDetailResponse response) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        mEditor.putString(PREF_KEY_USER_DATA, json).apply();
    }

    @Override
    public UserDetailResponse getUserData() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_KEY_USER_DATA, null);
        return gson.fromJson(json, UserDetailResponse.class);
    }

    @Override
    public void setProximitySettings(NotificationSettingsResponse response) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        mEditor.putString(PREF_KEY_PROXIMITY_DATA, json).apply();
    }

    @Override
    public NotificationSettingsResponse getProximitySettings() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_KEY_PROXIMITY_DATA, null);
        return gson.fromJson(json, NotificationSettingsResponse.class);
    }

    @Override
    public void setSelectedColor(String colorCode) {
        mEditor.putString(PREF_KEY_COLOR_CODE, colorCode).apply();
    }

    @Override
    public String getSavedColorCode() {
        return mPrefs.getString(PREF_KEY_COLOR_CODE, "#153359"); // default color
    }

    @Override
    public void saveList(MyHomeListData data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        mEditor.putString(PREF_KEY_SELECTED_LIST, json).apply();
    }

    @Override
    public MyHomeListData getSavedList() {
        Gson gson = new Gson();
        String json = mPrefs.getString(PREF_KEY_SELECTED_LIST, null);
        return gson.fromJson(json, MyHomeListData.class);
    }

    @Override
    public void saveDeviceId(String refreshedToken) {
        mEditor.putString(PREF_KEY_DEVICE_ID, refreshedToken).apply();

    }

    @Override
    public String getDeviceId() {
        return mPrefs.getString(PREF_KEY_DEVICE_ID, null);
    }

    @Override
    public void sendDeviceIdToServer(boolean b) {
        mEditor.putBoolean(PREF_KEY_DEVICE_SEND_TO_SERVER, b).apply();
    }

    @Override
    public boolean IsDeviceIdSendToServer() {
        return mPrefs.getBoolean(PREF_KEY_DEVICE_SEND_TO_SERVER, false);
    }

    @Override
    public void saveProximityItems(String itemsJsonString) {
        mEditor.putString(PREF_KEY_PROXIMITY_ITEMS, itemsJsonString).apply();
    }

    @Override
    public String getSavedProximityItems() {
        return mPrefs.getString(PREF_KEY_PROXIMITY_ITEMS, null);
    }

    @Override
    public void setBluetoothNotificationsEnabled(boolean val) {
        mEditor.putBoolean(PREF_KEY_BLUETOOTH_NOTIFICATIONS, val).apply();
    }

    @Override
    public boolean getBluetoothNotificationEnabled() {
        return mPrefs.getBoolean(PREF_KEY_BLUETOOTH_NOTIFICATIONS, false);
    }

    @Override
    public boolean getIsItemAdded() {
        return mPrefs.getBoolean(PREF_KEY_IS_ITEM_ADDED, false);
    }

    @Override
    public void setIsItemAdded(boolean isAdded) {
        mEditor.putBoolean(PREF_KEY_IS_ITEM_ADDED, isAdded).apply();

    }

    @Override
    public boolean isJustLoggedIn() {
        return mPrefs.getBoolean(PREF_KEY_JUST_LOGGED_IN, true);
    }

    @Override
    public void setIsJustLoggedIn(boolean isTrue) {
        mEditor.putBoolean(PREF_KEY_JUST_LOGGED_IN, isTrue).apply();
    }

    @Override
    public void setCurrentLocation(Location location) {
        mEditor.putString(PREF_KEY_CURRENT_LOCATION, (location.getLatitude() + "," + location.getLongitude())).apply();
    }

    @Override
    public Location getCurrentLocation() {
        String loc[] = mPrefs.getString(PREF_KEY_CURRENT_LOCATION, "0.0,0.0").split(",");
        Location currentLocation = null;
        if (loc.length > 0) {
            currentLocation = new Location("");//provider name is unnecessary
            currentLocation.setLatitude(Double.valueOf(loc[0]));//your coords of course
            currentLocation.setLongitude(Double.valueOf(loc[1]));
        }
        return currentLocation;
    }

    @Override
    public void setIsFacebookLogin(boolean isTrue) {
        mEditor.putBoolean(PREF_KEY_IS_FB_LOGIN, isTrue).apply();

    }

    @Override
    public boolean isFacebookLogin() {
        return mPrefs.getBoolean(PREF_KEY_IS_FB_LOGIN, false);
    }

    @Override
    public void saveBluetoothItemList(String jsonData) {
        mEditor.putString(PREF_KEY_BLUETOOTH_ITEM_LIST, jsonData).apply();
    }

    @Override
    public ArrayList<GetBluetoothItemsListResponse.BluetoothItem> getSavedBluetoothItems() {
        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> arrayList = new ArrayList<>();
        String json = mPrefs.getString(PREF_KEY_BLUETOOTH_ITEM_LIST, null);

        if (!TextUtils.isEmpty(json)) {
            arrayList.addAll(Arrays.asList(new Gson().fromJson(json, GetBluetoothItemsListResponse.BluetoothItem[].class)));
        }

        return arrayList;
    }

    @Override
    public void setBluetoothDeviceConnected(boolean isConnected) {
        mEditor.putBoolean(PREF_KEY_BLUETOOTH_DEVICE_CONNECTED, isConnected).apply();
    }

    @Override
    public boolean isBluetoothDeviceConnected() {
        return mPrefs.getBoolean(PREF_KEY_BLUETOOTH_DEVICE_CONNECTED, false);
    }

    @Override
    public void setInProgressValue(boolean isInProgress) {
        mEditor.putBoolean(PREF_KEY_SAVE_IN_PROGRESS_VALUE,isInProgress).apply();
    }

    @Override
    public boolean isInProgressValue() {
        return mPrefs.getBoolean(PREF_KEY_SAVE_IN_PROGRESS_VALUE,false);
    }
}

