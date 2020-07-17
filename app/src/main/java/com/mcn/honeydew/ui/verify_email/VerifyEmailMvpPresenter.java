package com.mcn.honeydew.ui.verify_email;

import com.mcn.honeydew.ui.base.MvpPresenter;

public interface VerifyEmailMvpPresenter<V extends VerifyEmailMvpView> extends MvpPresenter<V> {

    void verifyEmailToCode(String email);

    void verifyOTP(String otp);


}
