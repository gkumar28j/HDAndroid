package com.mcn.honeydew.ui.forgotPassword.locateAccountFragment;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface LocateAccountMvpPresenter<V extends LocateAccountMvpView> extends MvpPresenter<V> {
    void locateAccount(String emailOrPhone);
}

