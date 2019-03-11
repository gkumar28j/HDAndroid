package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.AddUpdateItemRequest;
import com.mcn.honeydew.data.network.model.response.AddUpdateItemResponse;
import com.mcn.honeydew.data.network.model.response.DeleteRecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddRecentItemsPresenter<V extends AddRecentItemsMvpView> extends BasePresenter<V> implements AddRecentItemsMvpPresenter<V>{

    @Inject
    public AddRecentItemsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doGetRecentItemsListCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<RecentItemsResponse>() {
                    @Override
                    public void accept(RecentItemsResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }

                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().updateView(response.getResult());
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
                        // handle the login error here
                        // handleApiError(throwable);

                    }
                });
    }

    @Override
    public void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName, String Location, String Longitude, int StatusId) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            getMvpView().addItemsCallFailed();
            return;
        }
        getMvpView().showLoading();

        final AddUpdateItemRequest request = new AddUpdateItemRequest();
        request.setItemId(ItemId);
        request.setItemName(ItemName);
        request.setItemTime(ItemTime);
        request.setLatitude(Latitude);
        request.setListId(ListId);
        request.setListName(ListName);
        request.setLongitude(Longitude);
        request.setLocation(Location);
        request.setStatusId(StatusId);
        request.setPhoto("");


        getDataManager().doAddUpdateItemsCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<AddUpdateItemResponse>() {
                    @Override
                    public void accept(AddUpdateItemResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().itemSuccesfullyAdded();
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
                        getMvpView().addItemsCallFailed();
                        // handle the login error here
                        // handleApiError(throwable);

                    }
                });
    }

    @Override
    public void deleteRecentItems() {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doDeleteRecentItemsCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteRecentItemsResponse>() {
                    @Override
                    public void accept(DeleteRecentItemsResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().recentItemsDeleted(response.getResult().getStatus());
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
                        // handle the login error here
                        // handleApiError(throwable);

                    }
                });
    }

}
