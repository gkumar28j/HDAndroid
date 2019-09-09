package com.mcn.honeydew.data.network;


import com.mcn.honeydew.BuildConfig;
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
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.data.network.model.response.UpdateHeaderColorResponse;
import com.mcn.honeydew.data.network.model.response.UpdateNotificationSettingRequest;
import com.mcn.honeydew.data.network.model.response.UpdateProximityRangeResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserEmailResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserResponse;
import com.mcn.honeydew.data.network.model.response.VerifyOtpResponse;
import com.mcn.honeydew.utils.AppConstants;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by amit on 15/2/18.
 */

public interface ApiCall {

    String HEADER_PARAM_SEPARATOR = ":";
    String API_VERSION = "v1_2";

    String ENDPOINT_SERVER_LOGIN = "token";
    String ENDPOINT_USER_DETAIL = API_VERSION + "/api/Account/UserDetail";
    String ENDPOINT_REGISTER_USER = API_VERSION + "/api/Account/RegisterUser";
    String ENDPOINT_HOME_DATA_SHARED_LIST = API_VERSION + "/api/item/GetMyListAndSharedList";
    String ENDPOINT_PHONE_VERIFICATION = API_VERSION + "/api/Phone/Call";
    String ENDPOINT_UPDATE_VERIFICATION_METHOD = API_VERSION + "/api/Phone/UpdateVerificationStatus";
    String ENDPOINT_ACCOUNT_LOGOUT = API_VERSION + "/api/Account/LogoutUser";
    String ENDPOINT_FACEBOOK_LOGIN = API_VERSION + "/api/SocialSite/LoginFacebook";
    String ENDPOINT_DELETE_SHARED_LIST_HOME = API_VERSION + "/api/item/DeleteList";
    String ENDPOINT_UPDATE_USER_NAME = API_VERSION + "/api/Account/UpdateUserRegistration";
    String ENDPOINT_MAINTAIN_ORDER_SHARED_LIST_HOME = API_VERSION + "/api/item/ManageListOrder";
    String ENDPOINT_LOCATE_ACCOUNT = API_VERSION + "/api/account/ValidUser";
    String ENDPOINT_SEND_OTP_FOR_RESET_PASSWORD = API_VERSION + "/api/account/SendOTPForResetPassword";
    String ENDPOINT_VERIFY_OTP = API_VERSION + "/api/account/VerifyOTPCode";
    String ENDPOINT_HOME_DETAIL_LIST = API_VERSION + "/api/item/GetUserList";
    String ENDPOINT_NOTIFICATION_SETTINGS = API_VERSION + "/api/Account/GetNotificationSettings";
    String ENDPOINT_CHANGE_PASSWORD = API_VERSION + "/api/Account/ChangeUserPassword";
    String ENDPOINT_RESET_PASSWORD = API_VERSION + "/api/account/ResetPassword";
    String ENDPOINT_ADD_UPDATE_LIST_CALL_URL = API_VERSION + "/api/item/AddUpdateList";
    String ENDPOINT_PUSHNOTIFICATION_ENABLE_DISABLE_URL = API_VERSION + "/api/Account/PushNotificationEnableDisable";
    String ENDPOINT_UPDATE_PROXIMITY_RANGE_URL = API_VERSION + "/api/Account/UpdateProximityRange";
    String ENDPOINT_ADD_UPDATE_LIST_COLOR_URL = API_VERSION + "/api/item/UpdateHeaderColor";

    String ENDPOINT_RECENT_ITEMS_LIST_CALL_URL = API_VERSION + "/api/item/recentitems";

    String ENDPOINT_RECENT_LOCATION_ITEMS_LIST_CALL_URL = API_VERSION + "/api/item/recentlocations?location=";

    String ENDPOINT_ADD_UPDATE_ITEMS_LIST_CALL_URL = API_VERSION + "/api/item/AddUpdateItem";

    String ENDPOINT_GET_USER_SETTINGS_URL = API_VERSION + "/api/account/GetUserSettings";


