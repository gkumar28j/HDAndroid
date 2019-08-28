package com.mcn.honeydew.ui.notifications;

import com.mcn.honeydew.data.DataManager;
import com.mcn.honeydew.ui.base.BasePresenter;
import com.mcn.honeydew.utils.rx.SchedulerProvider;
import javax.inject.Inject;
import io.reactivex.disposables.CompositeDisposable;


/**
 * Created by amit on 20/2/18.
 */

public class NotificationsPresenter<V extends NotificationsMvpView> extends BasePresenter<V> implements NotificationsMvpPresenter<V> {

    @Inject
    public NotificationsPresenter(DataManager dataManager,
                                  SchedulerProvider schedulerProvider,
                                  CompositeDisposable compositeDisposable) {
        super(dataManager, schedulerProvider, compositeDisposable);
    }

    @android.annotation.SuppressLint("CheckResult")
    @Override
    public void onViewPrepared() {


    }
}
