package com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment;

import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.VerifyOtpRequest;
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

public class VerifyOtpPresenter<V extends VerifyOtpMvpView> extends BasePresenter<V> implements VerifyOtpMvpPresenter<V> {

    private static final String TAG = VerifyOtpPresenter.class.getSimpleName();

    @Inject
    public VerifyOtpPresenter(DataManager dataManager,
                              SchedulerProvider schedulerProvider,
                              CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

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

        VerifyOtpRequest request = new VerifyOtpRequest(authenticationDetail, otp);

        getDataManager().doVerifyOtpCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<VerifyOtpResponse>() {
                    @Override
                    public void accept(VerifyOtpResponse response) throws Exception {
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


}
