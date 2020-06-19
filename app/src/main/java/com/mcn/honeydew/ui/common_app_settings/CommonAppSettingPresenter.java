package com.mcn.honeydew.ui.common_app_settings;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class CommonAppSettingPresenter<V extends CommonAppSettingsMvpView> extends BasePresenter<V> implements CommonAppSettingsMvpPresenter<V> {


    @Inject
    public CommonAppSettingPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }
}
