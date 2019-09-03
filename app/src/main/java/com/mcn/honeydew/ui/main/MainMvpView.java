package com.mcn.honeydew.ui.main;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 16/2/18.
 */

public interface MainMvpView extends MvpView {

    void showMyListFragment(MyHomeListData data);

    void openLoginActivity();

    void onResumeSuccess(boolean isJustLoggedIn);

    void checkProximityPermission();

    void onBluetoothFoundConnected();

    void onNotificationFetched(int count);

}