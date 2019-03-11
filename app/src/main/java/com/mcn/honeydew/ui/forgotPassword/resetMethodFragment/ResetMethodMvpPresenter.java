package com.mcn.honeydew.ui.forgotPassword.resetMethodFragment;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ResetMethodMvpPresenter<V extends ResetMethodMvpView> extends MvpPresenter<V> {
    void onContinueClicked(String authentication, String email, int isEmail);
}

