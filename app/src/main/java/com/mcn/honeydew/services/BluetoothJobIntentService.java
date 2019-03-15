package com.mcn.honeydew.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.bluetooth.BluetoothA2dp;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.NotificationType;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.http.util.TextUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import timber.log.Timber;


/**
 * Created by gkumar on 23/3/18.
 */

public class BluetoothJobIntentService extends JobIntentService {

    static final int JOB_ID = 170;
    private static final int FAIL = -1;
    private static final String CHANNEL_ID = "5";

    public static final String KEY_NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_TYPE = "notificationType";
    private static final String KEY_MESSAGE = "message";
    public static final String KEY_LIST_ID = "ListId";
    public static final String KEY_LIST_NAME = "ListName";
    public static final String LIST_NAME = "ListName";
    public static final String KEY_HEADER_COLOR = "headercolor";
    public static final String KEY_IS_OWNER = "IsOwner";

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

    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, BluetoothJobIntentService.class, JOB_ID, work);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        // Checking if user is logged in
        if (mDataManager == null || mDataManager.getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType())
            return;

        String action = intent.getAction();

        if (action != null && action.equals(BluetoothA2dp.ACTION_CONNECTION_STATE_CHANGED)) {
            // BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

            Bundle extras = intent.getExtras();
            if (extras == null) {
                Crashlytics.log("Bluetooth extras is null");
                return;
            }

            switch (intent.getExtras().getInt("android.bluetooth.profile.extra.STATE")) {

                case BluetoothProfile.STATE_CONNECTING:
                    Timber.d("Connecting..!");
                    break;

                case BluetoothProfile.STATE_CONNECTED:
                    mDataManager.setBluetoothDeviceConnected(true);
                    Timber.d("Bluetooth Connected!");
                    if (mDataManager.getUserData().isIsBluetoothNotification()) {
                        Timber.d("Bluetooth Setting is On!");

                        // Getting saved data from local storage
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> items = mDataManager.getSavedBluetoothItems();
                        for (GetBluetoothItemsListResponse.BluetoothItem btItem : items) {
                            Bundle bundle = new Bundle();
                            bundle.putString(KEY_MESSAGE, btItem.getMessage());
                            bundle.putString(KEY_LIST_ID, String.valueOf(btItem.getListId()));
                            bundle.putString(KEY_LIST_NAME, btItem.getListName());
                            bundle.putString(KEY_HEADER_COLOR, btItem.getListHeaderColor());
                            bundle.putString(KEY_IS_OWNER, String.valueOf(btItem.isOwner()));
                            bundle.putString(KEY_NOTIFICATION_TYPE, "bluetooth");
                            sendNotification(bundle);
                            btItem.setSent(true);

                            try {
                                Thread.sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }

                        // Saving bluetooth items
                        mDataManager.saveBluetoothItemList(new Gson().toJson(items));
                    }
                    break;

                case BluetoothProfile.STATE_DISCONNECTED:
                    mDataManager.setBluetoothDeviceConnected(false);
                    Timber.d("Bluetooth Disconnected!");
                    ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = mDataManager.getSavedBluetoothItems();
                    for (GetBluetoothItemsListResponse.BluetoothItem it : savedItems) {
                        it.setSent(false);
                    }
                    // Saving bluetooth items
                    mDataManager.saveBluetoothItemList(new Gson().toJson(savedItems));
                    break;
            }

        }


    }

    private void sendNotification(Bundle bundle) {
        // Get an instance of the Notification manager

        String msg = bundle.getString(KEY_MESSAGE);
     /*   String listId = bundle.getString(KEY_LIST_ID);
        String listName = bundle.getString(KEY_LIST_NAME);
        String headerColor = bundle.getString(KEY_HEADER_COLOR);
        String isOwner = bundle.getString(KEY_IS_OWNER);*/

        String message = Html.fromHtml(msg).toString();
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_title);
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Setting hunter notification sound if notification type is expire item
            /*if (!TextUtils.isEmpty(notificationType) && !notificationType.equalsIgnoreCase(NotificationType.EXPIRE_ITEM)) {
                mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound), null);
            } else {
                mChannel.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION), null);
            }*/

            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound), null);

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


        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        builder.setAutoCancel(true);
        mNotificationManager.notify((int) (Math.random() * 10000), builder.build());
    }


}
