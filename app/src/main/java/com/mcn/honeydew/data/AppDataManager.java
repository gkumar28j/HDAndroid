package com.mcn.honeydew.data;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.mcn.honeydew.data.db.DbHelper;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.ApiHelper;
import com.mcn.honeydew.data.network.model.HomeCardDeleteResponse;
import com.mcn.honeydew.data.network.model.HomeDetailListResponse;
import com.mcn.honeydew.data.network.model.HomeListCardDeleteRequest;
import com.mcn.honeydew.data.network.model.HomeResponse;
import com.mcn.honeydew.data.network.model.LoginResponse;
import com.mcn.honeydew.data.network.model.LogoutResponse;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.PhoneVerificationRequest;
import com.mcn.honeydew.data.network.model.PhoneVerificationResponse;
import com.mcn.honeydew.data.network.model.RegistrationRequest;
import com.mcn.honeydew.data.network.model.RegistrationResponse;
import com.mcn.honeydew.data.network.model.ReorderCardsHomeRequest;
import com.mcn.honeydew.data.network.model.ReorderHomeListData;
import com.mcn.honeydew.data.network.model.UpdateVerificationStatusRequest;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.AddUpdateListRequest;
import com.mcn.honeydew.data.network.model.request.AllowAutoDeleteRequest;
import com.mcn.honeydew.data.network.model.request.BluetoothRequest;
import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.request.ChangePasswordRequest;
import com.mcn.honeydew.data.network.model.request.DailyReminderExpiringRequest;
import com.mcn.honeydew.data.network.model.request.DeleteItemListRequest;
import com.mcn.honeydew.data.network.model.request.DeleteItemPhotoRequest;
import com.mcn.honeydew.data.network.model.request.EmailUpdateNewRequest;
import com.mcn.honeydew.data.network.model.request.FacebookLoginRequest;
import com.mcn.honeydew.data.network.model.request.PushNotificationSettingsRequest;
import com.mcn.honeydew.data.network.model.request.ReminderTimeRequest;
import com.mcn.honeydew.data.network.model.request.ReorderItemsMyList;
import com.mcn.honeydew.data.network.model.request.ResetPasswordRequest;
import com.mcn.honeydew.data.network.model.request.SendOtpRequest;
import com.mcn.honeydew.data.network.model.request.SetNotificationPrefRequest;
import com.mcn.honeydew.data.network.model.request.ShareListRequest;
import com.mcn.honeydew.data.network.model.request.UnshareListRequest;
import com.mcn.honeydew.data.network.model.request.UpdateDeviceInfoRequest;
import com.mcn.honeydew.data.network.model.request.UpdateEmailRequest;
import com.mcn.honeydew.data.network.model.request.UpdateHeaderColorRequest;
import com.mcn.honeydew.data.network.model.request.UpdateListSettingsRequest;
import com.mcn.honeydew.data.network.model.request.UpdateProximityRangeRequest;
import com.mcn.honeydew.data.network.model.request.UpdateUserNameRequest;
import com.mcn.honeydew.data.network.model.request.VerifyNewEmailOTPRequest;
import com.mcn.honeydew.data.network.model.request.VerifyOtpRequest;
import com.mcn.honeydew.data.network.model.response.AddItemsLocationResponse;
import com.mcn.honeydew.data.network.model.response.AddUpdateItemResponse;
import com.mcn.honeydew.data.network.model.response.AddUpdateListResponse;
import com.mcn.honeydew.data.network.model.response.BluetoothResponse;
import com.mcn.honeydew.data.network.model.response.ChangeItemStatusResponse;
import com.mcn.honeydew.data.network.model.response.ChangePasswordResponse;
import com.mcn.honeydew.data.network.model.response.DailyReminderExpiredResponse;
import com.mcn.honeydew.data.network.model.response.DailyReminderExpiringResponse;
import com.mcn.honeydew.data.network.model.response.DeleteItemListResponse;
import com.mcn.honeydew.data.network.model.response.DeleteItemPhotoResponse;
import com.mcn.honeydew.data.network.model.response.DeleteRecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.DeleteUserResponse;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.data.network.model.response.FacebookLoginResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.data.network.model.response.NotificationCountResponse;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationReadResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.PushNotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.data.network.model.response.ReminderTimeResponse;
import com.mcn.honeydew.data.network.model.response.ResetNotificationCountResponse;
import com.mcn.honeydew.data.network.model.response.ResetPasswordResponse;
import com.mcn.honeydew.data.network.model.response.SendOtpResponse;
import com.mcn.honeydew.data.network.model.response.ShareListResponse;
import com.mcn.honeydew.data.network.model.response.SystemNotifcationPrefData;
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.data.network.model.response.UpdateHeaderColorResponse;
import com.mcn.honeydew.data.network.model.response.UpdateNotificationSettingRequest;
import com.mcn.honeydew.data.network.model.response.UpdateProximityRangeResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserEmailResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserResponse;
import com.mcn.honeydew.data.network.model.response.VerifyOtpResponse;
import com.mcn.honeydew.data.pref.PreferencesHelper;
import com.mcn.honeydew.di.ApplicationContext;
import com.mcn.honeydew.utils.CommonUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by amit on 14/2/18.
 */

