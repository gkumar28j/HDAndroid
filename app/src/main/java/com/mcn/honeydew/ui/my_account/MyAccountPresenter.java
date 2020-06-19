package com.mcn.honeydew.ui.my_account;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class MyAccountPresenter<V extends MyAccountMvpView> extends BasePresenter<V> implements MyAccountMvpPresenter<V> {

    @Inject
    public MyAccountPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

}
