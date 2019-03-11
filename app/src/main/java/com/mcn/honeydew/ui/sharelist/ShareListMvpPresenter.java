package com.mcn.honeydew.ui.sharelist;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 7/3/18.
 */
@PerActivity
public interface ShareListMvpPresenter<V extends ShareListMvpView> extends MvpPresenter<V> {

    void onViewPrepared();

    void getUserSettings(int listId);

    void deleteUserFromSharedList(int listId, String emailOrPhone);

    void changeNotificationSettingForSharedUser(int status, String emailOrPhone);

    void changeAutoDeletionSetting(int status, String toUserEmailOrPhone);

    void changePushStatusForOthersList(int status);

    void fetchBluetoothItems();
}
