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
import com.mcn.honeydew.data.network.model.request.AddUpdateItemRequest;
import com.mcn.honeydew.data.network.model.request.AddUpdateListRequest;
import com.mcn.honeydew.data.network.model.request.AllowAutoDeleteRequest;
import com.mcn.honeydew.data.network.model.request.BluetoothRequest;
import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.request.ChangePasswordRequest;
import com.mcn.honeydew.data.network.model.request.DailyReminderExpiringRequest;
import com.mcn.honeydew.data.network.model.request.DeleteItemListRequest;
import com.mcn.honeydew.data.network.model.request.FacebookLoginRequest;
import com.mcn.honeydew.data.network.model.request.PushNotificationSettingsRequest;
import com.mcn.honeydew.data.network.model.request.ReminderTimeRequest;
import com.mcn.honeydew.data.network.model.request.ReorderItemsMyList;
import com.mcn.honeydew.data.network.model.request.ResetPasswordRequest;
import com.mcn.honeydew.data.network.model.request.SendOtpRequest;
import com.mcn.honeydew.data.network.model.request.ShareListRequest;
import com.mcn.honeydew.data.network.model.request.UnshareListRequest;
import com.mcn.honeydew.data.network.model.request.UpdateDeviceInfoRequest;
import com.mcn.honeydew.data.network.model.request.UpdateEmailRequest;
import com.mcn.honeydew.data.network.model.request.UpdateHeaderColorRequest;
import com.mcn.honeydew.data.network.model.request.UpdateListSettingsRequest;
import com.mcn.honeydew.data.network.model.request.UpdateProximityRangeRequest;
import com.mcn.honeydew.data.network.model.request.UpdateUserNameRequest;
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
import com.mcn.honeydew.data.network.model.response.FacebookLoginResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationReadResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.PushNotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.data.network.model.response.ReminderTimeResponse;
import com.mcn.honeydew.data.network.model.response.ResetPasswordResponse;
import com.mcn.honeydew.data.network.model.response.SendOtpResponse;
import com.mcn.honeydew.data.network.model.response.ShareListResponse;
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.data.network.model.response.UpdateHeaderColorResponse;
import com.mcn.honeydew.data.network.model.response.UpdateNotificationSettingRequest;
import com.mcn.honeydew.data.network.model.response.UpdateProximityRangeResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserEmailResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserResponse;
import com.mcn.honeydew.data.network.model.response.VerifyOtpResponse;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Query;

/**
 * Created by amit on 14/2/18.
 */

public interface ApiHelper {

    ApiHeader getApiHeader();

    void setApiHeader(ApiHeader apiHeader);

    Observable<LoginResponse> doServerLoginApiCall(String username, String grantType, String password);

    Observable<RegistrationResponse> doRegisterApiCall(RegistrationRequest request);

    /**
     * for the home list main
     */
    Observable<HomeResponse> doHomeApiCall();

    Observable<UserDetailResponse> doUserDetailsApiCall();

    Observable<PhoneVerificationResponse> doPhoneVerificationApiCall(PhoneVerificationRequest request);

    Observable<PhoneVerificationResponse> doUpdateVerificationApiCall(UpdateVerificationStatusRequest request);


    Observable<HomeCardDeleteResponse> doDeleteCards(HomeListCardDeleteRequest request);

    Observable<ReorderHomeListData> doReorderHomeCards(List<ReorderCardsHomeRequest> request);

    Observable<LogoutResponse> doLogoutApiCall();

    Observable<FacebookLoginResponse> doLoginWithFacebookApiCall(FacebookLoginRequest request);

    Observable<LocateAccountResponse> doLocateAccountApiCall(String emailOrPhone);

    Observable<SendOtpResponse> doSendOtpApiCall(SendOtpRequest request);

    Observable<HomeDetailListResponse> doGetHomeDetailList(String Id);

    Observable<HomeDetailListResponse> doGetHomeDetailListCall(String Id);


