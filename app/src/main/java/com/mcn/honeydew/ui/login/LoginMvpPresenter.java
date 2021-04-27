package com.mcn.honeydew.ui.login;

import com.mcn.honeydew.data.network.model.request.FacebookLoginRequest;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface LoginMvpPresenter<V extends LoginMvpView> extends MvpPresenter<V> {

    void onServerLoginClick(String email, String password);

    void onFacebookLoginClick(FacebookLoginRequest loginRequest);

    void onForgotPasswordClicked();

    void resendOTP(String email);

}