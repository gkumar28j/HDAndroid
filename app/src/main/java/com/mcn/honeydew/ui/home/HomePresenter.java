package com.mcn.honeydew.ui.home;

import android.annotation.SuppressLint;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.HomeCardDeleteResponse;
import com.mcn.honeydew.data.network.model.HomeListCardDeleteRequest;
import com.mcn.honeydew.data.network.model.HomeResponse;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.ReorderCardsHomeRequest;
import com.mcn.honeydew.data.network.model.ReorderHomeListData;
import com.mcn.honeydew.data.network.model.request.AddUpdateListRequest;
import com.mcn.honeydew.data.network.model.request.UnshareListRequest;
import com.mcn.honeydew.data.network.model.response.AddUpdateListResponse;
import com.mcn.honeydew.data.network.model.response.GetBluetoothItemsListResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amit on 20/2/18.
 */

public class HomePresenter<V extends HomeListMvpView> extends BasePresenter<V> implements HomeMvpPresenter<V> {

    private boolean hasShownNetworkError = false;
    private boolean hasShownServerError = false;

    @Inject
    public HomePresenter(DataManager dataManager,
                         SchedulerProvider schedulerProvider,
                         CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onViewPrepared(boolean showLoading) {
        fetchApiData(showLoading);
    }

    @Override
    public void onDeleteCard(MyHomeListData data, final int layoutPosition) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        getDataManager().doDeleteCards(new HomeListCardDeleteRequest(data.getListId()))
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<HomeCardDeleteResponse>() {
                    @Override
                    public void accept(HomeCardDeleteResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (response.getResult().getStatus() == 1) {
                            getMvpView().deleteComplete(response.getResult().getMessage(), layoutPosition);
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

    @Override
    public void onUnshareCard(MyHomeListData data, final int layoutPosition) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();

        UnshareListRequest mRequest = new UnshareListRequest();
        mRequest.setListId(data.getListId());
        mRequest.setListName(data.getListName());
        mRequest.setListOwnerId(data.getListOwnerId());
        mRequest.setToUserName(data.getToUserName());
        mRequest.setToUserProfileId(data.getToUserProfileId());

        getDataManager().doGetUnshareList(mRequest)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<HomeCardDeleteResponse>() {
                    @Override
                    public void accept(HomeCardDeleteResponse response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        if (response.getResult().getStatus() == 1) {
                            getMvpView().deleteComplete(response.getResult().getMessage(), layoutPosition);
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

    @SuppressLint("CheckResult")
    @Override
    public void onReorderCardsData(ArrayList<MyHomeListData> data) {
        //getMvpView().showLoading();

        List<ReorderCardsHomeRequest> mlist = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            ReorderCardsHomeRequest reorderCardsHomeRequest = new ReorderCardsHomeRequest();
            reorderCardsHomeRequest.setListId(data.get(i).getListId());
            reorderCardsHomeRequest.setOrderNumber(data.size() - i);
            mlist.add(reorderCardsHomeRequest);
        }


        getDataManager().doReorderHomeCards(mlist)
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<ReorderHomeListData>() {
                    @Override
                    public void accept(ReorderHomeListData response) throws Exception {
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();
                        getMvpView().onReorderComplete();

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
    private void fetchApiData(boolean showLoading) {
        if (!isViewAttached()) return;

        if (!getMvpView().isNetworkConnected()) {

        /*    if (hasShownNetworkError) return;

            getMvpView().showMessage(R.string.connection_error);
            hasShownNetworkError = true;*/

            ArrayList<MyHomeListData> response = getDataManager().getHomeResponseData();
            getMvpView().replceData(response);
            hasShownNetworkError = false;
            hasShownServerError = false;


            return;
        }
        if (showLoading) {
            getMvpView().showLoading();
        } else {
            // getMvpView().showLoading();
        }


        getDataManager().doHomeApiCall()
                .subscribeOn(getSchedulerProvider().io())
                .observeOn(getSchedulerProvider().ui())
                .subscribe(new Consumer<HomeResponse>() {
                    @Override
                    public void accept(HomeResponse response) throws Exception {

                        if (!isViewAttached()) {
                            return;
                        }
                        if (response.getErrorObject().getStatus() == 1) {
                            if (response.getResults() != null) {
                                getMvpView().replceData(response.getResults());
                                ArrayList<MyHomeListData> newItems = new ArrayList<>(response.getResults());
                                getDataManager().saveHomeResponseData(new Gson().toJson(newItems));
                                hasShownNetworkError = false;
                                hasShownServerError = false;
                            }

                        } else {

                        }

                        getMvpView().hideLoading();

                    }


                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {

                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();

                        if (hasShownServerError) return;

                        // handle the login error here
                        handleApiError(throwable);
                        hasShownServerError = true;

                    }
                });

    }

    @Override
    public void onEditListName(String name, int listId, String color, int fontSize) {

        if (!isViewAttached())
            return;

        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        //  getMvpView().showLoading();
        AddUpdateListRequest request = new AddUpdateListRequest();
        request.setListFontSize(fontSize);
        request.setListHeaderColor(color);
        request.setListId(listId);
        request.setListName(name);


        getDataManager().doAddUpdateListCall(request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<AddUpdateListResponse>() {
                    @Override
                    public void accept(AddUpdateListResponse response) throws Exception {
                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();

                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().onEditCompleted();
                        } else
                            getMvpView().onError(response.getErrorObject().getErrorMessage());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (!isViewAttached()) return;
                        getMvpView().hideLoading();
                        handleApiError(throwable);
                    }
                });

    }

    @SuppressLint("CheckResult")
    @Override
    public void fetchBluetoothList() {
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
