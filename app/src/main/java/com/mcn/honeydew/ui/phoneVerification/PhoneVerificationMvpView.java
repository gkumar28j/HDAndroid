package com.mcn.honeydew.ui.phoneVerification;

import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by amit on 16/2/18.
 */

public interface PhoneVerificationMvpView extends MvpView {

    void onCountriesFetched(List<Countries> countries);

    void onVerificationCodeSent(String code, boolean verificationStatus);

    void openMainActivity(boolean b);
}
