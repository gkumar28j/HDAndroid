package com.mcn.honeydew.ui.forgotPassword.resetMethodFragment;

import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface ResetMethodMvpView extends MvpView {
    void onOtpSent(String verificationCode, String emailOrPhone);
}