@Singleton
public class AppDataManager implements DataManager {

    private static final String TAG = "AppDataManager";

    private final Context mContext;
    private final PreferencesHelper mPreferencesHelper;
    private final DbHelper mDbHelper;
    private final ApiHelper mApiHelper;


    @Inject
    public AppDataManager(@ApplicationContext Context context,
                          PreferencesHelper preferencesHelper, DbHelper dbHelper, ApiHelper apiHelper) {
        mContext = context;
        mPreferencesHelper = preferencesHelper;
        mDbHelper = dbHelper;
        mApiHelper = apiHelper;

    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHelper.getApiHeader();
    }

    @Override
    public void setApiHeader(ApiHeader apiHeader) {
        mApiHelper.setApiHeader(apiHeader);
    }

    @Override
    public Observable<LoginResponse> doServerLoginApiCall(String username, String grantType, String password) {
        return mApiHelper.doServerLoginApiCall(username, grantType, password);
    }

    @Override
    public Observable<RegistrationResponse> doRegisterApiCall(RegistrationRequest request) {
        return mApiHelper.doRegisterApiCall(request);
    }

    @Override
    public Observable<HomeResponse> doHomeApiCall() {
        return mApiHelper.doHomeApiCall();
    }

    @Override
    public Observable<UserDetailResponse> doUserDetailsApiCall() {
        return mApiHelper.doUserDetailsApiCall();
    }

    @Override
    public Observable<PhoneVerificationResponse> doPhoneVerificationApiCall(PhoneVerificationRequest request) {
        return mApiHelper.doPhoneVerificationApiCall(request);
    }

    @Override
    public Observable<PhoneVerificationResponse> doUpdateVerificationApiCall(UpdateVerificationStatusRequest request) {
        return mApiHelper.doUpdateVerificationApiCall(request);
    }

    public Observable<LogoutResponse> doLogoutApiCall() {
        return mApiHelper.doLogoutApiCall();
    }


    @Override
    public Observable<FacebookLoginResponse> doLoginWithFacebookApiCall(FacebookLoginRequest request) {
        return mApiHelper.doLoginWithFacebookApiCall(request);
    }

    @Override
    public Observable<LocateAccountResponse> doLocateAccountApiCall(String emailOrPhone) {
        return mApiHelper.doLocateAccountApiCall(emailOrPhone);
    }

    @Override
    public Observable<SendOtpResponse> doSendOtpApiCall(SendOtpRequest request) {
        return mApiHelper.doSendOtpApiCall(request);
    }

