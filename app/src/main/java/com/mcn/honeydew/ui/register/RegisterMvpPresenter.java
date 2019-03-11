package com.mcn.honeydew.ui.register;

import com.mcn.honeydew.data.network.model.UserModel;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface RegisterMvpPresenter<V extends RegisterMvpView> extends MvpPresenter<V> {

    //void onSignUpClicked(String firstName, String lastName, String email, String phoneNumber, String password, String confirmPassword);
    void onSignUpClicked(UserModel user);

    void fetchCountries();
}

