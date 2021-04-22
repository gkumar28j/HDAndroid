package com.mcn.honeydew.ui.settings.editEmail;

import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.ui.base.MvpView;

public interface EditEmailMvpView extends MvpView {

    void dismissDialog();

    void showUserEmail(UserDetailResponse userData);

    void refreshData();

    void onOTPReceived();

}
