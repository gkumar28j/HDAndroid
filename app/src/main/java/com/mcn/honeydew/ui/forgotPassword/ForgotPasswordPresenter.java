package com.mcn.honeydew.ui.forgotPassword;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by amit on 16/2/18.
 */

public class ForgotPasswordPresenter<V extends ForgotPasswordMvpView> extends BasePresenter<V> implements ForgotPasswordMvpPresenter<V> {

    private static final String TAG = ForgotPasswordPresenter.class.getSimpleName();

    @Inject
    public ForgotPasswordPresenter(DataManager dataManager,
                                   SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void openLocateAccountFragment() {
        getMvpView().showLocateAccountFragment();
    }

    @Override
    public void openResetMethodFragment(LocateAccountResponse.Detail detail) {
        getMvpView().showResetMethodFragment(detail);
    }

    @Override
    public void openVerifyCodeFragment(String verificationCode, String emailOrPhone, int isEmail, String lastTwoDigit, String hiddenEmail) {
        getMvpView().showVerifyCodeFragment(verificationCode, emailOrPhone, isEmail, lastTwoDigit,hiddenEmail);
    }

    @Override
    public void openResetPasswordFragment(String mAuthentication) {
        getMvpView().showResetPasswordFragment(mAuthentication);
    }
}
