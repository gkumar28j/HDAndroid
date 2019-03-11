package com.mcn.honeydew.ui.addItems.addItemsWhen;

import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by gkumar on 9/3/18.
 */

public interface AddItemsWhenMvpView extends MvpView {

    void itemSuccesfullyAdded();
    void receiveLatLngs(List<RecentLocationAddItemsResponse.LocationResult> results);
    void addItemsCallFailed();
}
