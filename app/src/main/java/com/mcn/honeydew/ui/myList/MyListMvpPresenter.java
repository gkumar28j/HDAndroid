package com.mcn.honeydew.ui.myList;

import com.mcn.honeydew.data.network.model.request.ChangeItemStatusRequest;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.ui.base.MvpPresenter;

import java.util.ArrayList;

/**
 * Created by gkumar on 26/2/18.
 */

public interface MyListMvpPresenter<V extends MyListMvpView> extends MvpPresenter<V> {

    void getData(String Id,boolean showLoading);


    void deleteItem(MyListResponseData data, int layoutPosition);

    void changeItemStatus(ChangeItemStatusRequest changeItemStatusRequest);

    void onReorderData(ArrayList<MyListResponseData> data);

    void fetchBluetoothItems();
}