    @Override
    public Observable<HomeDetailListResponse> doGetHomeDetailList(String Id) {
        return mApiHelper.doGetHomeDetailList(Id);
    }


    @Override
    public Observable<HomeDetailListResponse> doGetHomeDetailListCall(String Id) {
        return mApiHelper.doGetHomeDetailListCall(Id);
    }


    @Override
    public Observable<HomeCardDeleteResponse> doDeleteCards(HomeListCardDeleteRequest request) {

        return mApiHelper.doDeleteCards(request);
    }

    @Override
    public Observable<UpdateUserResponse> doUpdateUserName(UpdateUserNameRequest request) {
        return mApiHelper.doUpdateUserName(request);
    }

    @Override
    public Observable<NotificationSettingsResponse> doNotificationSettingsCall() {
        return mApiHelper.doNotificationSettingsCall();
    }

    @Override
    public Observable<ChangePasswordResponse> doChangePasswordCall(ChangePasswordRequest request) {
        return mApiHelper.doChangePasswordCall(request);
    }

    @Override
    public Observable<AddUpdateListResponse> doAddUpdateListCall(AddUpdateListRequest request) {
        return mApiHelper.doAddUpdateListCall(request);
    }

    @Override
    public Observable<PushNotificationSettingsResponse> doPushNotificationSettingsCall(PushNotificationSettingsRequest request) {
        return mApiHelper.doPushNotificationSettingsCall(request);
    }

    @Override
    public Observable<UpdateProximityRangeResponse> doUpdateProximityRangeCall(UpdateProximityRangeRequest request) {
        return mApiHelper.doUpdateProximityRangeCall(request);
    }

    @Override
    public Observable<RecentItemsResponse> doGetRecentItemsListCall() {
        return mApiHelper.doGetRecentItemsListCall();
    }

    @Override
    public Observable<UpdateHeaderColorResponse> doUpdateHeaderColorCall
            (UpdateHeaderColorRequest request) {
        return mApiHelper.doUpdateHeaderColorCall(request);

    }

    @Override
    public Observable<VerifyOtpResponse> doVerifyOtpCall(VerifyOtpRequest request) {
        return mApiHelper.doVerifyOtpCall(request);
    }

    @Override
    public Observable<ResetPasswordResponse> doResetPasswordCall(ResetPasswordRequest request) {
        return mApiHelper.doResetPasswordCall(request);
    }

    @Override
    public Observable<ReorderHomeListData> doReorderHomeCards
            (List<ReorderCardsHomeRequest> request) {
        return mApiHelper.doReorderHomeCards(request);
    }


    @Override
    public String getAccessToken() {
        return mPreferencesHelper.getAccessToken();
    }

    @Override
    public void setAccessToken(String accessToken) {
        mPreferencesHelper.setAccessToken(accessToken);

    }

    @Override
    public String getTokenType() {
        return mPreferencesHelper.getTokenType();
    }

    @Override
    public void setTokenType(String tokenType) {
        mPreferencesHelper.setTokenType(tokenType);
    }

    @Override
    public String getRefreshToken() {
        return mPreferencesHelper.getRefreshToken();
    }

    @Override
    public void setRefreshToken(String refreshToken) {
        mPreferencesHelper.setRefreshToken(refreshToken);
    }

    @Override
    public void setUserData(UserDetailResponse response) {
        mPreferencesHelper.setUserData(response);
    }

    @Override
    public UserDetailResponse getUserData() {
        return mPreferencesHelper.getUserData();
    }

    @Override
    public void setProximitySettings(NotificationSettingsResponse response) {
        mPreferencesHelper.setProximitySettings(response);
    }

    @Override
    public NotificationSettingsResponse getProximitySettings() {
        return mPreferencesHelper.getProximitySettings();
    }

    @Override
    public void setSelectedColor(String htmlColorCode) {
        mPreferencesHelper.setSelectedColor(htmlColorCode);
    }

