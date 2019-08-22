package com.mcn.honeydew.ui.settings.editEmail;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

public class EditEmailDialogPresenter<V extends EditEmailMvpView> extends BasePresenter<V> implements EditEmailMvpPresenter<V> {


    @Inject
    public EditEmailDialogPresenter(DataManager dataManager, SchedulerProvider schedulerProvider, CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @Override
    public void onEmailSubmit(String s) {

    }

    @Override
    public void onCancelClicked() {

    }

    @Override
    public void onViewPrepared() {

    }
}
