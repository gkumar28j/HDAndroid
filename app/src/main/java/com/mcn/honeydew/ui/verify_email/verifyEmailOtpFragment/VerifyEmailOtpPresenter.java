package com.mcn.honeydew.ui.verify_email.verifyEmailOtpFragment;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.EmailUpdateNewRequest;
import com.mcn.honeydew.data.network.model.request.VerifyNewEmailOTPRequest;
import com.mcn.honeydew.data.network.model.request.VerifyOtpRequest;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.data.network.model.response.VerifyOtpResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;


/**
 * Created by amit on 16/2/18.
 */

public class VerifyEmailOtpPresenter<V extends VerifyEmailOtpMvpView> extends BasePresenter<V> implements VerifyEmailOtpMvpPresenter<V> {

    private static final String TAG = VerifyEmailOtpPresenter.class.getSimpleName();

    @Inject
    public VerifyEmailOtpPresenter(DataManager dataManager,
                                   SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void verifyOtp(String authenticationDetail, String otp) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if(TextUtils.isEmpty(otp))
        {
            getMvpView().onError("Code can not be blank.");
            return;
        }

        getMvpView().showLoading();

        VerifyNewEmailOTPRequest request = new VerifyNewEmailOTPRequest();
        request.setAuthenticateDetail(authenticationDetail);
        request.setOTPCode(otp);

        getDataManager().doVerifyEmailNewOtp(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<EmailUpdateNewResponse>() {
                    @Override
                    public void accept(EmailUpdateNewResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getResult().getStatus() == 1)
                            getMvpView().onVerified();
                        else
                            getMvpView().showMessage(response.getResult().getMessage());


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void resendOTP(String email) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        EmailUpdateNewRequest request = new EmailUpdateNewRequest();
        request.setEmail(email);
        request.setFacebookEmail("");
        if(getDataManager().isFacebookLogin()){
            request.setIsFacebookLogin(1);
        }else {
            request.setIsFacebookLogin(0);
        }
        getMvpView().showLoading();

        getDataManager().resendOTP(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<EmailUpdateNewResponse>() {
                    @Override
                    public void accept(EmailUpdateNewResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getErrorObject().getStatus() != 1) {
                            getMvpView().onError(response.getErrorObject().getErrorMessage());
                            return;
                        }

                        if(response.getResult().getStatus()==1){
                            getMvpView().onOTPReceived();
                        }else {
                            getMvpView().showMessage(response.getResult().getMessage());
                        }


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }
                });
    }


}
