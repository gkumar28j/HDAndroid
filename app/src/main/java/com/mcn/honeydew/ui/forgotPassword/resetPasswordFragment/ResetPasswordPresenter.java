package com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment;

import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.ResetPasswordRequest;
import com.mcn.honeydew.data.network.model.response.ResetPasswordResponse;
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

public class ResetPasswordPresenter<V extends ResetPasswordMvpView> extends BasePresenter<V> implements ResetPasswordMvpPresenter<V> {

    private static final String TAG = ResetPasswordPresenter.class.getSimpleName();

    @Inject
    public ResetPasswordPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void resetPassword(String authentication, String newPassword, String confirmPassword) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(newPassword)) {
            getMvpView().onError("New password can not be blank.");
            return;
        }

        if (TextUtils.isEmpty(confirmPassword)) {
            getMvpView().onError("Confirm password can not be blank.");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            getMvpView().onError("New password and confirm password must be same.");
            return;
        }

        getMvpView().showLoading();

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmailOrNumber(authentication);
        request.setNewPassword(newPassword);
        request.setConfirmPassword(confirmPassword);

        getDataManager().doResetPasswordCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ResetPasswordResponse>() {
                    @Override
                    public void accept(ResetPasswordResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getResult() == 1) {
                            getMvpView().onPasswordResetSuccess();
                        } else
                            getMvpView().onError(response.getErrorObject().getErrorMessage());


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
