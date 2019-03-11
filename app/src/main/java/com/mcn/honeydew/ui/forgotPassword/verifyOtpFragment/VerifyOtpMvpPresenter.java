package com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface VerifyOtpMvpPresenter<V extends VerifyOtpMvpView> extends MvpPresenter<V> {
    void verifyOtp(String authenticationDetail, String otp);
}