    @Override
    public String getSavedColorCode() {
        return mPreferencesHelper.getSavedColorCode();
    }

    @Override
    public void saveList(MyHomeListData data) {
        mPreferencesHelper.saveList(data);
    }

    @Override
    public MyHomeListData getSavedList() {
        return mPreferencesHelper.getSavedList();
    }

    @Override
    public void saveDeviceId(String refreshedToken) {
        mPreferencesHelper.saveDeviceId(refreshedToken);
    }

    @Override
    public String getDeviceId() {
        return mPreferencesHelper.getDeviceId();
    }

    @Override
    public void sendDeviceIdToServer(boolean b) {
        mPreferencesHelper.sendDeviceIdToServer(b);
    }

    @Override
    public boolean IsDeviceIdSendToServer() {
        return mPreferencesHelper.IsDeviceIdSendToServer();
    }

    @Override
    public void saveProximityItems(String itemsJsonString) {
        mPreferencesHelper.saveProximityItems(itemsJsonString);
    }

    @Override
    public String getSavedProximityItems() {
        return mPreferencesHelper.getSavedProximityItems();
    }

    @Override
    public void setBluetoothNotificationsEnabled(boolean val) {
        mPreferencesHelper.setBluetoothNotificationsEnabled(val);

    }

    @Override
    public boolean getBluetoothNotificationEnabled() {
        return mPreferencesHelper.getBluetoothNotificationEnabled();
    }

    @Override
    public boolean getIsItemAdded() {
        return mPreferencesHelper.getIsItemAdded();
    }

    @Override
    public void setIsItemAdded(boolean isAdded) {
        mPreferencesHelper.setIsItemAdded(isAdded);
    }

    @Override
    public boolean isJustLoggedIn() {
        return mPreferencesHelper.isJustLoggedIn();
    }

    @Override
    public void setIsJustLoggedIn(boolean isTrue) {
        mPreferencesHelper.setIsJustLoggedIn(isTrue);
    }

    @Override
    public void setCurrentLocation(Location location) {
        mPreferencesHelper.setCurrentLocation(location);
    }

    @Override
    public Location getCurrentLocation() {
        return mPreferencesHelper.getCurrentLocation();
    }

    @Override
    public void setIsFacebookLogin(boolean isTrue) {
        mPreferencesHelper.setIsFacebookLogin(isTrue);
    }

    @Override
    public boolean isFacebookLogin() {
        return mPreferencesHelper.isFacebookLogin();
    }

    @Override
    public void saveBluetoothItemList(String jsonData) {
        mPreferencesHelper.saveBluetoothItemList(jsonData);
    }

    @Override
    public ArrayList<GetBluetoothItemsListResponse.BluetoothItem> getSavedBluetoothItems() {
        return mPreferencesHelper.getSavedBluetoothItems();
    }

    @Override
    public void setBluetoothDeviceConnected(boolean isConnected) {
        mPreferencesHelper.setBluetoothDeviceConnected(isConnected);
    }

    @Override
    public boolean isBluetoothDeviceConnected() {
        return mPreferencesHelper.isBluetoothDeviceConnected();
    }

    @Override
    public void setInProgressValue(boolean isInProgress) {
        mPreferencesHelper.setInProgressValue(isInProgress);
    }

    @Override
    public boolean isInProgressValue() {
        return mPreferencesHelper.isInProgressValue();
    }

    @Override
    public void saveHomeResponseData(String response) {
        mPreferencesHelper.saveHomeResponseData(response);
    }

    @Override
    public ArrayList<MyHomeListData> getHomeResponseData() {
        return mPreferencesHelper.getHomeResponseData();
    }

    @Override
    public void saveNotificationResponseData(String response) {
        mPreferencesHelper.saveNotificationResponseData(response);

    }

    @Override
    public ArrayList<NotificationListResponse.NotificationListData> getNotificationData() {
        return mPreferencesHelper.getNotificationData();
    }


