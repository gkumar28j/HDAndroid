package com.mcn.honeydew.ui.addItems.addItemsWhen;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.response.AddUpdateItemResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import io.reactivex.ObservableSource;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observers.DefaultObserver;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddItemsWhenPresenter<V extends AddItemsWhenMvpView> extends BasePresenter<V> implements AddItemsWhenMvpPresenter<V> {

    @Inject
    public AddItemsWhenPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
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

      /*  final AddUpdateItemRequest request = new AddUpdateItemRequest();
        request.setItemId(ItemId);
        request.setItemName(ItemName);
        request.setItemTime(ItemTime);
        request.setLatitude(Latitude);
        request.setListId(ListId);
        request.setListName(ListName);
        request.setLongitude(Longitude);
        request.setLocation(Location);
        request.setStatusId(StatusId);
        request.setPhoto("");*/


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
                .flatMap(new Function<AddUpdateItemResponse, ObservableSource<GetBluetoothItemsListResponse>>() {
                    @Override
                    public ObservableSource<GetBluetoothItemsListResponse> apply(AddUpdateItemResponse addUpdateItemResponse) throws Exception {

                        if (isViewAttached()) {
                            if (addUpdateItemResponse.getErrorObject().getStatus() == 1) {
                                getMvpView().itemSuccesfullyAdded();
                            }
                        }

                        return getDataManager().doGetAllBluetoothItems()
                                .subscribeOn(getSchedulerProvider().io())
                                .observeOn(getSchedulerProvider().ui());
                    }
                }).subscribeWith(new DefaultObserver<GetBluetoothItemsListResponse>() {
            @Override
            public void onNext(GetBluetoothItemsListResponse bluetoothItemsListResponse) {
                if (!isViewAttached())
                    return;
                getMvpView().hideLoading();
                ArrayList<GetBluetoothItemsListResponse.BluetoothItem> newItems = new ArrayList<>(Arrays.asList(bluetoothItemsListResponse.getResult()));
                ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = getDataManager().getSavedBluetoothItems();
                for (int i = 0; (i < newItems.size()); i++) {
                    GetBluetoothItemsListResponse.BluetoothItem newItem = newItems.get(i);
                    // if an item of the api response is not available in saved list them adding that in saved item and
                    // saving updated saved item in shared pref.
                    if (savedItems.contains(newItem))
                        newItem.setSent(true);
                }
                getDataManager().saveBluetoothItemList(new Gson().toJson(newItems));
            }

            @Override
            public void onError(Throwable e) {
                if (!isViewAttached())
                    return;
                getMvpView().hideLoading();
            }

            @Override
            public void onComplete() {
                if (!isViewAttached())
                    return;
                getMvpView().hideLoading();
            }
        });


       /* getDataManager().doAddUpdateItemsCall(request)
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
                });*/
    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchLatLng(String text, Double Lat, Double Lng, int radius, String key) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().addItemsCallFailed();
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        String loc = String.valueOf(Lat) + "," + String.valueOf(Lng);
        getMvpView().showLoading();

        getDataManager().doGetGoogleLocationCall(text, loc, radius, key)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<RecentLocationAddItemsResponse>() {
                    @Override
                    public void accept(RecentLocationAddItemsResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().receiveLatLngs(response.getResults());

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
                        if (!isViewAttached()) {
                            return;
                        }
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> newItems = new ArrayList<>(Arrays.asList(response.getResult()));
                        ArrayList<GetBluetoothItemsListResponse.BluetoothItem> savedItems = getDataManager().getSavedBluetoothItems();
                        for (int i = 0; (i < newItems.size()); i++) {
                            GetBluetoothItemsListResponse.BluetoothItem newItem = newItems.get(i);
                            // if an item of the api response is not available in saved list them adding that in saved item and
                            // saving updated saved item in shared pref.
                            if (savedItems.contains(newItem))
                                newItem.setSent(true);
                        }
                        getDataManager().saveBluetoothItemList(new Gson().toJson(newItems));

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


}
