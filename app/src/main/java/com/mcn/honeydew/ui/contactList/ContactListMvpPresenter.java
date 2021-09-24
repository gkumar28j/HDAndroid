package com.mcn.honeydew.ui.contactList;

import com.mcn.honeydew.data.network.model.SelectedContact;
import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

import java.util.List;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ContactListMvpPresenter<V extends ContactListMvpView> extends MvpPresenter<V> {
    void shareListToContact(int listId, List<SelectedContact> selectedContacts);
}

