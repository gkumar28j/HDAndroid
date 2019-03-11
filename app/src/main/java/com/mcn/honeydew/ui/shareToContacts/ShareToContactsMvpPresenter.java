package com.mcn.honeydew.ui.shareToContacts;

import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

import java.util.List;

/**
 * Created by amit on 7/3/18.
 */
@PerActivity
public interface ShareToContactsMvpPresenter<V extends ShareToContactsMvpView> extends MvpPresenter<V> {

    //void shareListToContact(int listId, String emailOrPhone, String contactName, int position);
    void shareListToContact(int listId, List<SelectedContact> selectedContacts);
}
