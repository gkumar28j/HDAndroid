package com.mcn.honeydew.ui.verify_email.verifyEmailOtpFragment;

import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface VerifyEmailOtpMvpView extends MvpView {
    void onVerified();

    void onOTPReceived();
}