    String ENDPOINT_GET_LAT_LNG_FRM_ADDRESS_CALL_URL = "https://maps.googleapis.com/maps/api/place/textsearch/json";

    String ENDPOINT_GET_NEARBY_SEARCH_ADDRESS_CALL_URL = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";

    String ENDPOINT_SHARE_LIST_CONTACT_URL = API_VERSION + "/api/item/ShareListToUser";

    String ENDPOINT_GET_DELETE_RECENT_ITEMS_CALL = API_VERSION + "/api/item/DeleteRecentItems";

    String ENDPOINT_GET_DELETE_RECENT_LOCATIONS_CALL = API_VERSION + "/api/item/DeleteRecentLocations";

    String ENDPOINT_DELETE_ITEM_LIST_CALL = API_VERSION + "/api/item/DeleteItem";

    String ENDPOINT_CHANGE_ITEM_STATUS_LIST_CALL = API_VERSION + "/api/item/ChangeItemStatus";

    String ENDPOINT_DELETE_USER_FROM_SHARED_LIST_CALL = API_VERSION + "/api/Account/DeleteUserFromShareList";

    String ENDPOINT_UPDATE_NOTIFICATION_SETTING_BY_OWNER_CALL = API_VERSION + "/api/Account/UpdateNotificationSettingByOwner";

    String ENDPOINT_ALLOW_DELETION_AFTER_COMPLETE_CALL = API_VERSION + "/api/account/SetItemDeleteOnCompletion";

    String ENDPOINT_CHANGE_PUSH_STATUS_FOR_OTHERS_LIST_CALL = API_VERSION + "/api/Account/PushNotificationEnableDisable";

    String ENDPOINT_REORDERING_ITEMS_CALL = API_VERSION + "/api/item/ManageItemOrder";

    String ENDPOINT_UPDATE_DEVICE_INFO_CALL = API_VERSION + "/api/account/UpdateDeviceInfo";

    String ENDPOINT_GET_PROXIMITY_ITEM_CALL = API_VERSION + "/api/item/ProximiyItem";

    String ENDPOIT_SEND_BLUETOOTH_PUSH_API_CALL = API_VERSION + "/api/Account/BluetoothConnectDisconnet";

    String ENDPOIT_ENABLE_DIABLE_BLUETOOTH_PUSH_API_CALL = API_VERSION + "/api/Account/PushNotificationEnableDisable";

    String ENDPOINT_UNSHARE_LIST_HOME = API_VERSION + "/api/Account/UnShareListFromDoer";

    String ENDPOINT_GET_LIST_SETTINGS = API_VERSION + "/api/item/GetListSettings";

    String ENDPOINT_SAVE_LIST_SETTINGS = API_VERSION + "/api/item/ListSettings";

    String ENDPOINT_GET_ALL_BLUETOOTH_ITEMS = API_VERSION + "/api/Notification/GetAllPushNotification";

    //  String ENDPOINT_GET_LIST_SETTINGS = API_VERSION + "/api/item/GetListSettings";


    String ENDPOINT_UPDATE_EMAIL = API_VERSION + "/api/Account/EmailUpdate";

    // Daily Reminder Expiring

    String ENDPOINT_DAILY_EXPIRING_ITEMS = API_VERSION + "/api/Account/BluetoothReminderForExpiring";

    String ENDPOINT_DAILY_EXPIRED_ITEMS = API_VERSION + "/api/Account/BluetoothReminderForExpired";

    String ENDPOINT_REMINDER_TIME = API_VERSION + "/api/Account/BluetoothReminderTime";

    String ENDPOINT_NOTIFICATION_LIST_CALL = API_VERSION + "/api/Notification/GetAllSystemNotification";

    String ENDPOINT_NOTIFICATION_LIST_READ_UPDATE_CALL = API_VERSION + "/api/Notification/ReadNotification";

    String ENDPOINT_GET_NOTIFICATION_COUNT = API_VERSION + "/api/Notification/GetSystemNotificationCount";

