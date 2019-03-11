package com.mcn.honeydew.ui.changePassword;

import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.ChangePasswordRequest;
import com.mcn.honeydew.data.network.model.response.ChangePasswordResponse;
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

public class ChangePasswordPresenter<V extends ChangePasswordMvpView> extends BasePresenter<V> implements ChangePasswordMvpPresenter<V> {

    private static final String TAG = ChangePasswordPresenter.class.getSimpleName();

    @Inject
    public ChangePasswordPresenter(DataManager dataManager,
                                   SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onSubmitClicked(String oldPassword, String newPassword, String confirmPassword) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(oldPassword)) {
            getMvpView().onError("Old password can not be blank.");
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

        final ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword);
        request.setNewPassword(newPassword);
        request.setConfirmPassword(confirmPassword);

        getDataManager().doChangePasswordCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ChangePasswordResponse>() {
                    @Override
                    public void accept(ChangePasswordResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().showMessage(response.getResult().getMessage());
                            getMvpView().onPasswordChanged();
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
