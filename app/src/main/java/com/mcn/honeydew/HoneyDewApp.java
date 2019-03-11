package com.mcn.honeydew;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ApplicationComponent;
import com.mcn.honeydew.di.component.DaggerApplicationComponent;
import com.mcn.honeydew.di.module.ApplicationModule;
import android.arch.lifecycle.ProcessLifecycleOwner;
import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by amit on 14/2/18.
 */

public class HoneyDewApp extends MultiDexApplication {

    @Inject
    DataManager mDataManager;

    @Inject
    CalligraphyConfig mCalligraphyConfig;

    @Inject
    HoneyDewAppLifecycleObserver mLifecycleObserver;

    private ApplicationComponent mApplicationComponent;

    public static HoneyDewApp get(Context context) {
        return (HoneyDewApp) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        } /*else {
            Timber.plant(new ReleaseTree());
        }*/


        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this)).build();

        mApplicationComponent.inject(this);

        ProcessLifecycleOwner.get().getLifecycle().addObserver(mLifecycleObserver);

        CalligraphyConfig.initDefault(mCalligraphyConfig);

        if (mDataManager.getCurrentUserLoggedInMode()
                != DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
            UserDetailResponse res = mDataManager.getUserData();
            if (res != null) {
                Crashlytics.setUserEmail(res.getPrimaryEmail());
                Crashlytics.setUserName(res.getUserName());
            }

        }

    }

    public ApplicationComponent getComponent() {
        return mApplicationComponent;
    }

    public DataManager getDataManager() {
        return mDataManager;
    }
}
