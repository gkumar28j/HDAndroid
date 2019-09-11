package com.mcn.honeydew.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.JobIntentService;

import com.google.gson.Gson;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.GetProximityResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.utils.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class ProximityJobIntentService extends JobIntentService {
    public static final String SEPERATE_KEY = "_@#";
    static final int JOB_ID = 571;
    public static final String KEY_DELETE_REQUIRED = "key_delete_required";
    private static final String TAG = "Proximity";

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
        enqueueWork(context, ProximityJobIntentService.class, JOB_ID, work);
    }

    @SuppressLint("CheckResult")
    @Override
    protected void onHandleWork(@NonNull Intent intent) {

        if (intent.hasExtra(KEY_DELETE_REQUIRED)) {
            if (intent.getBooleanExtra(KEY_DELETE_REQUIRED, false)) {
                removeAllProximity();
                return;
            }
        }

        if (!NetworkUtils.isNetworkConnected(getApplicationContext()))
            return;

        // Checking proximity setting to be enabled
        if (mDataManager.getUserData() != null && !mDataManager.getUserData().isIsProximityNotification()) {
            return;
        }

        mDataManager.doGetProximityResponseCall()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<GetProximityResponse>() {

                    @Override
                    public void accept(GetProximityResponse response) throws Exception {

                        if (response.getErrorObject().getStatus() == 1) {

                            List<GetProximityResponse.Result> allItems = new ArrayList<>(response.getResult());
                            if (!allItems.isEmpty()) {
                                List<GetProximityResponse.Result> noRepeatItems = new ArrayList<>();

                                for (GetProximityResponse.Result item : allItems) {
                                    boolean isFound = false;
                                    for (GetProximityResponse.Result i : noRepeatItems) {
                                        if (i.equals(item)) {
                                            isFound = true;
                                            break;
                                        }
                                    }

                                    if (!isFound) noRepeatItems.add(item);
                                }

                                String itemsString = new Gson().toJson(noRepeatItems);
                                mDataManager.saveProximityItems(itemsString);
                                mDataManager.setIsItemAdded(true);
                            }
                        }
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


    public void removeAllProximity() {
        mDataManager.saveProximityItems("[]");
        mDataManager.setIsItemAdded(false);
        //removeGeofences();
    }


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

}
