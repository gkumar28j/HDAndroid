package com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ResetPasswordMvpPresenter<V extends ResetPasswordMvpView> extends MvpPresenter<V> {
    void resetPassword(String email, String newPassword, String confirmPassword);
}

