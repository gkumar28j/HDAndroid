package com.mcn.honeydew.ui.my_account;

import com.mcn.honeydew.ui.base.MvpPresenter;

public interface MyAccountMvpPresenter<V extends MyAccountMvpView> extends MvpPresenter<V> {

    public void onViewPrepared();

    void onLogoutClick();

    void onUpdateUserNameClicked();

  //  void onUpdatePasswordClicked();

    void onUpdateEmail();

    void onEmailChanged(String email , boolean isverified);

    void onEmailSubmit(String s);


}
