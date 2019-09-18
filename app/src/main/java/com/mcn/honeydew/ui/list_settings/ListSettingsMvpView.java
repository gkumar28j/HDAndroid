package com.mcn.honeydew.ui.list_settings;

import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 20/2/18.
 */

public interface ListSettingsMvpView extends MvpView {
    void onListSettingFetched(GetListSettingsResponse.ListSettings settings);
    void toggleChanges(boolean toggle);
}
