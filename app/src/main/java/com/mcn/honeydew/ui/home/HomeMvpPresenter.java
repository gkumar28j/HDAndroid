package com.mcn.honeydew.ui.home;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

import java.util.ArrayList;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface HomeMvpPresenter<V extends HomeListMvpView> extends MvpPresenter<V> {
    void onViewPrepared(boolean showLoading);
    void onDeleteCard(MyHomeListData data, int layoutPosition);
    void onUnshareCard(MyHomeListData data, int layoutPosition);
    void onReorderCardsData(ArrayList<MyHomeListData> data);

    void onEditListName(String name, int listId, String color, int fontSize);

    void fetchBluetoothList();

    void resaveData(ArrayList<MyHomeListData> list);
}
