package com.mcn.honeydew.ui.login;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.LoginResponse;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.EmailUpdateNewRequest;
import com.mcn.honeydew.data.network.model.request.FacebookLoginRequest;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.data.network.model.response.FacebookLoginResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;

;


/**
 * Created by amit on 16/2/18.
 */

public class LoginPresenter<V extends LoginMvpView> extends BasePresenter<V> implements LoginMvpPresenter<V> {

    @Inject
    public LoginPresenter(DataManager dataManager,
                          SchedulerProvider schedulerProvider,
                          CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onServerLoginClick(String email, String password) {
        //validate email and password
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            getMvpView().onError(R.string.error_empty_email);
            return;
        }

        if (TextUtils.isEmpty(password)) {
            getMvpView().onError(R.string.error_empty_password);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doServerLoginApiCall(email, "password", password)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .flatMap(new Function<LoginResponse, ObservableSource<UserDetailResponse>>() {
                    @Override
                    public ObservableSource<UserDetailResponse> apply(LoginResponse loginResponse) throws Exception {
                        if (loginResponse.getError() == null) {
                            getDataManager().getApiHeader().setmAccessToken(loginResponse.getAccessToken());
                            getDataManager().getApiHeader().setmRefreshToken(loginResponse.getRefreshToken());
                            getDataManager().getApiHeader().setmTokenType(loginResponse.getTokenType());
                            getDataManager().updateUserCredential(
                                    loginResponse.getAccessToken(),
                                    loginResponse.getRefreshToken(),
                                    loginResponse.getTokenType());

                            getDataManager().setIsFacebookLogin(false);

                            enableFCM();
                        } else {
                            if (isViewAttached()) {
                                getMvpView().onError(loginResponse.getError());
                            }
                        }

                        return getDataManager().doUserDetailsApiCall()
                                .subscribeOn(getSchedulerProvider().io())
                                .observeOn(getSchedulerProvider().ui());
                    }
                })
                .subscribeWith(new DefaultObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {

                        if (!isViewAttached()) return;

                        getDataManager().updateUserData(userDetailResponse, DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                        getMvpView().hideLoading();

                        if (userDetailResponse.getIsPhoneVerified() == 1) {
                            getDataManager().setUserData(userDetailResponse);

                            if (!userDetailResponse.isEmailVerified()) {
                                getMvpView().verifyEmail(userDetailResponse.getPrimaryEmail());
                                return;
                            }

                            if (getDataManager().isFirstTimeLoggedIn()) {
                                getMvpView().openTourActivity();
                            } else {
                                getMvpView().openMainActivity();
                            }


                        } else
                            getMvpView().openPhoneVerificationActivity(userDetailResponse.getPrimaryMobile());

                    }

                    @Override
                    public void onError(Throwable e) {
                        handleApiError(e);

                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onComplete() {

                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();
                    }
                });


    }


    @SuppressLint("CheckResult")
    @Override
    public void onFacebookLoginClick(FacebookLoginRequest loginRequest) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();


        getDataManager().doLoginWithFacebookApiCall(loginRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .flatMap(new Function<FacebookLoginResponse, ObservableSource<UserDetailResponse>>() {
                    @Override
                    public ObservableSource<UserDetailResponse> apply(FacebookLoginResponse loginResponse) throws Exception {

                        getDataManager().getApiHeader().setmAccessToken(loginResponse.getAccessToken());
                        getDataManager().getApiHeader().setmRefreshToken(loginResponse.getRefreshToken());
                        getDataManager().getApiHeader().setmTokenType(loginResponse.getTokenType());
                        getDataManager().updateUserCredential(
                                loginResponse.getAccessToken(),
                                loginResponse.getRefreshToken(),
                                loginResponse.getTokenType());

                        getDataManager().setIsFacebookLogin(true);

                        return getDataManager().doUserDetailsApiCall()
                                .subscribeOn(getSchedulerProvider().io())
                                .observeOn(getSchedulerProvider().ui());
                    }
                })
                .subscribeWith(new DefaultObserver<UserDetailResponse>() {
                    @Override
                    public void onNext(UserDetailResponse userDetailResponse) {

                        if (!isViewAttached()) return;

                        getDataManager().updateUserData(userDetailResponse, DataManager.LoggedInMode.LOGGED_IN_MODE_SERVER);
                        getMvpView().hideLoading();

                        if (userDetailResponse.getIsPhoneVerified() == 1) {
                            getMvpView().openMainActivity();
                            enableFCM();
                        } else
                            getMvpView().openPhoneVerificationActivity(userDetailResponse.getPrimaryMobile());

                    }

                    @Override
                    public void onError(Throwable e) {
                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();
                    }

                    @Override
                    public void onComplete() {
                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();
                    }
                });

    }

    @Override
    public void onForgotPasswordClicked() {
        getMvpView().openForgotPasswordActivity();
    }

    @SuppressLint("CheckResult")
    @Override
    public void resendOTP(String email) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();


        EmailUpdateNewRequest request = new EmailUpdateNewRequest();
        request.setEmail(email);
        request.setFacebookEmail("");
        if (getDataManager().isFacebookLogin()) {
            request.setIsFacebookLogin(1);
        } else {
            request.setIsFacebookLogin(0);
        }
     //   getMvpView().showLoading();

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

                        if (response.getResult().getStatus() == 1) {
                            getMvpView().onOTPSendSuccess(email);
                        } else {
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


    private void enableFCM() {
        // Enable FCM via enable Auto-init service which generate new token and receive in FCMService
        //FirebaseMessaging.getInstance().setAutoInitEnabled(true);

        //    String token = FirebaseInstanceId.getInstance().getToken();

        Log.e("enabledFCM", "yes");
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);

    }
}
