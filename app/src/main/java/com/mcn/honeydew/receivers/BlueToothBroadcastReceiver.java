package com.mcn.honeydew.receivers;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mcn.honeydew.services.BluetoothJobIntentService;


/**
 * Created by gkumar on 22/3/18.
 */

public class BlueToothBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        BluetoothJobIntentService.enqueueWork(context,intent);
    }
}
