package com.mcn.honeydew.ui.settings.editname;

import android.text.TextUtils;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.UpdateUserNameRequest;
import com.mcn.honeydew.data.network.model.response.UpdateUserResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 23/2/18.
 */

public class EditNameDialogPresenter<V extends EditNameDialogMvpView> extends BasePresenter<V>
        implements EditNameDialogMvpPresenter<V> {

    public static final String TAG = "EditNameDialogPresenter";


    @Inject
    public EditNameDialogPresenter(DataManager dataManager,
                                   SchedulerProvider schedulerProvider,
                                   CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onNameSubmitted(final String name) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        if (TextUtils.isEmpty(name)) {
            getMvpView().showMessage(R.string.error_empty_first_name);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doUpdateUserName(new UpdateUserNameRequest(name))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<UpdateUserResponse>() {
                    @Override
                    public void accept(UpdateUserResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().showMessage(response.getMessage());
                        UserDetailResponse data = getDataManager().getUserData();
                        data.setUserName(name);
                        getDataManager().setUserData(data);

                        getMvpView().hideLoading();
                        getMvpView().refreshData();
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
        getMvpView().showUserName(getDataManager().getUserData());
    }
}
