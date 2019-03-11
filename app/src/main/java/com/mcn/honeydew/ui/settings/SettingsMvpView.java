package com.mcn.honeydew.ui.settings;

import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 20/2/18.
 */

public interface SettingsMvpView extends MvpView {

    void setUserData(UserDetailResponse userData);

    void openLoginActivity();

    void showEditNameDialog();

    void showChangePasswordActivity();

    void onLogoutSuccess();

    void onLogoutFailed();
}
