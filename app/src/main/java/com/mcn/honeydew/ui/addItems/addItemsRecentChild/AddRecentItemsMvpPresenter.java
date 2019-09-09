package com.mcn.honeydew.ui.addItems.addItemsRecentChild;

import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddRecentItemsMvpPresenter<V extends AddRecentItemsMvpView> extends MvpPresenter<V> {

    void onViewPrepared();

    void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName,
                    String Location, String Longitude, int StatusId, String url);

    void deleteRecentItems();


}
