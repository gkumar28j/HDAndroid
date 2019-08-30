package com.mcn.honeydew.ui.settings;

import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 20/2/18.
 */

public interface SettingsMvpView extends MvpView {

    void setUserData(UserDetailResponse userData, boolean facebookLogin);

    void openLoginActivity();

    void showEditNameDialog(boolean facebookLogin);

    void showChangePasswordActivity();

    void onLogoutSuccess();

    void onLogoutFailed();

    void showEditEmailDialog();


    // for other notification setting like bluetooth and proximity

    void openProximitySettingView();

    void setProximityNotification(NotificationSettingsResponse results);

    void toggleBlueToothButton(boolean val);


    // for daily reminder items




}
