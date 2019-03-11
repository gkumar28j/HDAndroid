package com.mcn.honeydew.di.component;

import android.app.Application;
import android.content.Context;

import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.di.ApplicationContext;
import com.mcn.honeydew.di.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by amit on 14/2/18.
 */

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(HoneyDewApp app);

    @ApplicationContext
    Context context();

    Application application();

    DataManager getDataManager();
}