    @Override
    public void saveAppSettings(String response) {

        mPreferencesHelper.saveAppSettings(response);
    }

    @Override
    public NotificationSettingsResponse getNotificationSettingResponse() {
        return mPreferencesHelper.getNotificationSettingResponse();
    }

    @Override
    public void setFirstTimeLoggedIn(boolean isFirstTime) {
        mPreferencesHelper.setFirstTimeLoggedIn(isFirstTime);
    }

    @Override
    public boolean isFirstTimeLoggedIn() {
        return mPreferencesHelper.isFirstTimeLoggedIn();
    }

    @Override
    public void settingsClickedPermission(boolean value) {
        mPreferencesHelper.settingsClickedPermission(value);
    }

    @Override
    public boolean isSettingsClickedPermission() {
        return mPreferencesHelper.isSettingsClickedPermission();
    }

    @Override
    public void setNotificationFilterPref(String duration) {
        mPreferencesHelper.setNotificationFilterPref(duration);
    }

    @Override
    public String getNotificationFilterPref() {
        return mPreferencesHelper.getNotificationFilterPref();
    }

    @Override
    public int getCurrentUserLoggedInMode() {
        return mPreferencesHelper.getCurrentUserLoggedInMode();
    }

    @Override
    public void updateUserData(UserDetailResponse data, LoggedInMode loggedInMode) {
        setCurrentUserLoggedInMode(loggedInMode);
        setUserData(data);

    }

    @Override
    public void updateUserData(UserDetailResponse data) {

    }

    @Override
    public void updateUserCredential(String accessToken, String refreshToken, String tokenType) {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setTokenType(tokenType);
        updateApiHeader(accessToken, refreshToken, tokenType);
    }

    private void updateApiHeader(String accessToken, String refreshToken, String tokenType) {
        mApiHelper.getApiHeader().setmAccessToken(accessToken);
        mApiHelper.getApiHeader().setmRefreshToken(refreshToken);
        mApiHelper.getApiHeader().setmTokenType(tokenType);
    }

    @Override
    public void updateNewCredential(String accessToken, String refreshToken, String tokenType) {
        setAccessToken(accessToken);
        setRefreshToken(refreshToken);
        setTokenType(tokenType);
    }

