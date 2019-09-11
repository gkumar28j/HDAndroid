package com.mcn.honeydew.di.component;

import com.mcn.honeydew.di.PerService;
import com.mcn.honeydew.di.module.ServiceModule;
import com.mcn.honeydew.services.BluetoothJobIntentService;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.services.GeofenceTransitionsJobIntentService;
import com.mcn.honeydew.services.MyFirebaseMessagingService;
import com.mcn.honeydew.services.ProximityJobIntentService;

import dagger.Component;

/**
 * Created by amit on 16/3/18.
 */

@PerService
@Component(dependencies = ApplicationComponent.class, modules = ServiceModule.class)
public interface ServiceComponent {

  //  void inject(MyFirebaseInstanceIDService service);

    void inject(BluetoothJobIntentService service);

    void inject(MyFirebaseMessagingService service);

    void inject(ProximityJobIntentService service);

    void inject(GeofenceTransitionsJobIntentService service);

    void inject(GeoFenceFilterService service);

}
