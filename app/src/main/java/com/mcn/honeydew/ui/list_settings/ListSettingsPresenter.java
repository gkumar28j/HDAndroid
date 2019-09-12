package com.mcn.honeydew.ui.list_settings;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.UpdateListSettingsRequest;
import com.mcn.honeydew.data.network.model.response.GetListSettingsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 20/2/18.
 */

public class ListSettingsPresenter<V extends ListSettingsMvpView> extends BasePresenter<V> implements ListSettingsMvpPresenter<V> {

    @Inject
    public ListSettingsPresenter(DataManager dataManager,
                                 SchedulerProvider schedulerProvider,
                                 CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getListSettings(int listId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        getDataManager().doGetListSettings(listId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GetListSettingsResponse>() {
                    @Override
                    public void accept(GetListSettingsResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();

                        if (response != null) {
                            GetListSettingsResponse.ListSettings listSettings = response.getResult()[0];
                            getMvpView().onListSettingFetched(listSettings);
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateSettings(int listId, boolean status) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getDataManager().setInProgressValue(status);

        int userProfileId = getDataManager().getUserData().getUserProfileId();
        UpdateListSettingsRequest request = new UpdateListSettingsRequest(listId, userProfileId, status);

        getDataManager().doUpdateListSettings(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<Void>() {
                    @Override
                    public void accept(Void aVoid) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


/*
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

                        getDataManager().setUserAsLoggedOut();
                        getMvpView().hideLoading();
                        getMvpView().openLoginActivity();

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
                        getMvpView().openLoginActivity();

                        // handle the login error here

                    }
                });


    }*/


}
