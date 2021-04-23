package com.mcn.honeydew.ui.my_account;

import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.ui.base.MvpView;

public interface MyAccountMvpView extends MvpView {

    void onLoadDataSuccess(UserDetailResponse userData, boolean facebookLogin);

    void showEditNameDialog(boolean facebookLogin);

  //  void showChangePasswordActivity();

    void onLogoutSuccess();

    void onLogoutFailed();

    void showEditEmailDialog();

    void onEmailUpdatedSuccess();

}
