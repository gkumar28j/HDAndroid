package com.mcn.honeydew.ui.forgotPassword;

import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface ForgotPasswordMvpView extends MvpView {
    void showLocateAccountFragment();

    void showResetMethodFragment(LocateAccountResponse.Detail detail);

    void showVerifyCodeFragment(String verificationCode, String emailOrPhone, int isEmail, String lastTwoDigit, String hiddenEmail);

    void showResetPasswordFragment(String mAuthentication);
}
