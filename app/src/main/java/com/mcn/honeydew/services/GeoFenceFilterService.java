package com.mcn.honeydew.services;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.receivers.GeofenceBroadcastReceiver;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.GeofenceErrorMessages;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import timber.log.Timber;

public class GeoFenceFilterService extends Service {
    public static final String ACTION_NOTIFICATION = "com.mcn.honeydew.NOTIFICATION";
    public static final String TAG = GeoFenceFilterService.class.getSimpleName();

    public static Intent getStartIntent(Context context) {
        return new Intent(context, GeoFenceFilterService.class);
    }

    private final double DEFAULT_RANGE = 1609.34;
    private final String RANGE_1_8_MILE = "1/8 mile";
    private final String RANGE_1_4_MILE = "1/4 mile";
    private final String RANGE_1_2_MILE = "1/2 mile";
    private final String RANGE_3_4_MILE = "3/4 mile";
    private final String RANGE_1_MILE = "1 mile";

    public static boolean isRunning = false;

    private NotificationManagerCompat mNotificationManager;
    private final static int NOTIFICATION_ID = 95;
    private final static String CHANNEL_ID = "service_notification_id";

    private List<GetProximityResponse.Result> mSavedItemsList;

    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;
    private LocationRequest mLocationRequest;
    private ItemCompleteReceiver mReceiver;

    private double rangeInMeter = DEFAULT_RANGE;

    public static final String SEPARATE_KEY = "_@#";

    /**
     * Used when requesting to add or remove geofences.
     */
    private PendingIntent mGeofencePendingIntent;


    @Inject
    DataManager mDataManager;

    GeofencingClient mGeofencingClient;


    @Override
    public void onCreate() {
        super.onCreate();
        ServiceComponent component = DaggerServiceComponent.builder()
                .applicationComponent(((HoneyDewApp) getApplication()).getComponent())
                .build();
        component.inject(this);

        // Creating broadcast receiver and registering it
        mReceiver = new ItemCompleteReceiver();
        IntentFilter intentFilter = new IntentFilter(AppConstants.ACTION_ITEM_COMPLETE);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, intentFilter);

        //Toast.makeText(GeoFenceFilterService.this, "Service Started", Toast.LENGTH_SHORT).show();
        mNotificationManager = NotificationManagerCompat.from(this);
        createNotificationChannel();
        isRunning = true;

