package com.mcn.honeydew.ui.settings.editEmail;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.EmailUpdateNewRequest;
import com.mcn.honeydew.data.network.model.request.UpdateEmailRequest;
import com.mcn.honeydew.data.network.model.request.UpdateUserNameRequest;
import com.mcn.honeydew.data.network.model.response.EmailUpdateNewResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserEmailResponse;
import com.mcn.honeydew.data.network.model.response.UpdateUserResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class EditEmailDialogPresenter<V extends EditEmailMvpView> extends BasePresenter<V> implements EditEmailMvpPresenter<V> {


    @Inject
    public EditEmailDialogPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
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
        request.setEmail(email);
        request.setFacebookEmail("");
        if(getDataManager().isFacebookLogin()){
            request.setIsFacebookLogin(1);
        }else {
            request.setIsFacebookLogin(0);
        }
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
                            getMvpView().refreshData();
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

    @SuppressLint("CheckResult")
    @Override
    public void onResendOTP(String email) {

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

    @Override
    public void onCancelClicked() {
        getMvpView().dismissDialog();
    }

    @Override
    public void onViewPrepared() {

        getMvpView().showUserEmail(getDataManager().getUserData());

    }
}
