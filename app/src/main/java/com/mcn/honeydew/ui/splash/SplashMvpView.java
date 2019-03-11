package com.mcn.honeydew.ui.splash;


import com.mcn.honeydew.ui.base.MvpView;

public interface SplashMvpView extends MvpView {

    void openLoginActivity();

    void openMainActivity();

    void openPhoneVerificationActivity(String primaryMobile);
}
