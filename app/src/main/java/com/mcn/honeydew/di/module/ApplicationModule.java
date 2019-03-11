package com.mcn.honeydew.di.module;

import android.app.Application;
import android.content.Context;

import com.mcn.honeydew.HoneyDewAppLifecycleObserver;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.AppDataManager;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.db.AppDbHelper;
import com.mcn.honeydew.data.db.DbHelper;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.ApiHeader;
import com.mcn.honeydew.data.network.ApiHelper;
import com.mcn.honeydew.data.network.ApiInterceptor;
import com.mcn.honeydew.data.network.AppApiHelper;
import com.mcn.honeydew.data.pref.AppPreferencesHelper;
import com.mcn.honeydew.data.pref.PreferencesHelper;
import com.mcn.honeydew.di.ApiInfo;
import com.mcn.honeydew.di.ApplicationContext;
import com.mcn.honeydew.di.DatabaseInfo;
import com.mcn.honeydew.di.PreferenceInfo;
import com.mcn.honeydew.utils.AppConstants;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by amit on 14/2/18.
 */
@Module
public class ApplicationModule {


    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    @ApiInfo
    String provideApiKey() {
        return AppConstants.API_KEY;
    }

    @Provides
    @PreferenceInfo
    String providePreferenceName() {
        return AppConstants.PREF_NAME;
    }

    @Provides
    @DatabaseInfo
    String provideDatabaseName() {
        return AppConstants.DB_NAME;
    }

    @Provides
    @DatabaseInfo
    Integer provideDatabaseVersion() {
        return AppConstants.DB_VERSION;
    }

    @Provides
    @Singleton
    HoneyDewAppLifecycleObserver provideLifecycleObserver() {
        return new HoneyDewAppLifecycleObserver(mApplication);
    }

    @Provides
    @Singleton
    DataManager provideDataManager(AppDataManager appDataManager) {
        return appDataManager;
    }

    @Provides
    @Singleton
    DbHelper provideDbHelper(AppDbHelper appDbHelper) {
        return appDbHelper;
    }

    @Provides
    @Singleton
    PreferencesHelper providePreferencesHelper(AppPreferencesHelper appPreferencesHelper) {
        return appPreferencesHelper;
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(AppApiHelper appApiHelper) {
        return appApiHelper;
    }

    @Provides
    @Singleton
    ApiCall provideApiCall(ApiInterceptor apiInterceptor) {
        return ApiCall.Factory.create(apiInterceptor);
    }

    @Provides
    @Singleton
    ApiHeader provideApiHeader(@ApiInfo String apiKey, PreferencesHelper preferencesHelper) {
        return new ApiHeader(apiKey, preferencesHelper.getAccessToken(), preferencesHelper.getRefreshToken(), preferencesHelper.getTokenType());
    }

    @Provides
    @Singleton
    CalligraphyConfig provideCalligraphyDefaultConfig() {
        return new CalligraphyConfig.Builder()
                .setDefaultFontPath("Lato/Lato-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build();
    }
}
