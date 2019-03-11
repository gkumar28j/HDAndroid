package com.mcn.honeydew.ui.addlist;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 20/2/18.
 */

public interface AddListMvpView extends MvpView {

    void openAddItemFragment(int newItemId);

    void openMyList(MyHomeListData data);

}
