package com.mcn.honeydew.ui.notification_settings;

import com.mcn.honeydew.data.network.model.request.SetNotificationPrefRequest;
import com.mcn.honeydew.ui.base.MvpPresenter;

public interface SystemNotificationSettingMvpPresenter<V extends SystemNotificationSettingsMvpView> extends MvpPresenter<V> {

    void getNotificationPrefrences();

    void setNotificationPref(String data);


}
