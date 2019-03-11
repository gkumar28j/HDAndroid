package com.mcn.honeydew.ui.settings.editname;

import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.ui.base.DialogMvpView;

/**
 * Created by amit on 23/2/18.
 */

public interface EditNameDialogMvpView extends DialogMvpView {


    void dismissDialog();

    void showUserName(UserDetailResponse userData);

    void refreshData();
}

