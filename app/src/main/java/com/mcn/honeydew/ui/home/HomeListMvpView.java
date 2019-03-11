package com.mcn.honeydew.ui.home;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.ArrayList;

/**
 * Created by amit on 20/2/18.
 */

public interface HomeListMvpView extends MvpView {

    void replceData(ArrayList<MyHomeListData> response);
    void deleteComplete(String message,int pos);
    void onEditCompleted();
    void onReorderComplete();

}
