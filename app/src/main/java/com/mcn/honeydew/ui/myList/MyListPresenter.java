package com.mcn.honeydew.ui.myList;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.HomeDetailListResponse;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.ReorderHomeListData;
import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.request.DeleteItemListRequest;
import com.mcn.honeydew.data.network.model.request.ReorderItemsMyList;
import com.mcn.honeydew.data.network.model.response.ChangeItemStatusResponse;
import com.mcn.honeydew.data.network.model.response.DeleteItemListResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gkumar on 26/2/18.
 */

public class MyListPresenter<V extends MyListMvpView> extends BasePresenter<V> implements MyListMvpPresenter<V> {
    private boolean hasShownNetworkError = false;
    private boolean hasShownServerError = false;

    @Inject
    public MyListPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void getData(String Id, boolean showLoading) {
        if (!getMvpView().isNetworkConnected()) {

            if (hasShownNetworkError) return;
            getMvpView().showMessage(R.string.connection_error);
            hasShownNetworkError = true;

            return;
        }
        if (showLoading) {
            getMvpView().showLoading();
        }


        getDataManager().doGetHomeDetailListCall(Id)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<HomeDetailListResponse>() {
                    @Override
                    public void accept(HomeDetailListResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }

                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().replceData(response.getMyListResponseData());

                            // Setting isOwner field in saved list data. this value is not coming from push notification and its is used to show
                            // particular layout (list owner/doer)
                            // Modified By: Ashish
                            if (response.getMyListResponseData().size() > 0) {
                                MyHomeListData myHomeListData = getDataManager().getSavedList();
                                myHomeListData.setIsOwner(response.getMyListResponseData().get(0).isOwner());
                                getDataManager().saveList(myHomeListData);
                            }


                            hasShownNetworkError = false;
                            hasShownServerError = false;
                        } else {

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

                        if (hasShownServerError) return;

                        getMvpView().onErrorOccured();
                        hasShownServerError = true;

                    }
                });
    }

    @SuppressLint("CheckResult")
    @Override
    public void deleteItem(final MyListResponseData data, int layoutPosition) {


        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }


        getDataManager().doDeleteItemListCall(new DeleteItemListRequest(data.getItemId(), data.getListId(), data.getListName()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteItemListResponse>() {
                    @Override
                    public void accept(DeleteItemListResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().onDeleteComplete();

/*
                        GetProximityResponse.Result result = new GetProximityResponse.Result();
                        result.setItemName(data.getItemName());
                        result.setLocation(data.getLocation());
                        result.setStatusId(data.getStatusId());
                        result.setLongitude(Double.valueOf(data.getLongitude()));
                        result.setLatitude(Double.valueOf(data.getLatitude()));
                        result.setListId(data.getListId());
                        result.setListHeaderColor(data.getListHeaderColor());
                        result.setListId(data.getListId());

                        List<GetProximityResponse.Result> resultList = new ArrayList<>();
                        resultList.add(result);
                        getMvpView().onListItemDeleted(resultList);*/

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void changeItemStatus(ChangeItemStatusRequest changeItemStatusRequest) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getDataManager().dochangeItemStatusListCall(changeItemStatusRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ChangeItemStatusResponse>() {
                    @Override
                    public void accept(ChangeItemStatusResponse response) throws Exception {


                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void onReorderData(ArrayList<MyListResponseData> data) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        //  getMvpView().showLoading();

        List<ReorderItemsMyList> mlist = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ReorderItemsMyList reorderItems = new ReorderItemsMyList();
            reorderItems.setItemId(data.get(i).getItemId());
            reorderItems.setOrderNumber(data.size() - i);
            reorderItems.setUserProfileId(data.get(i).getUserProfileId());
            mlist.add(reorderItems);
        }


        getDataManager().doReorderItems(mlist)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ReorderHomeListData>() {
                    @Override
                    public void accept(ReorderHomeListData response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        //    getMvpView().hideLoading();

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        //    getMvpView().hideLoading();
                        // handle the login error here
                        handleApiError(throwable);

                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchBluetoothItems() {
        if (!getMvpView().isNetworkConnected()) {
            return;
        }

        getDataManager().doGetAllBluetoothItems()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GetBluetoothItemsListResponse>() {
                    @Override
                    public void accept(GetBluetoothItemsListResponse response) throws Exception {
                        getDataManager().saveBluetoothItemList(new Gson().toJson(response.getResult()));

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                    }
                });
    }


}
