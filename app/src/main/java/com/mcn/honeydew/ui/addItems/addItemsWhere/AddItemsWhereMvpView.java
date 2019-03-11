package com.mcn.honeydew.ui.addItems.addItemsWhere;


import com.mcn.honeydew.data.network.model.response.CustomLocationData;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddItemsWhereMvpView extends MvpView {

    void updateAdapterViews(List<CustomLocationData> arr);

    void receiveLatLngs(List<RecentLocationAddItemsResponse.LocationResult> results);

    void onReceivedNearbySearch(List<CustomLocationData> mlist);

    void itemSuccesfullyAdded();

    void recentLocationDelted(int status);

    void addItemsCallFailed();


}
