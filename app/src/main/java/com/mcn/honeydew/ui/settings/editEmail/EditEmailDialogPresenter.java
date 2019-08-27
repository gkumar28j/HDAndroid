package com.mcn.honeydew.ui.settings.editEmail;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.UpdateEmailRequest;
import com.mcn.honeydew.data.network.model.request.UpdateUserNameRequest;
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

        UpdateEmailRequest request = new UpdateEmailRequest();
        request.setEmail(email);

        getDataManager().doUpdateUserEmail(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<UpdateUserEmailResponse>() {
                    @Override
                    public void accept(UpdateUserEmailResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().showMessage(response.getResult().getMessage());

                        if(response.getResult().getStatus()==1) {
                            UserDetailResponse data = getDataManager().getUserData();
                            data.setPrimaryEmail(email);
                            getDataManager().setUserData(data);
                            getMvpView().refreshData();
                        }
                            getMvpView().hideLoading();
                            getMvpView().dismissDialog();

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        handleApiError(throwable);
                        getMvpView().dismissDialog();

                        // handle the login error here

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
