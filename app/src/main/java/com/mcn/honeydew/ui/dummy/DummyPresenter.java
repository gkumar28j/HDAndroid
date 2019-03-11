package com.mcn.honeydew.ui.dummy;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by amit on 7/3/18.
 */

public class DummyPresenter<V extends DummyMvpView> extends BasePresenter<V> implements DummyMvpPresenter<V> {

    @Inject
    public DummyPresenter(DataManager dataManager,
                            SchedulerProvider schedulerProvider,
                            CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }


}
