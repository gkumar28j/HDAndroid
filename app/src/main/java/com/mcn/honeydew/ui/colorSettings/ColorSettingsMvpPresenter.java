package com.mcn.honeydew.ui.colorSettings;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ColorSettingsMvpPresenter<V extends ColorSettingsMvpView> extends MvpPresenter<V> {

    void updateListColor(String colorCode, int listId);
}

