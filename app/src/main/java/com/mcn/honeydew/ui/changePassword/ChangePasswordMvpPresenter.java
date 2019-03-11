package com.mcn.honeydew.ui.changePassword;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ChangePasswordMvpPresenter<V extends ChangePasswordMvpView> extends MvpPresenter<V> {
    void onSubmitClicked(String oldPassword, String newPassword, String confirmPassword);
}

