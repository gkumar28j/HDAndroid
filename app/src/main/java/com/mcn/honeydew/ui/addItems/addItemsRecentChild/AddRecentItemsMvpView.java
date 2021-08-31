package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import com.mcn.honeydew.data.network.model.response.RecentItemsResponse;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddRecentItemsMvpView  extends MvpView{

    void updateView(List<RecentItemsResponse.RecentItemsData> mlist);

    void itemSuccesfullyAdded();

    void recentItemsDeleted(int status);

    void addItemsCallFailed();

    void onDeletePhotoSuccess();

}
