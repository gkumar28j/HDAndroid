package com.mcn.honeydew.ui.notifications;


import com.mcn.honeydew.data.network.model.response.NotificationListResponse;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by amit on 20/2/18.
 */

public interface NotificationsMvpView extends MvpView {

    void showContentLoading(boolean loading);
    void showContentList(List<NotificationListResponse.NotificationListData> contentDataModelList);

    void showEmptyView(boolean b);

    void onResetNotification();
}
