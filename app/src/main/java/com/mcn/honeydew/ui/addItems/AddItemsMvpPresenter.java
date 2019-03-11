package com.mcn.honeydew.ui.addItems;

import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by gkumar on 5/3/18.
 */

public interface AddItemsMvpPresenter<V extends AddItemsMvpView> extends MvpPresenter<V> {



  /*  void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName,
                    String Location, String Longitude, int StatusId);*/

    void onGetLocationData();

}
