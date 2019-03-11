package com.mcn.honeydew.ui.addItems.addItemsWhen;

import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddItemsWhenMvpPresenter<V extends AddItemsWhenMvpView> extends MvpPresenter<V> {

    void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName,
                    String Location, String Longitude, int StatusId);

    void fetchLatLng(String text, Double Lat, Double Lng, int radius, String key);

    void fetchBluetoothItems();
}
