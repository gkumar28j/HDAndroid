package com.mcn.honeydew.ui.main;

import android.content.Context;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface MainMvpPresenter<V extends MainMvpView> extends MvpPresenter<V> {

    void saveSelectedList(MyHomeListData data);

    void onViewPrepared(Context ctx);

    void checkLoginSession();

    boolean isIsProximityNotification();

    void checkBluetoothConnectivity();

    void checkAndCallBluetoothApi();

    void fetchBluetoothItems();
}
