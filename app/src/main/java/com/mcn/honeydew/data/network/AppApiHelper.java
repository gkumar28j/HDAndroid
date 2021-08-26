package com.mcn.honeydew.data.network;

import com.mcn.honeydew.data.network.model.HomeCardDeleteResponse;
import com.mcn.honeydew.data.network.model.HomeDetailListResponse;
import com.mcn.honeydew.data.network.model.HomeListCardDeleteRequest;
import com.mcn.honeydew.data.network.model.HomeResponse;
import com.mcn.honeydew.data.network.model.LoginResponse;
import com.mcn.honeydew.data.network.model.LogoutResponse;
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
import com.mcn.honeydew.data.network.model.response.DeleteRecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.DeleteUserResponse;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.data.network.model.response.FacebookLoginResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
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
public class AppApiHelper implements ApiHelper {

    private ApiHeader mApiHeader;
    private ApiCall mApiCall;

    @Inject
    public AppApiHelper(ApiHeader apiHeader, ApiCall apiCall) {
        mApiHeader = apiHeader;
        mApiCall = apiCall;
    }

    @Override
    public ApiHeader getApiHeader() {
        return mApiHeader;
    }

    @Override
    public void setApiHeader(ApiHeader apiHeader) {
        if (apiHeader != null) {
            mApiHeader.setmApiKey(apiHeader.getmApiKey());
            mApiHeader.setmAccessToken(apiHeader.getmAccessToken());
            mApiHeader.setmRefreshToken(apiHeader.getmRefreshToken());
            mApiHeader.setmTokenType(apiHeader.getmTokenType());
        }
    }

    @Override
    public Observable<LoginResponse> doServerLoginApiCall(String username, String grantType, String password) {
        return mApiCall.doServerLogin(username, grantType, password);
    }

    @Override
    public Observable<RegistrationResponse> doRegisterApiCall(RegistrationRequest request) {
        return mApiCall.doRegister(request);
    }

    @Override
    public Observable<HomeResponse> doHomeApiCall() {
        return mApiCall.getHomeList();
    }

    @Override
    public Observable<UserDetailResponse> doUserDetailsApiCall() {
        return mApiCall.getUserDetails();
    }

    @Override
    public Observable<PhoneVerificationResponse> doPhoneVerificationApiCall(PhoneVerificationRequest request) {
        return mApiCall.doPhoneVerification(request);
    }

    @Override
    public Observable<PhoneVerificationResponse> doUpdateVerificationApiCall(UpdateVerificationStatusRequest request) {
        return mApiCall.doUpdatePhoneVerification(request);
    }

    @Override

    public Observable<HomeCardDeleteResponse> doDeleteCards(HomeListCardDeleteRequest request) {
        return mApiCall.doDeleteCards(request);
    }

    public Observable<LogoutResponse> doLogoutApiCall() {
        return mApiCall.doLogoutApiCall();
    }

    @Override
    public Observable<FacebookLoginResponse> doLoginWithFacebookApiCall(FacebookLoginRequest request) {
        return mApiCall.doLoginWithFacebook(request);
    }

    @Override
    public Observable<LocateAccountResponse> doLocateAccountApiCall(String emailOrPhone) {
        return mApiCall.doLocateAccount(emailOrPhone);
    }

    @Override
    public Observable<SendOtpResponse> doSendOtpApiCall(SendOtpRequest request) {
        return mApiCall.doSendOtp(request);
    }

    @Override
    public Observable<HomeDetailListResponse> doGetHomeDetailList(String Id) {
        return mApiCall.getHomeDetailsList(Id);
    }

    @Override
    public Observable<HomeDetailListResponse> doGetHomeDetailListCall(String Id) {
        return mApiCall.getHomeDetailsList(Id);
    }


    @Override
    public Observable<ReorderHomeListData> doReorderHomeCards(List<ReorderCardsHomeRequest> request) {
        return mApiCall.doReorderHomeCards(request);
    }

    @Override
    public Observable<UpdateUserResponse> doUpdateUserName(UpdateUserNameRequest request) {
        return mApiCall.doUpdateUserName(request);
    }

    @Override
    public Observable<NotificationSettingsResponse> doNotificationSettingsCall() {
        return mApiCall.doNotificationSettingsCall();
    }

