package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.HomeCardDeleteResponse;
import com.mcn.honeydew.data.network.model.HomeListCardDeleteRequest;
import com.mcn.honeydew.data.network.model.request.DeleteItemPhotoRequest;
import com.mcn.honeydew.data.network.model.response.AddUpdateItemResponse;
import com.mcn.honeydew.data.network.model.response.DeleteItemPhotoResponse;
import com.mcn.honeydew.data.network.model.response.DeleteRecentItemsResponse;
import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddRecentItemsPresenter<V extends AddRecentItemsMvpView> extends BasePresenter<V> implements AddRecentItemsMvpPresenter<V> {

    @Inject
    public AddRecentItemsPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
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
                            List<RecentItemsResponse.RecentItemsData> unsortedList = new ArrayList<>();
                            unsortedList = response.getResult();

                            Collections.sort(unsortedList, new Comparator<RecentItemsResponse.RecentItemsData>() {
                                @Override
                                public int compare(RecentItemsResponse.RecentItemsData lhs, RecentItemsResponse.RecentItemsData rhs) {
                                    return lhs.getItemName().compareToIgnoreCase(rhs.getItemName());
                                }
                            });
                            getMvpView().updateView(unsortedList);
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

    @SuppressLint("CheckResult")
    @Override
    public void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName, String Location, String Longitude, int StatusId, String url) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            getMvpView().addItemsCallFailed();
            return;
        }
        getMvpView().showLoading();

       /* final AddUpdateItemRequest request = new AddUpdateItemRequest();
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
        request.setFilePath(url);*/
        MultipartBody.Part body = null;
        if (url != null) {
            File file = new File(url);
            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), file);

            body =
                    MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        }

        RequestBody ItemIds = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(ItemId));
        RequestBody ItemNames = RequestBody.create(MediaType.parse("multipart/form-data"), ItemName != null ? ItemName : "");
        RequestBody ItemTimes = RequestBody.create(MediaType.parse("multipart/form-data"), ItemTime != null ? ItemTime : "");
        RequestBody Latitudes = RequestBody.create(MediaType.parse("multipart/form-data"), Latitude != null ? Latitude : "");
        RequestBody ListIds = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(ListId));
        RequestBody ListNames = RequestBody.create(MediaType.parse("multipart/form-data"), ListName != null ? ListName : "");
        RequestBody Longitudes = RequestBody.create(MediaType.parse("multipart/form-data"), Longitude != null ? Longitude : "");
        RequestBody Locations = RequestBody.create(MediaType.parse("multipart/form-data"), Location != null ? Location : "");
        RequestBody StatusIds = RequestBody.create(MediaType.parse("multipart/form-data"), String.valueOf(StatusId));

        getDataManager().doUpdateRecentItemsCall(ItemIds, ItemNames, ItemTimes, Latitudes, ListIds, ListNames, Longitudes, Locations, StatusIds, body)
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

    @SuppressLint("CheckResult")
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

    @SuppressLint("CheckResult")
    @Override
    public void deletePhoto(int itemID) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        DeleteItemPhotoRequest request = new DeleteItemPhotoRequest();
        request.setItemId(itemID);

        getDataManager().deleteItemPhoto(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteItemPhotoResponse>() {
                    @Override
                    public void accept(DeleteItemPhotoResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (response.getResult().getStatus() == 1) {
                            getMvpView().onDeletePhotoSuccess();

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
                        // handleApiError(throwable);

                    }
                });

    }

}
