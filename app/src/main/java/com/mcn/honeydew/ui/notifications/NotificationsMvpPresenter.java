package com.mcn.honeydew.ui.notifications;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface NotificationsMvpPresenter<V extends NotificationsMvpView> extends MvpPresenter<V> {


    void loadData();

    void loadMoreData();

    void refreshData();

    void setIsRead(int notificationId);

    void resetNotification();


}
