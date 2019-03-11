package com.mcn.honeydew.ui.addlist;

import android.annotation.SuppressLint;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.request.AddUpdateListRequest;
import com.mcn.honeydew.data.network.model.response.AddUpdateListResponse;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by amit on 20/2/18.
 */

public class AddListPresenter<V extends AddListMvpView> extends BasePresenter<V> implements AddListMvpPresenter<V> {

    @Inject
    public AddListPresenter(DataManager dataManager,
                            SchedulerProvider schedulerProvider,
                            CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewPrepared(String name, int listId, String color, int fontSize) {
        if (!getMvpView().isNetworkConnected()) {
            getMvpView().showMessage(R.string.connection_error);
            return;
        }
        getMvpView().showLoading();
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
                        if (!isViewAttached()) {
                            return;
                        }
                        getMvpView().hideLoading();

                        if (response.getErrorObject().getStatus() == 1) {
                            getMvpView().openAddItemFragment(response.getResult().getNewItemId());
                        } else
                            getMvpView().onError(response.getErrorObject().getErrorMessage());
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

    @Override
    public void createMyListData(String date, String name, int listId, String colorcode) {
        MyHomeListData listData = new MyHomeListData();
        listData.setListId(listId);
        listData.setListHeaderColor(colorcode);
        listData.setListName(name);
        listData.setLongSelected(false);
        listData.setIsOwner(true);
        listData.setListFontSize(12);
        listData.setListOwnerName(getDataManager().getUserData().getUserName());
        listData.setListOwnerId(getDataManager().getUserData().getUserProfileId());
        listData.setCreatedDate(date);
        getMvpView().openMyList(listData);


    }
}
