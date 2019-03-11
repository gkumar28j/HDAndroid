package com.mcn.honeydew.ui.notifications;

import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 20/2/18.
 */

public interface NotificationsMvpView extends MvpView {


    void openProximitySettingView();

    void setProximityNotification(NotificationSettingsResponse results);

    void toggleBlueToothButton(boolean val);
}
