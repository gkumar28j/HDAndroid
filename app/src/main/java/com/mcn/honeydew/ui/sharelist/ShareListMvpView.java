package com.mcn.honeydew.ui.sharelist;

import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.response.GetUserSettingResponse;
import com.mcn.honeydew.ui.base.MvpView;

import java.util.List;

/**
 * Created by amit on 7/3/18.
 */

public interface ShareListMvpView extends MvpView {

    void onSelectedListLoaded(MyHomeListData data);

    void onUserSettingsLoaded(List<GetUserSettingResponse.Result> settings);

    void onUserDeletedFromSharedList();

}
