package com.mcn.honeydew.ui.settings;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface SettingsMvpPresenter<V extends SettingsMvpView> extends MvpPresenter<V> {
    void onViewPrepared();

    void onLogoutClick();

    void onUpdateUserClick();

    void onUpdatePasswordClicked();

    void onUpdateEmail();


    // for bluetooth and proximity

   // void onViewPrepared();

    void onProximitySettingClick();

    void refreshSettings();

    void saveBluetoothData(boolean value);

    void toggleBluetoothSwitch();

    boolean isItemAdded();

    // reminder for expiring and expired

    void saveDailyExpiringItems(boolean value);

    void saveDailyExpiredItems(boolean value);




}
