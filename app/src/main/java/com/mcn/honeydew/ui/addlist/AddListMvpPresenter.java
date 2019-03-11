package com.mcn.honeydew.ui.addlist;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface AddListMvpPresenter<V extends AddListMvpView> extends MvpPresenter<V> {

    void onViewPrepared(String name, int listId, String color, int i);

    void createMyListData(String date, String name, int listId, String colorcode);
}
