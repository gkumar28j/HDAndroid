package com.mcn.honeydew.ui.notifications;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.BluetoothRequest;
import com.mcn.honeydew.data.network.model.response.BluetoothResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 20/2/18.
 */

public class NotificationsPresenter<V extends NotificationsMvpView> extends BasePresenter<V> implements NotificationsMvpPresenter<V> {

    @Inject
    public NotificationsPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @android.annotation.SuppressLint("CheckResult")
    @Override
    public void onViewPrepared() {
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
                        if (response.getErrorObject().getStatus() == 1) {

                            UserDetailResponse detailResponse = getDataManager().getUserData();
                            detailResponse.setIsProximityNotification(response.getResults().get(0).isProximityNotification());
                            detailResponse.setIsBluetoothNotification(response.getResults().get(0).isBluetoothNotification());

                            getDataManager().setUserData(detailResponse);

                            getDataManager().setProximitySettings(response);
                            getMvpView().setProximityNotification(response);

                        }

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });


    }

    @Override
    public void onProximitySettingClick() {
        getMvpView().openProximitySettingView();
    }

    @Override
    public void refreshSettings() {
        onViewPrepared();
    }

    @Override
    public void saveBluetoothData(boolean value) {

        UserDetailResponse mdata = getDataManager().getUserData();
        mdata.setIsBluetoothNotification(value);
        getDataManager().setUserData(mdata);

        BluetoothRequest request = new BluetoothRequest();
        request.setNotificationType(1);

        if (value) {
            request.setNotificationStatus(1);
        } else {
            request.setNotificationStatus(0);
        }


        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        getDataManager().doGetBluetoothResponse(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<BluetoothResponse>() {
                    @Override
                    public void accept(BluetoothResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        /*getMvpView().hideLoading();
                        if (response.getErrorObject().getStatus() == 1) {

                            if(response.getResult().getMessage().equals("Bluetooth has disconnected.")) {
                                getDataManager().getUserData().setIsBluetoothNotification(false);
                            }else {
                                getDataManager().getUserData().setIsBluetoothNotification(true);

                            }

                        }*/

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }


    @Override
    public void toggleBluetoothSwitch() {

        boolean toggle = getDataManager().getUserData().isIsBluetoothNotification();
        getMvpView().toggleBlueToothButton(toggle);
    }

    @Override
    public boolean isItemAdded() {
        return getDataManager().getIsItemAdded();
    }
}
