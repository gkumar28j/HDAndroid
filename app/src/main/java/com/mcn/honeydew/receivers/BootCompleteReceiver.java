package com.mcn.honeydew.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.data.DataManager;

public class BootCompleteReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w("BootCompleteReceiver", "Boot complete event occurred");

        DataManager dataManager = HoneyDewApp.get(context).getDataManager();

        if (dataManager == null) {
            Log.w("BootCompleteReceiver", "data manager is null");
            return;
        } else {
            // Checking if user logged out
            if (dataManager.getCurrentUserLoggedInMode()
                    == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) {
                Log.w("BootCompleteReceiver", "User is logged out");
                return;
            } else {
                // Checking if proximity is enabled
                if (dataManager.getCurrentUserLoggedInMode()
                        == DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()
                        && dataManager.getUserData().isIsProximityNotification()) {

                    // Checking OS version to start service from background
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                       // context.startForegroundService(GeoFenceFilterService.getStartIntent(context));
                        Log.w("BootCompleteReceiver", "foreground service started");
                    } else {
                        //context.startService(GeoFenceFilterService.getStartIntent(context));
                        Log.w("BootCompleteReceiver", "service is started");
                    }
                } else {
                    Log.w("BootCompleteReceiver", "proximity is disabled");
                }
            }
        }

      /*  if (dataManager == null) return;

        // Checking if user logged out
        if (dataManager.getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_LOGGED_OUT.getType()) return;

        // Checking if proximity is enabled
        if (dataManager.getCurrentUserLoggedInMode()
                == DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER.getType()
                && dataManager.getUserData().isIsProximityNotification()) {

            // Checking OS version to start service from background
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(GeoFenceFilterService.getStartIntent(context));
            } else {
                context.startService(GeoFenceFilterService.getStartIntent(context));
            }
        }*/

    }
}
