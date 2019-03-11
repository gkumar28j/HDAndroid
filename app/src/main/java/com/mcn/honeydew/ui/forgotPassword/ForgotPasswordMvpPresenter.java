package com.mcn.honeydew.ui.forgotPassword;

import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ForgotPasswordMvpPresenter<V extends ForgotPasswordMvpView> extends MvpPresenter<V> {

    void openLocateAccountFragment();

    void openResetMethodFragment(LocateAccountResponse.Detail detail);

    void openVerifyCodeFragment(String verificationCode, String emailOrPhone, int isEmail, String lastTwoDigit, String hiddenEmail);

    void openResetPasswordFragment(String mAuthentication);
}

