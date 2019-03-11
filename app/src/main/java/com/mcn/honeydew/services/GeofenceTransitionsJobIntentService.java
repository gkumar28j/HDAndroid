package com.mcn.honeydew.services;

/**
 * Created by amit on 15/3/18.
 */


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.JobIntentService;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.google.android.gms.location.GeofencingRequest;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.GeofenceErrorMessages;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.inject.Inject;

import timber.log.Timber;

import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_LIST_ID;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_NOTIFICATION_TYPE;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.LIST_HEADER_COLOR;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.LIST_NAME;

/**
 * Listener for geofence transition changes.
 * <p>
 * Receives geofence transition events from Location Services in the form of an Intent containing
 * the transition type and geofence id(s) that triggered the transition. Creates a notification
 * as the output.
 */
public class GeofenceTransitionsJobIntentService extends JobIntentService {

    private static final int JOB_ID = 573;

    private static final String TAG = "GeofenceTransitionsIS";

    private static final String CHANNEL_ID = "channel_01";
    String GROUP_KEY_SAME_LIST = "com.mcn.honeydew.LIST";

    @Inject
    DataManager mDataManager;

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, GeofenceTransitionsJobIntentService.class, JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((HoneyDewApp) getApplication()).getComponent())
                .build();
        component.inject(this);
    }

    /**
     * Handles incoming intents.
     *
     * @param intent sent by Location Services. This Intent is provided to Location
     *               Services (inside a PendingIntent) when addGeofences() is called.
     */
    @SuppressLint("TimberArgCount")
    @Override
    protected void onHandleWork(Intent intent) {
        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            String errorMessage = GeofenceErrorMessages.getErrorString(this,
                    geofencingEvent.getErrorCode());
            Timber.e(TAG, errorMessage);
            Crashlytics.log(errorMessage);
            return;
        }

        if (mDataManager == null || mDataManager.getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            return;
        }

        // Get the transition type.
        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        // Test that the reported transition was of interest.
        if (geofenceTransition == GeofencingRequest.INITIAL_TRIGGER_ENTER) {

            // Get the geofences that were triggered. A single event can trigger multiple geofences.
            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();

            HashMap<Integer, List<GetProximityResponse.Result>> notificationList = new HashMap<>();

            for (Geofence geo : triggeringGeofences) {
                String dataString;
                String requestId = geo.getRequestId();
                if (requestId.contains("Entered:")) {
                    int startIndex = requestId.indexOf(":");
                    dataString = requestId.substring(startIndex + 1, requestId.length()).trim();
                } else {
                    dataString = requestId;
                }

                String[] info = dataString.split(ProximityJobIntentService.SEPERATE_KEY);

                GetProximityResponse.Result data = new GetProximityResponse.Result();
                data.setListId(Integer.valueOf(info[0]));
                data.setListName(info[2]);
                data.setLocation(info[3]);
                data.setListHeaderColor(info[4]);

                if (notificationList.containsKey(data.getListId())) {
                    List<GetProximityResponse.Result> mlist = notificationList.get(data.getListId());
                    notificationList.put(data.getListId(), mlist);

                } else {
                    List<GetProximityResponse.Result> mlist = new ArrayList<>();
                    mlist.add(data);
                    notificationList.put(data.getListId(), mlist);
                }

            }
            Iterator it = notificationList.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry) it.next();
                List<GetProximityResponse.Result> mResults = notificationList.get(pair.getKey());
                List<GetProximityResponse.Result> mlist = removeDuplicates(mResults);
                if (mlist != null && mlist.size() > 0) {

                    for (GetProximityResponse.Result geofence : mlist) {
                        Bundle bundle = new Bundle();
                        bundle.putString(KEY_NOTIFICATION_TYPE, "proximity");
                        bundle.putString(KEY_LIST_ID, String.valueOf(geofence.getListId()));
                        bundle.putString(LIST_NAME, geofence.getListName());
                        bundle.putString(LIST_HEADER_COLOR, geofence.getListHeaderColor());

                        String notificationMessage = getString(R.string.notification_message)
                                .replace("@listName", geofence.getListName())
                                .replace("@locationName", geofence.getLocation());
                        sendNotification(bundle, notificationMessage);
                    }

                }

                it.remove(); // avoids a ConcurrentModificationException
            }

        } else {
            // Log the error.
            Timber.e(TAG, getString(R.string.geofence_transition_invalid_type, geofenceTransition));
            Crashlytics.log(getString(R.string.geofence_transition_invalid_type));

        }
    }

    /**
     * Gets transition details and returns them as a formatted string.
     *
     * @param geofenceTransition  The ID of the geofence transition.
     * @param triggeringGeofences The geofence(s) triggered.
     * @return The transition details formatted as String.
     */
    private String getGeofenceTransitionDetails(
            int geofenceTransition,
            List<Geofence> triggeringGeofences) {

        String geofenceTransitionString = getTransitionString(geofenceTransition);

        // Get the Ids of each geofence that was triggered.
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }
        String triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList);

        return geofenceTransitionString + ": " + triggeringGeofencesIdsString;
    }

    /**
     * Posts a notification in the notification bar when a transition is detected.
     * If the user clicks the notification, control goes to the MainActivity.
     */
    private void sendNotification(Bundle bundle, String msg) {

        String message = Html.fromHtml(msg).toString();

        // Get an instance of the Notification manager
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound), null);

            mNotificationManager.createNotificationChannel(mChannel);
        }


        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationIntent.putExtras(bundle);

      /*  TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

        // Add the main Activity to the task stack as the parent.
        stackBuilder.addParentStack(MainActivity.class);

        stackBuilder.addNextIntent(notificationIntent);*/

//        PendingIntent notificationPendingIntent =
//                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) (Math.random() * 10000), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setSmallIcon(R.mipmap.ic_stat_honey)
                .setContentTitle("HoneyDew")
                .setContentText(message)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.notification_sound))
                .setContentIntent(pendingIntent);


        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }
        // Dismiss notification once the user touches it.
        builder.setAutoCancel(true);

        // Issue the notification
        mNotificationManager.notify((int) (Math.random() * 10000), builder.build());
    }

    /**
     * Maps geofence transition types to their human-readable equivalents.
     *
     * @param transitionType A transition type constant defined in Geofence
     * @return A String indicating the type of transition
     */
    private String getTransitionString(int transitionType) {
        switch (transitionType) {
            case GeofencingRequest.INITIAL_TRIGGER_ENTER:
                return "Entered";
            case GeofencingRequest.INITIAL_TRIGGER_EXIT:
                return "exit";
            default:
                return "Unknown";
        }
    }


    public List<GetProximityResponse.Result> removeDuplicates(List<GetProximityResponse.Result> list) {
        Set set = new TreeSet(new Comparator() {

            @Override
            public int compare(Object o1, Object o2) {
                if (((GetProximityResponse.Result) o1).getLocation().trim().equals(((GetProximityResponse.Result) o2).getLocation().trim())) {
                    return 0;
                }
                return 1;
            }
        });
        set.addAll(list);

        final List newList = new ArrayList(set);
        return newList;
    }
}