    @Override
    public Observable<ChangePasswordResponse> doChangePasswordCall(ChangePasswordRequest request) {
        return mApiCall.doChangePassword(request);
    }

    @Override
    public Observable<AddUpdateListResponse> doAddUpdateListCall(AddUpdateListRequest request) {
        return mApiCall.doAddUpdateListCall(request);
    }

    @Override
    public Observable<PushNotificationSettingsResponse> doPushNotificationSettingsCall(PushNotificationSettingsRequest request) {
        return mApiCall.doPushNotificationSettingsCall(request);
    }

    @Override
    public Observable<UpdateProximityRangeResponse> doUpdateProximityRangeCall(UpdateProximityRangeRequest request) {
        return mApiCall.doUpdateProximityRangeCall(request);
    }

    @Override
    public Observable<UpdateHeaderColorResponse> doUpdateHeaderColorCall(UpdateHeaderColorRequest request) {
        return mApiCall.doUpdateHeaderColorCall(request);
    }

    @Override
    public Observable<VerifyOtpResponse> doVerifyOtpCall(VerifyOtpRequest request) {
        return mApiCall.doVerifyOtp(request);
    }

    @Override
    public Observable<ResetPasswordResponse> doResetPasswordCall(ResetPasswordRequest request) {
        return mApiCall.doResetPassword(request);
    }

    @Override
    public Observable<RecentItemsResponse> doGetRecentItemsListCall() {
        return mApiCall.doGetRecentItemsListCall();
    }

    @Override
    public Observable<AddUpdateItemResponse> doAddUpdateItemsCall(RequestBody request) {
        return mApiCall.doAddUpdateItemsCall(request);
    }

    @Override
    public Observable<GetUserSettingResponse> doGetUserSettingsCall(int listId) {
        return mApiCall.doGetUserSettingsCall(listId);
    }

    @Override
    public Observable<AddItemsLocationResponse> doGetRecentItemsLocationListCall() {
        return mApiCall.doGetRecentItemsLocationListCall();
    }

    @Override
    public Observable<RecentLocationAddItemsResponse> doGetGoogleLocationCall(String query, String loc, int radius, String key) {
        return mApiCall.doGetGoogleLocationCall(query, loc, radius, key);
    }

    @Override
    public Observable<RecentLocationAddItemsResponse> doGetNearbySearchLocationCall(String loc, int radius, String Key, String type) {
        return mApiCall.doGetNearbySearchLocationCall(loc, radius, Key, type);
    }

    @Override
    public Observable<ShareListResponse> doShareListCall(ShareListRequest request) {
        return mApiCall.doShareListToContactsCall(request);
    }

    @Override
    public Observable<DeleteItemListResponse> doDeleteItemListCall(DeleteItemListRequest deleteItemListRequest) {
        return mApiCall.doDeleteItemListCall(deleteItemListRequest);
    }

    @Override
    public Observable<ChangeItemStatusResponse> dochangeItemStatusListCall(ChangeItemStatusRequest changeItemStatusRequest) {
        return mApiCall.dochangeItemStatusListCall(changeItemStatusRequest);
    }

    @Override
    public Observable<DeleteUserResponse> deleteUserFromSharedListCall(HashMap<String, String> request) {
        return mApiCall.deleteUserFromSharedListCall(request);
    }

    @Override
    public Observable<DeleteUserResponse> updateNotificationSettingByOwnerCall(UpdateNotificationSettingRequest request) {
        return mApiCall.updateNotificationSettingByOwner(request);
    }

    @Override
    public Observable<DeleteRecentItemsResponse> doDeleteRecentItemsCall() {
        return mApiCall.doDeleteRecentItemsCall();
    }

    @Override
    public Observable<DeleteRecentItemsResponse> doDeleteRecentLocations() {
        return mApiCall.doDeleteRecentLocationsCall();
    }

    @Override
    public Observable<DeleteUserResponse> changePushStatusForOthersList(HashMap<String, Integer> status) {
        return mApiCall.changePushStatusForOthersList(status);
    }

    @Override
    public Observable<DeleteUserResponse> allowAutoDeleteOnItemComplete(AllowAutoDeleteRequest request) {
        return mApiCall.allowAutoDeletionOnComplete(request);
    }

    @Override
    public Observable<ReorderHomeListData> doReorderItems(List<ReorderItemsMyList> request) {
        return mApiCall.doReorderItems(request);
    }

