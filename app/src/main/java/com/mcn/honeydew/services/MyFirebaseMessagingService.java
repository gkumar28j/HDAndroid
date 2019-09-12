/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mcn.honeydew.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.model.request.UpdateDeviceInfoRequest;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.NotificationType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private static final String CHANNEL_ID = "5";
    public static final String KEY_NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_TYPE = "notificationType";
    private static final String KEY_MESSAGE = "message";
    public static final String KEY_LIST_ID = "ListId";
    public static final String LIST_NAME = "ListName";
    public static final String LIST_HEADER_COLOR = "headercolor";
    public static final String IS_OWNER = "IsOwner";
    public static final String IS_IN_PROGRESS = "InProgress";

    public static final String KEY_SYNC_REQUIRED = "SyncRequired";


    @Inject
    DataManager mDataManager;

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((HoneyDewApp) getApplication()).getComponent())
                .build();
        component.inject(this);
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (mDataManager.getCurrentUserLoggedInMode() == DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()) {
            String mType = "";
            String notificationType = "";
            String mMessage = "";
            String listId = "";
            String listName = "";
            String listHeaderColor = "";
            String isOwner = "0";
            String isInProgress = "0";
            Bundle bundle = new Bundle();


            if (remoteMessage.getData().containsKey(KEY_NOTIFICATION_TYPE)) {
                mType = remoteMessage.getData().get(KEY_NOTIFICATION_TYPE).toString();
                bundle.putString(KEY_NOTIFICATION_TYPE, mType);
            }
            if (remoteMessage.getData().containsKey(KEY_MESSAGE)) {
                mMessage = remoteMessage.getData().get(KEY_MESSAGE).toString();
                bundle.putString(KEY_MESSAGE, mMessage);
            }
            if (remoteMessage.getData().containsKey(KEY_LIST_ID)) {
                listId = remoteMessage.getData().get(KEY_LIST_ID).toString();
                bundle.putString(KEY_LIST_ID, listId);
            }
            if (remoteMessage.getData().containsKey(LIST_NAME)) {
                listName = remoteMessage.getData().get(LIST_NAME).toString();
                bundle.putString(LIST_NAME, listName);
            }
            if (remoteMessage.getData().containsKey(LIST_HEADER_COLOR)) {
                listHeaderColor = remoteMessage.getData().get(LIST_HEADER_COLOR).toString();
                bundle.putString(LIST_HEADER_COLOR, listHeaderColor);
            }

            if (remoteMessage.getData().containsKey(NOTIFICATION_TYPE)) {
                notificationType = remoteMessage.getData().get(NOTIFICATION_TYPE);
            }

            if (remoteMessage.getData().containsKey(IS_OWNER)) {
                isOwner = remoteMessage.getData().get(IS_OWNER);
                bundle.putString(IS_OWNER, isOwner);
            }


            if (remoteMessage.getData().containsKey(IS_IN_PROGRESS)) {
                isInProgress = remoteMessage.getData().get(IS_IN_PROGRESS);
                bundle.putString(IS_IN_PROGRESS, isInProgress);
            }

            if (notificationType.equalsIgnoreCase(NotificationType.DELETE_LIST) ||
                    notificationType.equalsIgnoreCase(NotificationType.UNSHARE_LIST) ||
                    notificationType.equalsIgnoreCase(NotificationType.DELETE_SHARE_LIST) ||
                    notificationType.equalsIgnoreCase(NotificationType.UPDATE_LIST)) {
                // Sync items but don't show notification
                bundle.putBoolean(KEY_SYNC_REQUIRED, true);

                // Calling API to fetch new list of bluetooth items
                fetchBluetoothItem(bundle, notificationType);

            } else if (notificationType.equalsIgnoreCase(NotificationType.EXPIRE_ITEM)) {
                // Sync not needed
                bundle.putBoolean(KEY_SYNC_REQUIRED, false);
                sendNotification(mMessage, bundle, notificationType);
            } else if (notificationType.equalsIgnoreCase(NotificationType.EXPIRED_ITEM)) {
                // For silent notification check. When item has expired
                // mType = remoteMessage.getData().get("ExpiredItem");
                bundle.putString(KEY_NOTIFICATION_TYPE, notificationType);

                // Calling API to fetch new list of bluetooth items
                fetchBluetoothItem(bundle, notificationType);
            } else {
                // Show notification & sync items

                bundle.putBoolean(KEY_SYNC_REQUIRED, true);
                sendNotification(mMessage, bundle, notificationType);

                // Calling API to fetch new list of bluetooth items
                fetchBluetoothItem(bundle, notificationType);

            }


        }


    }


    private void sendNotification(String msg, Bundle bundle, String notificationType) {
        // Get an instance of the Notification manager
        String message = Html.fromHtml(msg).toString();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_title);
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Setting hunter notification sound if notification type is expire item
            if (!TextUtils.isEmpty(notificationType) && !notificationType.equalsIgnoreCase(NotificationType.EXPIRE_ITEM)) {
                mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound), null);
            } else {
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            }


            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.putExtras(bundle);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 10000), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_stat_honey)
                .setContentTitle(getString(R.string.app_title))
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentIntent(pendingIntent);

        if (!TextUtils.isEmpty(notificationType) && !notificationType.equalsIgnoreCase(NotificationType.EXPIRE_ITEM)) {
            builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound));
        } else {
            builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        }

        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        builder.setAutoCancel(true);
        mNotificationManager.notify((int) (Math.random() * 10000), builder.build());
    }


    private void fetchBluetoothItem(Bundle bundle, String notificationType) {
        try {
            String url = AppConstants.BASE_URL + "v1_2/api/Notification/GetAllPushNotification";
            Timber.log(Log.DEBUG, "GetAllPushNotification: URL " + url);
            HttpGet httpGet = new HttpGet(url);
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(
                    CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);

            httpGet.addHeader(ApiHeader.HEADER_PARAM_API_KEY, mDataManager.getApiHeader().getmApiKey());
            httpGet.addHeader(ApiHeader.HEADER_PARAM_AUTHRIZATION, mDataManager.getApiHeader().getmTokenType() + " " + mDataManager.getApiHeader().getmAccessToken());
            httpGet.addHeader(ApiHeader.HEADER_PARAM_REFRESH_TOKEN, mDataManager.getApiHeader().getmRefreshToken());


                /*JSONObject json = new JSONObject();
                json.put("IsConnected", Integer.parseInt(params[0]));
                StringEntity se = new StringEntity(json.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                httppost.setEntity(se);*/

            HttpResponse response = httpclient.execute(httpGet);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                GetBluetoothItemsListResponse res = new Gson().fromJson(data, GetBluetoothItemsListResponse.class);

                final ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = mDataManager.getSavedBluetoothItems();
                final ArrayList<GetBluetoothItemsListResponse.BluetoothItem> recentItems = new ArrayList<>(Arrays.asList(res.getResult()));

                //savedItems.retainAll(recentItems);

                if (mDataManager.getUserData().isIsBluetoothNotification() && mDataManager.isBluetoothDeviceConnected()) {
                    for (int i = 0; i < recentItems.size(); i++) {
                        GetBluetoothItemsListResponse.BluetoothItem recentItem = recentItems.get(i);

                        if (!savedItems.contains(recentItem)) {
                            bundle.putString(KEY_LIST_ID, String.valueOf(recentItem.getListId()));
                            bundle.putString(LIST_NAME, recentItem.getListName());
                            bundle.putString(LIST_HEADER_COLOR, recentItem.getListHeaderColor());
                            bundle.putString(IS_OWNER, String.valueOf(recentItem.isOwner()));
                            sendNotification(recentItem.getMessage(), bundle, notificationType);
                        }
                        recentItem.setSent(true);
                    }

                    mDataManager.saveBluetoothItemList(new Gson().toJson(recentItems));
                }


                Timber.d("Get Bluetooth ITEM API response: " + data);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        //   super.onNewToken(token);
        Timber.d("FCM Token: " + token);

        if (mDataManager.getCurrentUserLoggedInMode() ==
                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()) {
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            sendRegistrationToServer(token);
        } else {

            mDataManager.saveDeviceId(token);
            mDataManager.sendDeviceIdToServer(false);

        }
    }

    private void sendRegistrationToServer(String token) {
        mDataManager.doUpdateDeviceInfo(new UpdateDeviceInfoRequest(0.0, 0.0, String.valueOf(BuildConfig.VERSION_NAME), ApiCall.API_VERSION, BuildConfig.VERSION_NAME, token, AppConstants.DEVICE_TYPE, CommonUtils.getOffsetTimeZone(),
                CommonUtils.getOffsetTimeZone(),
                CommonUtils.getTimeZoneOffsetName()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<UpdateDeviceInfoResponse>() {
                    @Override
                    public void accept(UpdateDeviceInfoResponse response) throws Exception {
                        mDataManager.sendDeviceIdToServer(true);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {


                    }
                });
        mDataManager.doUpdateDeviceInfo(new UpdateDeviceInfoRequest(0.0, 0.0, String.valueOf(BuildConfig.VERSION_NAME), ApiCall.API_VERSION, BuildConfig.VERSION_NAME, token, AppConstants.DEVICE_TYPE, CommonUtils.getOffsetTimeZone(),
                CommonUtils.getOffsetTimeZone(),
                CommonUtils.getTimeZoneOffsetName()));
        mDataManager.sendDeviceIdToServer(true);
    }
}
