package com.mcn.honeydew.ui.notifications.settings;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 28/2/18.
 */

@PerActivity
public interface NotificationSettingsMvpPresenter<V extends NotificationSettingsMvpView> extends MvpPresenter<V> {

    void onViewPrepared();

    void updateProximitySettings(int notificationStatus, int notificationType);

    void updateProximityRange(String range, int position);

    //void getProximityItems();

    //void clearAllProximityItems();
}