        // Showing non-cancelable notification if app is being used in device running Android version O or above.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createAndShowForegroundNotification();
        }


        mGeofencingClient = LocationServices.getGeofencingClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        // Creating Location Request
        mLocationRequest = new LocationRequest();
       /* mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(5000);*/
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(100);


        // Creating Location Callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                // Checking proximity setting for off, because in case of service caching this method get executed even after stopping service.
                if (mDataManager.getUserData() == null || !mDataManager.getUserData().isIsProximityNotification())
                    return;

                // Toast.makeText(GeoFenceFilterService.this, "Location updated in Proximity Service", Toast.LENGTH_SHORT).show();
                Timber.log(1, "Location Result: " + locationResult.getLastLocation());
                //mFusedLocationClient.removeLocationUpdates(mLocationCallback);


                if (mSavedItemsList != null && !mSavedItemsList.isEmpty()) {
                    //mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(GeoFenceFilterService.this);
                    removeGeofences();
                    mSavedItemsList.clear();
                }

                // Getting saved location list
                GetProximityResponse.Result[] array = new Gson().fromJson(mDataManager.getSavedProximityItems(), GetProximityResponse.Result[].class);

                if (array != null && array.length > 0) {
                    mSavedItemsList = new ArrayList<>(Arrays.asList(array));
                    // Setting location
                    setDistance(locationResult.getLastLocation());

                    removeItemsBasedOnLocationName();

                    // Sort List
                    sortList();

                    // Adding updated list into Geo-fence [only 20 items]
                    addGeofences(getGeofenceList(mSavedItemsList));

                    //addItemsIntoGeoFence();
                }


            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        // Requesting location update
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    @Override
    public void onDestroy() {
        isRunning = false;
        mNotificationManager.cancel(NOTIFICATION_ID);
        // Removing location updates
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);

        // Unregistering receiver
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);

        // Removing geo-fencing and disposing object
        if (mGeofencingClient != null) {
            // mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(GeoFenceFilterService.this);
            removeGeofences();
            mGeofencingClient = null;
        }
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    // Method to save distance in location items
    private void setDistance(Location currentLoc) {
        if (mSavedItemsList == null || mSavedItemsList.isEmpty())
            return;

        for (int i = 0; i < mSavedItemsList.size(); i++) {
            GetProximityResponse.Result loc = mSavedItemsList.get(i);
            double distance = calculateDistance(loc.getLatitude(), loc.getLongitude(), currentLoc);
            mSavedItemsList.get(i).setDistanceFromCurrentLoc(distance);
        }
    }

    // Method to calculate distance
    private double calculateDistance(double lat, double lng, Location currentLoc) {
        if (currentLoc == null) {
            return 0;
        }

        Location targetLoc = new Location("");
        targetLoc.setLatitude(lat);
        targetLoc.setLongitude(lng);
        return currentLoc.distanceTo(targetLoc);
    }

    /**
     * Adds geofences. This method should be called after the user has granted the location
     * permission.
     */

    @SuppressWarnings("MissingPermission")
    private synchronized void addGeofences(List<Geofence> dataGeofences) {
        if (!checkPermissions()) {
            return;
        }

        if (mGeofencingClient == null) {
            return;
        }

        int size = dataGeofences.size() >= 90 ? 90 : dataGeofences.size();


        for (int i = 0; i < size; i++) {

            Geofence data = dataGeofences.get(i);

            mGeofencingClient.addGeofences(getGeofencingRequest(data), getGeofencePendingIntent())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isComplete())
                                Timber.e("Add geo-fence task is completed");

                            if (task.isSuccessful()) {
                                Timber.e("Add Succeess");
                            } else {
                                String errorMessage = GeofenceErrorMessages.getErrorString(GeoFenceFilterService.this, task.getException());
                                Timber.e(errorMessage);
                                Crashlytics.log(errorMessage);
                            }
                        }
                    });
        }
    }


    /**
     * Builds and returns a GeofencingRequest. Specifies the list of geofences to be monitored.
     * Also specifies how the geofence notifications are initially triggered.
     *
     * @param data
     */
    private GeofencingRequest getGeofencingRequest(Geofence data) {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();

        // The INITIAL_TRIGGER_ENTER flag indicates that geofencing service should trigger a
        // GEOFENCE_TRANSITION_ENTER notification when the geofence is added and if the device
        // is already inside that geofence.
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);

        // Add the geofences to be monitored by geofencing service.
        builder.addGeofence(data);

        // Return a GeofencingRequest.
        return builder.build();
    }

    /**
     * Removes geofences. This method should be called after the user has granted the location
     * permission.
     */
    @SuppressWarnings("MissingPermission")
    private synchronized void removeGeofences() {
        if (!checkPermissions()) {
            return;
        }

        if(mGeofencingClient==null){
            return;
        }

        mGeofencingClient.removeGeofences(getGeofencePendingIntent()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isComplete()) {
                    Timber.d("Delete all geo-fence task is completed.");
                }
                if (task.isSuccessful()) {
                    Timber.d("All Geo-fences Remove Successfully");
                } else {
                    String errorMessage = GeofenceErrorMessages.getErrorString(GeoFenceFilterService.this, task.getException());
                    Timber.e(errorMessage);
                }
            }
        });
    }

    /**
     * Gets a PendingIntent to send with the request to add or remove Geofences. Location Services
     * issues the Intent inside this PendingIntent whenever a geofence transition occurs for the
     * current list of geofences.
     *
     * @return A PendingIntent for the IntentService that handles geofence transitions.
     */
    private PendingIntent getGeofencePendingIntent() {
        // Reuse the PendingIntent if we already have it.
        if (mGeofencePendingIntent != null) {
            return mGeofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // addGeofences() and removeGeofences().
        mGeofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return mGeofencePendingIntent;
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private String getRangeByPosition(int position) {
        String range = null;
        switch (position) {
            case 0:
                range = RANGE_1_8_MILE;
                break;
            case 1:
                range = RANGE_1_4_MILE;
                break;
            case 2:
                range = RANGE_1_2_MILE;
                break;
            case 3:
                range = RANGE_3_4_MILE;
                break;
            case 4:
                range = RANGE_1_MILE;
                break;
            default:
                break;
        }

        return range;
    }

    private List<Geofence> getGeofenceList(List<GetProximityResponse.Result> places) {

        String rangeString = getRangeByPosition(mDataManager.getUserData().getProximityId());
        if (TextUtils.isEmpty(rangeString)) {
            rangeString = String.valueOf(DEFAULT_RANGE);
            rangeInMeter = DEFAULT_RANGE;
        }
        switch (rangeString) {
            case RANGE_1_8_MILE:
                rangeInMeter = ((1.0 / 8) * 1609.34);
                break;

            case RANGE_1_4_MILE:
                rangeInMeter = ((1.0 / 4) * 1609.34);
                break;

            case RANGE_1_2_MILE:
                rangeInMeter = ((1.0 / 2) * 1609.34);
                break;

            case RANGE_3_4_MILE:
                rangeInMeter = ((3.0 / 4) * 1609.34);
                break;

            case RANGE_1_MILE:
                rangeInMeter = 1609.34;
                break;
        }
        List<Geofence> geofenceList = new ArrayList<>();

        for (GetProximityResponse.Result place : places) {
            if (TextUtils.isEmpty(place.getLocation()))
                continue;


            String color = TextUtils.isEmpty(place.getListHeaderColor()) ? "#153359" : place.getListHeaderColor();


            // Request id contains ListId and ItemId of an item
            String sb = place.getListId() + SEPARATE_KEY +
                    place.getItemId() + SEPARATE_KEY +
                    place.getListName() + SEPARATE_KEY +
                    place.getLocation() + SEPARATE_KEY +
                    color;/*+ SEPARATE_KEY +
                    place.getLatitude();*/

            if (sb.length() <= 100) {
                geofenceList.add(new Geofence.Builder()
                        // Set the request ID of the geofence. This is a string to identify this
                        // geofence.
                        .setRequestId(sb)
                        .setCircularRegion(
                                place.getLatitude(),
                                place.getLongitude(),
                                (float) rangeInMeter
                        )
                        .setNotificationResponsiveness(5)
                        .setExpirationDuration(Geofence.NEVER_EXPIRE)
                        .setTransitionTypes(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                        .build());
            }
        }

        return geofenceList;
    }

    private void sortList() {
        Collections.sort(mSavedItemsList, new Comparator<GetProximityResponse.Result>() {
            @Override
            public int compare(GetProximityResponse.Result o1, GetProximityResponse.Result o2) {
                return Integer.compare((int) o1.getDistanceFromCurrentLoc(), (int) o2.getDistanceFromCurrentLoc());
                //return (o1.getDistanceFromCurrentLoc() > o2.getDistanceFromCurrentLoc() ? 1 : 0);
            }
        });
    }

    private void removeItemsBasedOnLocationName() {

        Set<String> attributes = new HashSet<String>();
        Set<GetProximityResponse.Result> needToAddAgain = new HashSet<>();
        /* All confirmed duplicates go in here */
        List duplicates = new ArrayList<GetProximityResponse.Result>();

        for (GetProximityResponse.Result x : mSavedItemsList) {
            if (attributes.contains(x.getLocation())) {
                duplicates.add(x);
                needToAddAgain.add(x);
            }
            attributes.add(x.getLocation());
        }
        /* Clean list without any dups */
        mSavedItemsList.removeAll(duplicates);

        Set<String> visitedItems = new HashSet<>();
        for (GetProximityResponse.Result x : needToAddAgain) {
            if (!visitedItems.contains(x.getLocation())) {
                visitedItems.add(x.getLocation());
                mSavedItemsList.add(x);
            }

        }

    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setSound(null, null);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void createAndShowForegroundNotification() {
        Intent intent = new Intent(this, MainActivity.class);

        // Intent for opening notification screen on click of action button
        Intent actionIntent = new Intent(this, MainActivity.class);
        actionIntent.setAction(ACTION_NOTIFICATION);
        PendingIntent actionPendingIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Building notification here
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        mBuilder.setSmallIcon(R.mipmap.ic_stat_honey);
        mBuilder.setContentTitle("Proximity is enabled");
        mBuilder.setContentText("Tap to launch application");
        mBuilder.setAutoCancel(false);
        mBuilder.setContentIntent(pIntent);
        mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);

        // mBuilder.addAction(R.drawable.ic_proxmity_notification, "Notification", actionPendingIntent);

        // Launch notification
        startForeground(NOTIFICATION_ID, mBuilder.build());
    }

    // Broadcast Receiver

    class ItemCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() == AppConstants.ACTION_ITEM_COMPLETE) {
                if (intent.hasExtra("request_id")) {
                    final ArrayList<String> requestIds = intent.getStringArrayListExtra("request_id");
                    mGeofencingClient.removeGeofences(requestIds)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    String msg = " Geo-fence Item (" + requestIds.get(0) + ") removed successfully";
                                    Timber.d(msg);
                                    Crashlytics.log(TAG + msg);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    String msg = " Geo-fence Item (" + requestIds.get(0) + ") is not removed";
                                    Timber.d(msg);
                                    Crashlytics.log(TAG + msg);
                                }
                            });
                }
            }
        }
    }
}
