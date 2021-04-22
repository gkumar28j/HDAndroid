package com.mcn.honeydew.ui.verify_email.verifyEmailOtpFragment;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface VerifyEmailOtpMvpPresenter<V extends VerifyEmailOtpMvpView> extends MvpPresenter<V> {
    void verifyOtp(String authenticationDetail, String otp);

    void resendOTP(String email);
}

