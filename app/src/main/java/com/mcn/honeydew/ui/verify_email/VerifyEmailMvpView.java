package com.mcn.honeydew.ui.verify_email;

import com.mcn.honeydew.ui.base.MvpView;

public interface VerifyEmailMvpView extends MvpView {

    void emailVerifiedSuccessfully(boolean value);

    void otpVerifiedSuccess();

}
