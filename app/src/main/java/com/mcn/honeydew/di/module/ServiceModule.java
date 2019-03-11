package com.mcn.honeydew.di.module;

import android.app.Service;
import android.content.Context;

import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.mcn.honeydew.di.ApplicationContext;

import dagger.Module;
import dagger.Provides;

/**
 * Created by amit on 16/3/18.
 */

@Module
public class ServiceModule {

    private Service mService;

    public ServiceModule(Service service) {
        mService = service;
    }

    @Provides
    public Service provideService() {
        return this.mService;
    }

//    @Provides
//    GeofencingClient provideGeoFencingClient(Service service) {
//        return LocationServices.getGeofencingClient(service);
//    }
}

