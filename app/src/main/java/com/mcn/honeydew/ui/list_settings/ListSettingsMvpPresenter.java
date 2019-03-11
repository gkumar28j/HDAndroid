package com.mcn.honeydew.ui.list_settings;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface ListSettingsMvpPresenter<V extends ListSettingsMvpView> extends MvpPresenter<V> {
    void getListSettings(int listId);

    void updateSettings(int listId, boolean status);
}
