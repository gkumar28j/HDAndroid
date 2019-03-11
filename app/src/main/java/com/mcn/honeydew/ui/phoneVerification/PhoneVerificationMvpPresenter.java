package com.mcn.honeydew.ui.phoneVerification;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by Ashish on 21/2/18.
 */

@PerActivity
public interface PhoneVerificationMvpPresenter<V extends PhoneVerificationMvpView> extends MvpPresenter<V> {
    void fetchCountries();

    void getVerificationCode(int callType, String phoneNumber, String countryCode);

    void updateVerificationStatus(int status, String phoneNumber, String countryCode);

}