    @Override
    public Observable<UpdateDeviceInfoResponse> doUpdateDeviceInfo(UpdateDeviceInfoRequest updateDeviceInfoRequest) {
        return mApiCall.doUpdateDeviceInfo(updateDeviceInfoRequest);
    }

    @Override
    public Observable<GetProximityResponse> doGetProximityResponseCall() {
        return mApiCall.doGetProximityResponseCall();
    }

    @Override
    public Observable<BluetoothResponse> doGetBluetoothResponse(BluetoothRequest request) {
        return mApiCall.doGetBluetoothResponse(request);
    }

    @Override
    public Observable<HomeCardDeleteResponse> doGetUnshareList(UnshareListRequest request) {
        return mApiCall.doGetUnshareList(request);
    }

    @Override
    public Observable<GetListSettingsResponse> doGetListSettings(int listId) {
        return mApiCall.doGetListSettings(listId);
    }

    @Override
    public Observable<Void> doUpdateListSettings(UpdateListSettingsRequest request) {
        return mApiCall.doUpdateListSettings(request);
    }

    @Override
    public Observable<GetBluetoothItemsListResponse> doGetAllBluetoothItems() {
        return mApiCall.doGetAllBluetoothItems();
    }

    @Override
    public Observable<UpdateUserEmailResponse> doUpdateUserEmail(UpdateEmailRequest request) {
        return mApiCall.doUpdateUserEmail(request);
    }

    @Override
    public Observable<DailyReminderExpiringResponse> doSaveDailyReminderExpiring(DailyReminderExpiringRequest request) {
        return mApiCall.doSaveDailyReminderExpiring(request);
    }

    @Override
    public Observable<DailyReminderExpiredResponse> doSaveDailyReminderExpired(DailyReminderExpiringRequest request) {
        return mApiCall.doSaveDailyReminderExpired(request);
    }

    @Override
    public Observable<ReminderTimeResponse> doSaveReminderTime(ReminderTimeRequest request) {
        return mApiCall.doSaveReminderTime(request);
    }

    @Override
    public Observable<NotificationListResponse> doGetNotificationList(int pageIndex, int pageSize) {
        return mApiCall.doGetNotificationList(pageIndex, pageSize);
    }

    @Override
    public Observable<NotificationReadResponse> doUpdateNotificationRead(int notificationId) {
        return mApiCall.doUpdateNotificationRead(notificationId);
    }

    @Override
    public Observable<NotificationCountResponse> doGetNotificationCount() {
        return mApiCall.doGetNotificationCount();
    }

    @Override
    public Observable<ResetNotificationCountResponse> doResetNotificationCount() {
        return mApiCall.doResetNotificationCount();
    }

    @Override
    public Observable<AddUpdateItemResponse> doUpdateRecentItemsCall(RequestBody ItemId,
                                                                     RequestBody ItemName,
                                                                     RequestBody ItemTime,
                                                                     RequestBody Latitude,
                                                                     RequestBody ListId,
                                                                     RequestBody ListName,
                                                                     RequestBody Location,
                                                                     RequestBody Longitude,
                                                                     RequestBody StatusId,
                                                                     MultipartBody.Part image) {
        return mApiCall.doUpdateRecentItemsCall(ItemId,
                ItemName,
                ItemTime,
                Latitude,
                ListId,
                ListName,
                Location,
                Longitude,
                StatusId,
                image);
    }

    @Override
    public Observable<EmailUpdateNewResponse> doUpdateEmailNew(EmailUpdateNewRequest request) {
        return mApiCall.doUpdateEmailNew(request);
    }

    @Override
    public Observable<EmailUpdateNewResponse> doVerifyEmailNewOtp(VerifyNewEmailOTPRequest request) {
        return mApiCall.doVerifyEmailNewOtp(request);
    }

    @Override
    public Observable<EmailUpdateNewResponse> resendOTP(EmailUpdateNewRequest request) {
        return mApiCall.resendOTP(request);
    }

    @Override
    public Observable<SystemNotifcationPrefData> doGetSystemNotificationPrefData() {
        return mApiCall.doGetSystemNotificationPrefData();
    }

    @Override
    public Observable<SystemNotifcationPrefData> doSetSystemNotificationPref(SetNotificationPrefRequest request) {
        return mApiCall.doSetSystemNotificationPref(request);
    }


}

