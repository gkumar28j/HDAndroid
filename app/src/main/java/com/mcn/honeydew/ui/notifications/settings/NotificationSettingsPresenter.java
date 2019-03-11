package com.mcn.honeydew.ui.notifications.settings;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.request.PushNotificationSettingsRequest;
import com.mcn.honeydew.data.network.model.request.UpdateProximityRangeRequest;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.PushNotificationSettingsResponse;
import com.mcn.honeydew.data.network.model.response.UpdateProximityRangeResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 28/2/18.
 */

public class NotificationSettingsPresenter<V extends NotificationSettingsMvpView> extends BasePresenter<V> implements NotificationSettingsMvpPresenter<V> {

    private static final String TAG = NotificationSettingsPresenter.class.getSimpleName();

    @Inject
    public NotificationSettingsPresenter(DataManager dataManager,
                                         SchedulerProvider schedulerProvider,
                                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onViewPrepared() {
        getMvpView().setProximitySettings(getDataManager().getProximitySettings());
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateProximitySettings(final int notificationStatus, final int notificationType) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();
        getDataManager().doPushNotificationSettingsCall(new PushNotificationSettingsRequest(notificationStatus, notificationType))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<PushNotificationSettingsResponse>() {
                    @Override
                    public void accept(PushNotificationSettingsResponse response) throws Exception {

                        getMvpView().hideLoading();
                        NotificationSettingsResponse settingsResponse = getDataManager().getProximitySettings();
                        if (notificationStatus == 1) {

                            settingsResponse.getResults().get(0).setProximityNotification(true);
                            UserDetailResponse userDetailResponse = getDataManager().getUserData();
                            userDetailResponse.setIsProximityNotification(true);
                            getDataManager().setUserData(userDetailResponse);
                            getMvpView().updateProximityRange(settingsResponse.getResults().get(0).getProximityRange());


                        } else {
                            settingsResponse.getResults().get(0).setProximityNotification(false);
                            UserDetailResponse userDetailResponse = getDataManager().getUserData();
                            userDetailResponse.setIsProximityNotification(false);
                            getDataManager().setUserData(userDetailResponse);
                        }
                        getDataManager().setProximitySettings(settingsResponse);
                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void updateProximityRange(final String range, final int position) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();
        getDataManager().doUpdateProximityRangeCall(new UpdateProximityRangeRequest(position + 1))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<UpdateProximityRangeResponse>() {
                    @Override
                    public void accept(UpdateProximityRangeResponse response) throws Exception {

                        getMvpView().hideLoading();
                        NotificationSettingsResponse settingsResponse = getDataManager().getProximitySettings();
                        if (response.getErrorObject().getStatus() == 1) {
                            settingsResponse.getResults().get(0).setProximityRange(range);
                        }
                        getDataManager().setProximitySettings(settingsResponse);

                        // Setting position into user data
                        UserDetailResponse userDetail =  getDataManager().getUserData();
                        userDetail.setProximityId(position);
                        getDataManager().setUserData(userDetail);

                        getMvpView().onProximityRangeUpdated();

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });
    }

}
