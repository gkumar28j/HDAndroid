package com.mcn.honeydew.ui.my_account;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.LogoutResponse;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.EmailUpdateNewRequest;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class MyAccountPresenter<V extends MyAccountMvpView> extends BasePresenter<V> implements MyAccountMvpPresenter<V> {

    @Inject
    public MyAccountPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onViewPrepared() {
        getMvpView().onLoadDataSuccess(getDataManager().getUserData(), getDataManager().isFacebookLogin());
    }

    @SuppressLint("CheckResult")
    @Override
    public void onLogoutClick() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        getDataManager().doLogoutApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<LogoutResponse>() {
                    @Override
                    public void accept(LogoutResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        disableFCM();

                        getDataManager().setUserAsLoggedOut();
                        getDataManager().deleteAllRecords();
                        getMvpView().hideLoading();
                        getMvpView().onLogoutSuccess();
                        // clearing saved proximity settings


                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getDataManager().setUserAsLoggedOut();
                        getMvpView().hideLoading();
                        getMvpView().onLogoutFailed();

                        // handle the login error here

                    }
                });


    }



    @Override
    public void onUpdateUserNameClicked() {
        getMvpView().showEditNameDialog(getDataManager().isFacebookLogin());
    }

   /* @Override
    public void onUpdatePasswordClicked() {
        getMvpView().showChangePasswordActivity();
    }
*/
    @Override
    public void onUpdateEmail() {

        getMvpView().showEditEmailDialog();

    }

    @Override
    public void onEmailChanged(String email , boolean isVerified) {

        UserDetailResponse data = getDataManager().getUserData();
        data.setPrimaryEmail(email);
        data.setEmailVerified(isVerified);
        getDataManager().setUserData(data);

        getMvpView().onLoadDataSuccess(getDataManager().getUserData(), getDataManager().isFacebookLogin());

    }

    private void disableFCM() {
        // Disable auto init
        //FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Remove InstanceID initiate to unsubscribe all topic
                    // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }).start();
    }


    @SuppressLint("CheckResult")
    @Override
    public void onEmailSubmit(String email) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(email)) {
            getMvpView().showMessage(R.string.error_empty_email);
            return;
        }

        if (!CommonUtils.isEmailValid(email)) {
            getMvpView().showMessage(R.string.error_invalid_email);
            return;
        }
        getMvpView().showLoading();

        EmailUpdateNewRequest request = new EmailUpdateNewRequest();
        request.setEmail("");
        request.setFacebookEmail(email);
        request.setIsFacebookLogin(1);

        getMvpView().showLoading();

        getDataManager().doUpdateEmailNew(request)
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
                            getMvpView().onEmailUpdatedSuccess();
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