    Observable<UpdateUserResponse> doUpdateUserName(UpdateUserNameRequest request);

    Observable<VerifyOtpResponse> doVerifyOtpCall(VerifyOtpRequest request);

    Observable<ResetPasswordResponse> doResetPasswordCall(ResetPasswordRequest request);

    Observable<NotificationSettingsResponse> doNotificationSettingsCall();

    Observable<ChangePasswordResponse> doChangePasswordCall(ChangePasswordRequest request);

    Observable<AddUpdateListResponse> doAddUpdateListCall(AddUpdateListRequest request);

    Observable<PushNotificationSettingsResponse> doPushNotificationSettingsCall(PushNotificationSettingsRequest request);

    Observable<UpdateProximityRangeResponse> doUpdateProximityRangeCall(UpdateProximityRangeRequest request);


    Observable<RecentItemsResponse> doGetRecentItemsListCall();

    Observable<UpdateHeaderColorResponse> doUpdateHeaderColorCall(UpdateHeaderColorRequest request);

    Observable<AddUpdateItemResponse> doAddUpdateItemsCall(AddUpdateItemRequest request);

    Observable<GetUserSettingResponse> doGetUserSettingsCall(int listId);

    Observable<AddItemsLocationResponse> doGetRecentItemsLocationListCall();

    Observable<RecentLocationAddItemsResponse> doGetGoogleLocationCall(String query, String loc, int radius, String Key);

    Observable<RecentLocationAddItemsResponse> doGetNearbySearchLocationCall(String loc, int radius, String Key, String type);

    Observable<DeleteRecentItemsResponse> doDeleteRecentItemsCall();

    Observable<DeleteRecentItemsResponse> doDeleteRecentLocations();

    Observable<ShareListResponse> doShareListCall(ShareListRequest request);

    Observable<DeleteItemListResponse> doDeleteItemListCall(DeleteItemListRequest deleteItemListRequest);

    Observable<ChangeItemStatusResponse> dochangeItemStatusListCall(ChangeItemStatusRequest changeItemStatusRequest);

    Observable<DeleteUserResponse> deleteUserFromSharedListCall(HashMap<String, String> request);

    Observable<DeleteUserResponse> updateNotificationSettingByOwnerCall(UpdateNotificationSettingRequest request);

    Observable<DeleteUserResponse> allowAutoDeleteOnItemComplete(AllowAutoDeleteRequest request);

    Observable<DeleteUserResponse> changePushStatusForOthersList(HashMap<String, Integer> status);

    Observable<ReorderHomeListData> doReorderItems(List<ReorderItemsMyList> request);

    Observable<UpdateDeviceInfoResponse> doUpdateDeviceInfo(UpdateDeviceInfoRequest updateDeviceInfoRequest);

    Observable<GetProximityResponse> doGetProximityResponseCall();

    Observable<BluetoothResponse> doGetBluetoothResponse(BluetoothRequest request);

    Observable<HomeCardDeleteResponse> doGetUnshareList(UnshareListRequest request);

    Observable<GetListSettingsResponse> doGetListSettings(int listId);

    Observable<Void> doUpdateListSettings(UpdateListSettingsRequest request);

    Observable<GetBluetoothItemsListResponse> doGetAllBluetoothItems();

    Observable<UpdateUserEmailResponse> doUpdateUserEmail(UpdateEmailRequest request);

    Observable<DailyReminderExpiringResponse> doSaveDailyReminderExpiring(DailyReminderExpiringRequest request);

    Observable<DailyReminderExpiredResponse> doSaveDailyReminderExpired(DailyReminderExpiringRequest request);

    Observable<ReminderTimeResponse> doSaveReminderTime(ReminderTimeRequest request);

    Observable<NotificationListResponse> doGetNotificationList(int pageIndex, int pageSize);

    Observable<NotificationReadResponse> doUpdateNotificationRead(@Query("NotificationId") int notificationId);

}
