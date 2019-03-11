package com.mcn.honeydew.ui.addItems.addItemsWhere;

import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddItemsWhereMvpPresenter<V extends AddItemsWhereMvpView> extends MvpPresenter<V> {


    void fetchRecentLocations(double screenInInches);

    void fetchLatLng(String text, Double Lat, Double Lng, int radius, String key);

    void getNearbySearchResults(Double Lat, Double Lng, int radius, String key, String type, double smallestWidth);

    void onAddItems(int ItemId, String ItemName, String ItemTime, String Latitude, int ListId, String ListName,
                    String Location, String Longitude, int StatusId);

    void deleteRecentLocation();

    void fetchBluetoothItems();
}
