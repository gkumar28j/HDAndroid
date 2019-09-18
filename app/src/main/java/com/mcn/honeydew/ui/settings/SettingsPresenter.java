package com.mcn.honeydew.ui.settings;

import android.annotation.SuppressLint;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.LogoutResponse;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.BluetoothRequest;
import com.mcn.honeydew.data.network.model.request.DailyReminderExpiringRequest;
import com.mcn.honeydew.data.network.model.response.BluetoothResponse;
import com.mcn.honeydew.data.network.model.response.DailyReminderExpiredResponse;
import com.mcn.honeydew.data.network.model.response.DailyReminderExpiringResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 20/2/18.
 */

public class SettingsPresenter<V extends SettingsMvpView> extends BasePresenter<V> implements SettingsMvpPresenter<V> {

    @Inject
    public SettingsPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared() {

        getNotificationSettings();
        getMvpView().setUserData(getDataManager().getUserData(), getDataManager().isFacebookLogin());

    }

    @SuppressLint("CheckResult")
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

                        disableFCM();

                        getDataManager().setUserAsLoggedOut();
                        getDataManager().deleteAllRecords();
                        getMvpView().hideLoading();
                        getMvpView().onLogoutSuccess();
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
                        getMvpView().onLogoutFailed();

                        // handle the login error here

                    }
                });


    }

    @Override
    public void onUpdateUserClick() {
        getMvpView().showEditNameDialog(getDataManager().isFacebookLogin());
    }

    @Override
    public void onUpdatePasswordClicked() {
        getMvpView().showChangePasswordActivity();
    }

    @Override
    public void onUpdateEmail() {

        getMvpView().showEditEmailDialog();

    }

    @Override
    public void onProximitySettingClick() {
        getMvpView().openProximitySettingView();
    }

    @Override
    public void refreshSettings() {
        onViewPrepared();
    }

    @SuppressLint("CheckResult")
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



    private void disableFCM() {
        // Disable auto init
        //FirebaseMessaging.getInstance().setAutoInitEnabled(false);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Remove InstanceID initiate to unsubscribe all topic
                    // TODO: May be a better way to use FirebaseMessaging.getInstance().unsubscribeFromTopic()
                    FirebaseInstanceId.getInstance().deleteInstanceId();
                } catch (IOException e) {
                    e.printStackTrace();
                    Crashlytics.logException(e);
                }
            }
        }).start();
    }


    // for notification settings
    @SuppressLint("CheckResult")
    private void getNotificationSettings() {
        if (!getMvpView().isNetworkConnected()) {
            //getMvpView().showMessage(R.string.connection_error);

            getMvpView().setProximityNotification(getDataManager().getNotificationSettingResponse());

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
                            getDataManager().saveAppSettings(new Gson().toJson(response));

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

    @SuppressLint("CheckResult")
    @Override
    public void saveDailyExpiringItems(boolean value) {
        DailyReminderExpiringRequest request = new DailyReminderExpiringRequest();


        if (value) {
            request.setIsConnected(1);
        } else {
            request.setIsConnected(0);
        }


        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        getDataManager().doSaveDailyReminderExpiring(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DailyReminderExpiringResponse>() {
                    @Override
                    public void accept(DailyReminderExpiringResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

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

    @SuppressLint("CheckResult")
    @Override
    public void saveDailyExpiredItems(boolean value) {

        DailyReminderExpiringRequest request = new DailyReminderExpiringRequest();


        if (value) {
            request.setIsConnected(1);
        } else {
            request.setIsConnected(0);
        }


        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        getDataManager().doSaveDailyReminderExpired(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DailyReminderExpiredResponse>() {
                    @Override
                    public void accept(DailyReminderExpiredResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

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

}