    String ENDPOINT_RESET_NOTIFICATION_COUNT = API_VERSION + "/api/Notification/ResetSystemNotificationCount";

    String ENDPOINT_ADD_UPDATE_ITEMS_NEW_LIST_CALL_URL = API_VERSION + "/api/item/AddUpdateItemnew";

    @POST(ENDPOINT_SERVER_LOGIN)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    @FormUrlEncoded
    Observable<LoginResponse> doServerLogin(@Field("username") String username,
                                            @Field("grant_type") String grantType,
                                            @Field("password") String password);

    // shared cards in home fragment
    @GET(ENDPOINT_HOME_DATA_SHARED_LIST)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<HomeResponse> getHomeList();


    @POST(ENDPOINT_REGISTER_USER)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<RegistrationResponse> doRegister(@Body RegistrationRequest request);

    // Phone Verification call
    @POST(ENDPOINT_PHONE_VERIFICATION)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<PhoneVerificationResponse> doPhoneVerification(@Body PhoneVerificationRequest request);

    // Update Phone Verification call
    @POST(ENDPOINT_UPDATE_VERIFICATION_METHOD)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<PhoneVerificationResponse> doUpdatePhoneVerification(@Body UpdateVerificationStatusRequest request);

    @GET(ENDPOINT_USER_DETAIL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UserDetailResponse> getUserDetails();

    // logout call
    @POST(ENDPOINT_ACCOUNT_LOGOUT)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<LogoutResponse> doLogoutApiCall();

    // Login With Facebook call
    @POST(ENDPOINT_FACEBOOK_LOGIN)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<FacebookLoginResponse> doLoginWithFacebook(@Body FacebookLoginRequest request);

    // delete cards in home fragment
    @POST(ENDPOINT_DELETE_SHARED_LIST_HOME)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<HomeCardDeleteResponse> doDeleteCards(@Body HomeListCardDeleteRequest request);

    // delete cards in home fragment
    @POST(ENDPOINT_MAINTAIN_ORDER_SHARED_LIST_HOME)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ReorderHomeListData> doReorderHomeCards(@Body List<ReorderCardsHomeRequest> request);

    // Locate Account Call
    @GET(ENDPOINT_LOCATE_ACCOUNT)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<LocateAccountResponse> doLocateAccount(@Query("emailOrPhone") String emailOrPhone);

    @POST(ENDPOINT_SEND_OTP_FOR_RESET_PASSWORD)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<SendOtpResponse> doSendOtp(@Body SendOtpRequest request);

    // update username call
    @POST(ENDPOINT_UPDATE_USER_NAME)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UpdateUserResponse> doUpdateUserName(@Body UpdateUserNameRequest request);


    // update userEmail call
    @POST(ENDPOINT_UPDATE_EMAIL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UpdateUserEmailResponse> doUpdateUserEmail(@Body UpdateEmailRequest request);


    @GET(ENDPOINT_HOME_DETAIL_LIST)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<HomeDetailListResponse> getHomeDetailsList(@Query("listId") String listId);

    @GET(ENDPOINT_NOTIFICATION_SETTINGS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<NotificationSettingsResponse> doNotificationSettingsCall();

    // Verify OTP Call
    @POST(ENDPOINT_VERIFY_OTP)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<VerifyOtpResponse> doVerifyOtp(@Body VerifyOtpRequest request);

    // Reset Password
    @POST(ENDPOINT_RESET_PASSWORD)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PUBLIC_API)
    Observable<ResetPasswordResponse> doResetPassword(@Body ResetPasswordRequest request);

    // Change Password Call
    @POST(ENDPOINT_CHANGE_PASSWORD)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ChangePasswordResponse> doChangePassword(@Body ChangePasswordRequest request);

    // Add update List call

    @POST(ENDPOINT_ADD_UPDATE_LIST_CALL_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<AddUpdateListResponse> doAddUpdateListCall(@Body AddUpdateListRequest request);

    @POST(ENDPOINT_PUSHNOTIFICATION_ENABLE_DISABLE_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<PushNotificationSettingsResponse> doPushNotificationSettingsCall(@Body PushNotificationSettingsRequest request);

    @POST(ENDPOINT_UPDATE_PROXIMITY_RANGE_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UpdateProximityRangeResponse> doUpdateProximityRangeCall(@Body UpdateProximityRangeRequest request);

    // Update List Header call
    @POST(ENDPOINT_ADD_UPDATE_LIST_COLOR_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UpdateHeaderColorResponse> doUpdateHeaderColorCall(@Body UpdateHeaderColorRequest request);

    // Add Items ---Recent Items call
    @GET(ENDPOINT_RECENT_ITEMS_LIST_CALL_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<RecentItemsResponse> doGetRecentItemsListCall();


    @GET(ENDPOINT_RECENT_LOCATION_ITEMS_LIST_CALL_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<AddItemsLocationResponse> doGetRecentItemsLocationListCall();


    @GET(ENDPOINT_GET_LAT_LNG_FRM_ADDRESS_CALL_URL)
        // @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<RecentLocationAddItemsResponse> doGetGoogleLocationCall(@Query("query") String query,
                                                                       @Query("location") String loc,
                                                                       @Query("radius") int radius,
                                                                       @Query("key") String Key);
// nearby search

    @GET(ENDPOINT_GET_NEARBY_SEARCH_ADDRESS_CALL_URL)
        // @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<RecentLocationAddItemsResponse> doGetNearbySearchLocationCall(@Query("location") String loc,
                                                                             @Query("radius") int radius,
                                                                             @Query("key") String Key,
                                                                             @Query("type") String type);


    // Add Items ---Post Items call


    @POST(ENDPOINT_ADD_UPDATE_ITEMS_LIST_CALL_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<AddUpdateItemResponse> doAddUpdateItemsCall(@Body RequestBody request);

    @Multipart
    @POST(ENDPOINT_ADD_UPDATE_ITEMS_NEW_LIST_CALL_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<AddUpdateItemResponse> doUpdateRecentItemsCall(@Part("ItemId") RequestBody ItemId,
                                                              @Part("ItemName") RequestBody ItemName,
                                                              @Part("ItemTime") RequestBody ItemTime,
                                                              @Part("Latitude") RequestBody Latitude,
                                                              @Part("ListId") RequestBody ListId,
                                                              @Part("ListName") RequestBody ListName,
                                                              @Part("Longitude") RequestBody Longitude,
                                                              @Part("Location") RequestBody Location,
                                                              @Part("StatusId") RequestBody StatusId,
                                                              @Part MultipartBody.Part image);

    // Get User Setting
    @GET(ENDPOINT_GET_USER_SETTINGS_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<GetUserSettingResponse> doGetUserSettingsCall(@Query("ListId") int listId);

    @POST(ENDPOINT_SHARE_LIST_CONTACT_URL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ShareListResponse> doShareListToContactsCall(@Body ShareListRequest request);

    @POST(ENDPOINT_GET_DELETE_RECENT_ITEMS_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteRecentItemsResponse> doDeleteRecentItemsCall();

    @POST(ENDPOINT_GET_DELETE_RECENT_LOCATIONS_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteRecentItemsResponse> doDeleteRecentLocationsCall();

    @POST(ENDPOINT_DELETE_ITEM_LIST_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteItemListResponse> doDeleteItemListCall(@Body DeleteItemListRequest deleteItemListRequest);


    @POST(ENDPOINT_CHANGE_ITEM_STATUS_LIST_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ChangeItemStatusResponse> dochangeItemStatusListCall(@Body ChangeItemStatusRequest changeItemStatusRequest);

    @POST(ENDPOINT_DELETE_USER_FROM_SHARED_LIST_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteUserResponse> deleteUserFromSharedListCall(@Body HashMap<String, String> requestMap);

    @POST(ENDPOINT_UPDATE_NOTIFICATION_SETTING_BY_OWNER_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteUserResponse> updateNotificationSettingByOwner(@Body UpdateNotificationSettingRequest request);


    @POST(ENDPOINT_ALLOW_DELETION_AFTER_COMPLETE_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteUserResponse> allowAutoDeletionOnComplete(@Body AllowAutoDeleteRequest request);


    @POST(ENDPOINT_CHANGE_PUSH_STATUS_FOR_OTHERS_LIST_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DeleteUserResponse> changePushStatusForOthersList(@Body HashMap<String, Integer> hashMap);

    @POST(ENDPOINT_REORDERING_ITEMS_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ReorderHomeListData> doReorderItems(@Body List<ReorderItemsMyList> request);

    @POST(ENDPOINT_UPDATE_DEVICE_INFO_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<UpdateDeviceInfoResponse> doUpdateDeviceInfo(@Body UpdateDeviceInfoRequest updateDeviceInfoRequest);

    // Get Proximity items
    @GET(ENDPOINT_GET_PROXIMITY_ITEM_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<GetProximityResponse> doGetProximityResponseCall();

    // send bluetooth connection push

    @POST(ENDPOIT_ENABLE_DIABLE_BLUETOOTH_PUSH_API_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<BluetoothResponse> doGetBluetoothResponse(@Body BluetoothRequest request);


    @POST(ENDPOINT_UNSHARE_LIST_HOME)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<HomeCardDeleteResponse> doGetUnshareList(@Body UnshareListRequest request);

    @GET(ENDPOINT_GET_LIST_SETTINGS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<GetListSettingsResponse> doGetListSettings(@Query("ListId") int listId);

    @POST(ENDPOINT_SAVE_LIST_SETTINGS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<Void> doUpdateListSettings(@Body UpdateListSettingsRequest request);

    @GET(ENDPOINT_GET_ALL_BLUETOOTH_ITEMS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<GetBluetoothItemsListResponse> doGetAllBluetoothItems();


    // Daily Reminder Expiring and expired


    @POST(ENDPOINT_DAILY_EXPIRING_ITEMS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DailyReminderExpiringResponse> doSaveDailyReminderExpiring(@Body DailyReminderExpiringRequest request);

    @POST(ENDPOINT_DAILY_EXPIRED_ITEMS)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<DailyReminderExpiredResponse> doSaveDailyReminderExpired(@Body DailyReminderExpiringRequest request);


    @POST(ENDPOINT_REMINDER_TIME)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ReminderTimeResponse> doSaveReminderTime(@Body ReminderTimeRequest request);


    @GET(ENDPOINT_NOTIFICATION_LIST_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<NotificationListResponse> doGetNotificationList(@Query("PageIndex") int pageIndex, @Query("PageSize") int pageSize);

    @PUT(ENDPOINT_NOTIFICATION_LIST_READ_UPDATE_CALL)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<NotificationReadResponse> doUpdateNotificationRead(@Query("NotificationId") int notificationId);


    @PUT(ENDPOINT_RESET_NOTIFICATION_COUNT)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<ResetNotificationCountResponse> doResetNotificationCount();


    @GET(ENDPOINT_GET_NOTIFICATION_COUNT)
    @Headers(ApiHeader.API_AUTH_TYPE + HEADER_PARAM_SEPARATOR + ApiHeader.PROTECTED_API)
    Observable<NotificationCountResponse> doGetNotificationCount();


    class Factory {

        private static final int NETWORK_CALL_TIMEOUT = 60;

        public static ApiCall create(ApiInterceptor apiInterceptor) {

            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.addInterceptor(apiInterceptor);

            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

            logging.setLevel(
                    BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

            builder.addInterceptor(logging);

            builder.readTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);

            builder.writeTimeout(NETWORK_CALL_TIMEOUT, TimeUnit.SECONDS);

            OkHttpClient httpClient = builder.build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.BASE_URL)
                    .client(httpClient)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();

            return retrofit.create(ApiCall.class);
        }
    }
}
