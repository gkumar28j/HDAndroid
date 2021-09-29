package com.mcn.honeydew;

import android.content.Context;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ApplicationComponent;
import com.mcn.honeydew.di.component.DaggerApplicationComponent;
import com.mcn.honeydew.di.module.ApplicationModule;
import androidx.lifecycle.ProcessLifecycleOwner;
import javax.inject.Inject;

import io.fabric.sdk.android.Fabric;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;
import timber.log.Timber;


/**
 * Created by amit on 14/2/18.
 */

public class HoneyDewApp extends MultiDexApplication {

    @Inject
    DataManager mDataManager;

  /*  @Inject
    CalligraphyConfig mCalligraphyConfig;*/

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

        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("Lato/Lato-Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

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
