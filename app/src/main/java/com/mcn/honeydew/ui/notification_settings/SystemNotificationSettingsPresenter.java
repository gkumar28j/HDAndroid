package com.mcn.honeydew.ui.notification_settings;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.SetNotificationPrefRequest;
import com.mcn.honeydew.data.network.model.response.ResetNotificationCountResponse;
import com.mcn.honeydew.data.network.model.response.SystemNotifcationPrefData;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class SystemNotificationSettingsPresenter<V extends SystemNotificationSettingsMvpView> extends BasePresenter<V> implements SystemNotificationSettingMvpPresenter<V> {

    @Inject
    public SystemNotificationSettingsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getNotificationPrefrences() {

        if (!getMvpView().isNetworkConnected()) {
             getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doGetSystemNotificationPrefData()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<SystemNotifcationPrefData>() {
                    @Override
                    public void accept(SystemNotifcationPrefData response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onNotificationPrefFetched(response.getResult());

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
    public void setNotificationPref(String data) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();
        SetNotificationPrefRequest request = new SetNotificationPrefRequest();
        request.setFrequency(data);


        getDataManager().doSetSystemNotificationPref(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<SystemNotifcationPrefData>() {
                    @Override
                    public void accept(SystemNotifcationPrefData response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getDataManager().setNotificationFilterPref(data);
                        getMvpView().onSuccessfullySetPref();

                      //  getMvpView().onNotificationPrefFetched(response.getResult());

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
