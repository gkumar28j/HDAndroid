package com.mcn.honeydew.ui.login;

import com.mcn.honeydew.ui.base.MvpView;



/**
 * Created by amit on 16/2/18.
 */

public interface LoginMvpView extends MvpView {

    void openMainActivity();

    void openPhoneVerificationActivity(String primaryMobile);

    void openForgotPasswordActivity();

    void openTourActivity();

    void verifyEmail(String email);

    void onOTPSendSuccess(String email);

}
