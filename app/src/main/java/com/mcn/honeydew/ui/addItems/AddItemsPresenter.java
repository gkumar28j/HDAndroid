package com.mcn.honeydew.ui.addItems;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by gkumar on 5/3/18.
 */

public class AddItemsPresenter<V extends AddItemsMvpView> extends BasePresenter<V> implements AddItemsMvpPresenter<V> {

    @Inject
    public AddItemsPresenter(DataManager dataManager,
                             SchedulerProvider schedulerProvider,
                             CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


    @Override
    public void onGetLocationData() {

    }
}
