package com.mcn.honeydew.ui.forgotPassword.resetMethodFragment;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.SendOtpRequest;
import com.mcn.honeydew.data.network.model.response.SendOtpResponse;
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

public class ResetMethodPresenter<V extends ResetMethodMvpView> extends BasePresenter<V> implements ResetMethodMvpPresenter<V> {

    private static final String TAG = ResetMethodPresenter.class.getSimpleName();

    @Inject
    public ResetMethodPresenter(DataManager dataManager,
                                SchedulerProvider schedulerProvider,
                                CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onContinueClicked(final String authentication, String email, int isEmail) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }


        getMvpView().showLoading();

        SendOtpRequest request = new SendOtpRequest();
        request.setAuthenticateDetail(authentication);
        request.setEmail(email);
        request.setIsEmail(isEmail);

        getDataManager().doSendOtpApiCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<SendOtpResponse>() {
                    @Override
                    public void accept(SendOtpResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        SendOtpResponse.Detail detail = response.getDetails();

                        if (response.getErrorObject().getStatus() == 0) {
                            getMvpView().showMessage(response.getErrorObject().getErrorMessage());
                            return;
                        }

                        if (response.getDetails() != null && response.getDetails().getStatus() == 1) {
                            getMvpView().onOtpSent(detail.getOTP(), authentication /*detail.getEmail()*/);
                        }
                        getMvpView().showMessage(detail.getMessage());


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
