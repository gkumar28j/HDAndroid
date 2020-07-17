package com.mcn.honeydew.ui.verify_email;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class VerifyEmailPresenter<V extends VerifyEmailMvpView> extends BasePresenter<V> implements VerifyEmailMvpPresenter<V> {


    @Inject
    public VerifyEmailPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void verifyEmailToCode(String email) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();


        getDataManager().doNotificationSettingsCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<NotificationSettingsResponse>() {
                    @Override
                    public void accept(NotificationSettingsResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().emailVerifiedSuccessfully(false);
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });
    }

    @Override
    public void verifyOTP(String otp) {

    }


}
