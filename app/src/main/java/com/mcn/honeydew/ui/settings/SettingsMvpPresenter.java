package com.mcn.honeydew.ui.settings;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 20/2/18.
 */

@PerActivity
public interface SettingsMvpPresenter<V extends SettingsMvpView> extends MvpPresenter<V> {
    void onViewPrepared();

    void onLogoutClick();

    void onUpdateUserClick();

    void onUpdatePasswordClicked();
}
