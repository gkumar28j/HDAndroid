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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.model.request.UpdateDeviceInfoRequest;
import com.mcn.honeydew.data.network.model.response.UpdateDeviceInfoResponse;
import com.mcn.honeydew.di.component.DaggerServiceComponent;
import com.mcn.honeydew.di.component.ServiceComponent;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.CommonUtils;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";

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
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("FCM Token: " + refreshedToken);

        if (mDataManager.getCurrentUserLoggedInMode() ==
                DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()) {
            // If you want to send messages to this application instance or
            // manage this apps subscriptions on the server side, send the
            // Instance ID token to your app server.
            sendRegistrationToServer(refreshedToken);
        } else {

            mDataManager.saveDeviceId(refreshedToken);
            mDataManager.sendDeviceIdToServer(false);

        }


    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
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