    @Override
    public String getCountryListFromAsset() {
        try {
            return CommonUtils.loadJSONFromAsset(mContext, "countries.json");
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    @Override
    public void setCurrentUserLoggedInMode(LoggedInMode loggedInMode) {
        mPreferencesHelper.setCurrentUserLoggedInMode(loggedInMode);
    }

    @Override
    public void setUserAsLoggedOut() {
        saveProximityItems("[]");
        setIsItemAdded(false);
        updateUserCredential(null, null, null);
        updateUserData(null, DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT);
        sendDeviceIdToServer(false);
        setIsJustLoggedIn(true);
        setIsFacebookLogin(false);
        mPreferencesHelper.saveDeviceId(null);
        mPreferencesHelper.saveNotificationResponseData("[]");
        mPreferencesHelper.saveHomeResponseData("[]");
        mPreferencesHelper.saveAppSettings(null);
        mPreferencesHelper.setProximitySettings(null);

        Log.e("logged out","yes");

    }

    @Override
    public Observable<AddUpdateItemResponse> doAddUpdateItemsCall(RequestBody request) {
        return mApiHelper.doAddUpdateItemsCall(request);
    }

    @Override
    public Observable<GetUserSettingResponse> doGetUserSettingsCall(int listId) {
        return mApiHelper.doGetUserSettingsCall(listId);
    }

    @Override
    public Observable<AddItemsLocationResponse> doGetRecentItemsLocationListCall() {
        return mApiHelper.doGetRecentItemsLocationListCall();
    }

    @Override
    public Observable<RecentLocationAddItemsResponse> doGetGoogleLocationCall(String query, String loc, int radius, String Key) {
        return mApiHelper.doGetGoogleLocationCall(query, loc, radius, Key);
    }

    @Override
    public Observable<RecentLocationAddItemsResponse> doGetNearbySearchLocationCall(String loc, int radius, String Key, String type) {
        return mApiHelper.doGetNearbySearchLocationCall(loc, radius, Key, type);
    }

    @Override
    public Observable<ShareListResponse> doShareListCall(ShareListRequest request) {
        return mApiHelper.doShareListCall(request);
    }

    @Override
    public Observable<DeleteItemListResponse> doDeleteItemListCall(DeleteItemListRequest deleteItemListRequest) {
        return mApiHelper.doDeleteItemListCall(deleteItemListRequest);
    }

    @Override
    public Observable<ChangeItemStatusResponse> dochangeItemStatusListCall(ChangeItemStatusRequest changeItemStatusRequest) {
        return mApiHelper.dochangeItemStatusListCall(changeItemStatusRequest);
    }

    @Override
    public Observable<DeleteUserResponse> deleteUserFromSharedListCall(HashMap<String, String> request) {
        return mApiHelper.deleteUserFromSharedListCall(request);
    }

    @Override
    public Observable<DeleteUserResponse> updateNotificationSettingByOwnerCall(UpdateNotificationSettingRequest request) {
        return mApiHelper.updateNotificationSettingByOwnerCall(request);
    }

    @Override
    public Observable<DeleteRecentItemsResponse> doDeleteRecentItemsCall() {
        return mApiHelper.doDeleteRecentItemsCall();
    }

    @Override
    public Observable<DeleteRecentItemsResponse> doDeleteRecentLocations() {
        return mApiHelper.doDeleteRecentLocations();
    }

    @Override
    public Observable<DeleteUserResponse> allowAutoDeleteOnItemComplete(AllowAutoDeleteRequest request) {
        return mApiHelper.allowAutoDeleteOnItemComplete(request);
    }

    @Override
    public Observable<DeleteUserResponse> changePushStatusForOthersList(HashMap<String, Integer> status) {
        return mApiHelper.changePushStatusForOthersList(status);
    }

    public Observable<ReorderHomeListData> doReorderItems(List<ReorderItemsMyList> request) {
        return mApiHelper.doReorderItems(request);
    }

    @Override
    public Observable<UpdateDeviceInfoResponse> doUpdateDeviceInfo(UpdateDeviceInfoRequest updateDeviceInfoRequest) {
        return mApiHelper.doUpdateDeviceInfo(updateDeviceInfoRequest);
    }

    @Override
    public Observable<GetProximityResponse> doGetProximityResponseCall() {
        return mApiHelper.doGetProximityResponseCall();
    }

    @Override
    public Observable<BluetoothResponse> doGetBluetoothResponse(BluetoothRequest request) {
        return mApiHelper.doGetBluetoothResponse(request);
    }

    @Override
    public Observable<HomeCardDeleteResponse> doGetUnshareList(UnshareListRequest request) {
        return mApiHelper.doGetUnshareList(request);
    }

    @Override
    public Observable<GetListSettingsResponse> doGetListSettings(int listId) {
        return mApiHelper.doGetListSettings(listId);
    }

    @Override
    public Observable<Void> doUpdateListSettings(UpdateListSettingsRequest request) {
        return mApiHelper.doUpdateListSettings(request);
    }

    @Override
    public Observable<GetBluetoothItemsListResponse> doGetAllBluetoothItems() {
        return mApiHelper.doGetAllBluetoothItems();
    }

    @Override
    public Observable<UpdateUserEmailResponse> doUpdateUserEmail(UpdateEmailRequest request) {
        return mApiHelper.doUpdateUserEmail(request);
    }

    @Override
    public Observable<DailyReminderExpiringResponse> doSaveDailyReminderExpiring(DailyReminderExpiringRequest request) {
        return mApiHelper.doSaveDailyReminderExpiring(request);
    }

    @Override
    public Observable<DailyReminderExpiredResponse> doSaveDailyReminderExpired(DailyReminderExpiringRequest request) {
        return mApiHelper.doSaveDailyReminderExpired(request);
    }

    @Override
    public Observable<ReminderTimeResponse> doSaveReminderTime(ReminderTimeRequest request) {
        return mApiHelper.doSaveReminderTime(request);
    }

    @Override
    public Observable<NotificationListResponse> doGetNotificationList(int pageIndex, int pageSize) {
        return mApiHelper.doGetNotificationList(pageIndex, pageSize);
    }

    @Override
    public Observable<NotificationReadResponse> doUpdateNotificationRead(int notificationId) {
        return mApiHelper.doUpdateNotificationRead(notificationId);
    }

    @Override
    public Observable<NotificationCountResponse> doGetNotificationCount() {
        return mApiHelper.doGetNotificationCount();
    }

    @Override
    public Observable<ResetNotificationCountResponse> doResetNotificationCount() {
        return mApiHelper.doResetNotificationCount();
    }

    @Override
    public Observable<AddUpdateItemResponse> doUpdateRecentItemsCall(RequestBody ItemId, RequestBody ItemName,
                                                                     RequestBody ItemTime,
                                                                     RequestBody Latitude, RequestBody ListId,
                                                                     RequestBody ListName, RequestBody Location, RequestBody Longitude, RequestBody StatusId, MultipartBody.Part image) {
        return mApiHelper.doUpdateRecentItemsCall(ItemId, ItemName, ItemTime, Latitude, ListId, ListName, Location, Longitude, StatusId, image);
    }

    @Override
    public Observable<EmailUpdateNewResponse> doUpdateEmailNew(EmailUpdateNewRequest request) {
        return mApiHelper.doUpdateEmailNew(request);
    }

    @Override
    public Observable<EmailUpdateNewResponse> doVerifyEmailNewOtp(VerifyNewEmailOTPRequest request) {
        return mApiHelper.doVerifyEmailNewOtp(request);
    }

    @Override
    public Observable<EmailUpdateNewResponse> resendOTP(EmailUpdateNewRequest request) {
        return mApiHelper.resendOTP(request);
    }

    @Override
    public Observable<SystemNotifcationPrefData> doGetSystemNotificationPrefData() {
        return mApiHelper.doGetSystemNotificationPrefData();
    }

    @Override
    public Observable<SystemNotifcationPrefData> doSetSystemNotificationPref(SetNotificationPrefRequest request) {
        return mApiHelper.doSetSystemNotificationPref(request);
    }

    @Override
    public Observable<DeleteItemPhotoResponse> deleteItemPhoto(DeleteItemPhotoRequest response) {
        return mApiHelper.deleteItemPhoto(response);
    }

    @Override
    public void insertListData(int listId, String response) {
        mDbHelper.insertListData(listId, response);
    }

    @Override
    public void insertShareListData(int listId, String response) {
        mDbHelper.insertShareListData(listId,response);
    }

    @Override
    public ArrayList<MyListResponseData> getListData(int listId) {
        return mDbHelper.getListData(listId);
    }

    @Override
    public ArrayList<GetUserSettingResponse.Result> getShareListData(int listId) {
        return mDbHelper.getShareListData(listId);
    }

    @Override
    public void deleteAllRecords() {
        mDbHelper.deleteAllRecords();
    }


    /*@Override
    public List<GetProximityResponse.Result> getProximityItemList() {
        String json = mPreferencesHelper.getSavedProximityItems();
        GetProximityResponse.Result array[] = new Gson().fromJson(json, GetProximityResponse.Result[].class);
        List<GetProximityResponse.Result> itemList;
        if (array != null && array.length > 0) {
            itemList = new ArrayList<>(Arrays.asList(array));
            return itemList;
        }

        return null;
        //return mDbHelper.getProximityItemList();
    }*/
}

