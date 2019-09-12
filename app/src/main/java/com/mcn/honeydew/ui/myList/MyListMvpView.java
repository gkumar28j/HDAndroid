package com.mcn.honeydew.ui.myList;

import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by gkumar on 26/2/18.
 */

public interface MyListMvpView extends MvpView{

    void replceData(List<MyListResponseData> myListResponseData, boolean inProgressValue);

    void onErrorOccured();

    /*void onListItemDeleted(List<GetProximityResponse.Result> results);*/

    void onDeleteComplete();

}
