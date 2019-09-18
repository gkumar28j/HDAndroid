package com.mcn.honeydew.ui.sharelist;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.request.AllowAutoDeleteRequest;
import com.mcn.honeydew.data.network.model.response.DeleteUserResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.data.network.model.response.UpdateNotificationSettingRequest;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;

/**
 * Created by amit on 7/3/18.
 */

public class ShareListPresenter<V extends ShareListMvpView> extends BasePresenter<V> implements ShareListMvpPresenter<V> {

    @Inject
    public ShareListPresenter(DataManager dataManager,
                              SchedulerProvider schedulerProvider,
                              CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onViewPrepared() {
        getMvpView().onSelectedListLoaded(getDataManager().getSavedList());
    }

    @SuppressLint("CheckResult")
    @Override
    public void getUserSettings(int listId) {
        if (!getMvpView().isNetworkConnected()) {
          //  getMvpView().showMessage(R.string.connection_error);
            if(getDataManager().getSharedListSetting()!=null && getDataManager().getSharedListSetting().size()>0){
                getMvpView().onUserSettingsLoaded(getDataManager().getSharedListSetting());
            }

            return;
        }
        getMvpView().showLoading();

        getDataManager().doGetUserSettingsCall(listId)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<GetUserSettingResponse>() {
                    @Override
                    public void accept(GetUserSettingResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (response.getErrorObject().getStatus() == 1) {

                            getDataManager().saveSharedListData(new Gson().toJson(response.getResult()));
                            getMvpView().onUserSettingsLoaded(response.getResult());

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
    public void deleteUserFromSharedList(int listId, String emailOrPhone) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        HashMap<String, String> requestHashMap = new HashMap<>();
        requestHashMap.put("ListId", String.valueOf(listId));
        requestHashMap.put("EmailorPhoneNumber", emailOrPhone);

        getDataManager().deleteUserFromSharedListCall(requestHashMap)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteUserResponse>() {
                    @Override
                    public void accept(DeleteUserResponse response) throws Exception {

                        fetchBluetoothItems();

                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().onUserDeletedFromSharedList();
                        }

                        getMvpView().showMessage(response.getResult().getMessage());


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
    public void changeNotificationSettingForSharedUser(int status, String emailOrPhone) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }

        UpdateNotificationSettingRequest request = new UpdateNotificationSettingRequest();
        request.setAllownotificationFromuser(status);
        request.setEmailOrPhoneNumber(emailOrPhone);

        getDataManager().updateNotificationSettingByOwnerCall(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteUserResponse>() {
                    @Override
                    public void accept(DeleteUserResponse response) throws Exception {
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

    @Override
    public void changeAutoDeletionSetting(int status, String toUserEmailOrPhone) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        AllowAutoDeleteRequest request = new AllowAutoDeleteRequest();
        request.setIsAllowDeleteItemOnCompletion(status);
        request.setTouserEmailOrPhonenumber(toUserEmailOrPhone);
        request.setListId(getDataManager().getSavedList().getListId());

        getDataManager().allowAutoDeleteOnItemComplete(request)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteUserResponse>() {
                    @Override
                    public void accept(DeleteUserResponse response) throws Exception {
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

    @Override
    public void changePushStatusForOthersList(int status) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        HashMap<String, Integer> hashMap = new HashMap<>();
        hashMap.put("NotificationStatus", status);
        hashMap.put("NotificationType", 0); //0 = pushnotification, 1 = Bluetooth, 2 = Proximity

        getDataManager().changePushStatusForOthersList(hashMap)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<DeleteUserResponse>() {
                    @Override
                    public void accept(DeleteUserResponse response) throws Exception {
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

                    }
                });
    }
}
