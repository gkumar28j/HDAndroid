package com.mcn.honeydew.ui.notifications;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.data.network.model.response.NotificationReadResponse;
import com.mcn.honeydew.data.network.model.response.ResetNotificationCountResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;


/**
 * Created by amit on 20/2/18.
 */

public class NotificationsPresenter<V extends NotificationsMvpView> extends BasePresenter<V> implements NotificationsMvpPresenter<V> {
    private int current_page = 0;
    private List<NotificationListResponse.NotificationListData> mList = new ArrayList<>();

    @Inject
    public NotificationsPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void loadData() {
        current_page = 0;
        getData(false, false, true);
    }

    @Override
    public void loadMoreData() {
        getData(true, false, false);
    }

    @Override
    public void refreshData() {
        current_page = 0;
        getData(false, true, false);

    }

    @SuppressLint("CheckResult")
    @Override
    public void setIsRead(int notificationId) {

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getDataManager().doUpdateNotificationRead(notificationId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<NotificationReadResponse>() {
                    @Override
                    public void accept(NotificationReadResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }


                        if (response.getErrorObject().getStatus() == 1) {


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
                        handleApiError(throwable);
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void resetNotification() {

        if (!getMvpView().isNetworkConnected()) {
          //  getMvpView().showMessage(R.string.connection_error);
            return;
        }

        getDataManager().doResetNotificationCount()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ResetNotificationCountResponse>() {
                    @Override
                    public void accept(ResetNotificationCountResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        getMvpView().hideLoading();
                        getMvpView().onResetNotification();

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
    private void getData(final boolean loadmore, final boolean refresh, final boolean showLoadingUI) {

        if (!getMvpView().isNetworkConnected()) {
        //    getMvpView().showMessage(R.string.connection_error);
            String duration = getDataManager().getNotificationFilterPref();
            getMvpView().showContentList(getDataManager().getNotificationData(), duration);
            return;
        }
        if (showLoadingUI) {
            //mView.showProgress();
            getMvpView().showLoading();
        }
        if (loadmore) {
            getMvpView().showContentLoading(true);
        }

        getDataManager().doGetNotificationList(current_page, 10)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<NotificationListResponse>() {
                    @Override
                    public void accept(NotificationListResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        //  if (response.getErrorObject().getStatus() == 1) {
                        parseGetDataResponse(response, loadmore, refresh);
                        //  }

                        getMvpView().hideLoading();

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

    private void parseGetDataResponse(NotificationListResponse response, boolean loadMore, boolean refresh) {

        String duration = getDataManager().getNotificationFilterPref();
        current_page++;
        if (refresh) {
            mList.clear();
        }
        if (!loadMore && !mList.isEmpty()) {
            Collections.sort(mList, NotificationListResponse.NotificationListData.notificationComparator);
            getMvpView().showContentList(mList,duration);
            getDataManager().saveNotificationResponseData(new Gson().toJson(mList));
        } else {
            mList.addAll(response.getResult());
            Collections.sort(mList, NotificationListResponse.NotificationListData.notificationComparator); // descending order sorting

            int pos = 0;

            for (int i = 0; i < mList.size(); i++) {

                if (mList.get(i).getNotificationId() == 0) {

                    pos = i;
                    break;
                }
            }

            NotificationListResponse.NotificationListData data = mList.get(pos);
            mList.remove(pos);
            mList.add(0, data);

            getMvpView().showContentList(mList,duration);
            getDataManager().saveNotificationResponseData(new Gson().toJson(mList));
        }
        if (!mList.isEmpty()) {
            getMvpView().showEmptyView(false);
        } else {
            getMvpView().showEmptyView(true);
        }
    }
}
