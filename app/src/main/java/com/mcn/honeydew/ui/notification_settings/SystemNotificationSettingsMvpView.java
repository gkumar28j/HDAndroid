package com.mcn.honeydew.ui.notification_settings;

import com.mcn.honeydew.data.network.model.response.SystemNotifcationPrefData;
import com.mcn.honeydew.ui.base.MvpView;

public interface SystemNotificationSettingsMvpView extends MvpView {

    void onNotificationPrefFetched(SystemNotifcationPrefData.Result data);

    void onSuccessfullySetPref();

}
