package com.mcn.honeydew.ui.notifications.settings;

import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 28/2/18.
 */

public interface NotificationSettingsMvpView extends MvpView {
    void setProximitySettings(NotificationSettingsResponse proximitySettings);

    void updateProximityRange(String proximityRange);

    void onProximityRangeUpdated();

    //void onProximityItemLoaded(List<GetProximityResponse.Result> items);
}
