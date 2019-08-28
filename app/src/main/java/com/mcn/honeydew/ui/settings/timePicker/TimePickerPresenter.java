package com.mcn.honeydew.ui.settings.timePicker;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.ReminderTimeRequest;
import com.mcn.honeydew.data.network.model.response.ReminderTimeResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

public class TimePickerPresenter<V extends TimePickerMvpView> extends BasePresenter<V> implements TimePickerMvpPresenter<V> {

    @Inject
    public TimePickerPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onCancelClicked() {

        getMvpView().dismissDialog();

    }

    @Override
    public void onViewPrepared() {

        getMvpView().setTimeInTimePicker();
    }

    @SuppressLint("CheckResult")
    @Override
    public void updateTime(String date, int type) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getMvpView().showLoading();

        ReminderTimeRequest reminderTimeRequest = new ReminderTimeRequest();
        reminderTimeRequest.setReminderTime(date);
        reminderTimeRequest.setIsExpired(type);


        getDataManager().doSaveReminderTime(reminderTimeRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ReminderTimeResponse>() {
                    @Override
                    public void accept(ReminderTimeResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                    //    getMvpView().showMessage(response.getResult().getMessage());

                        getMvpView().refreshData();

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
}
