package com.mcn.honeydew.ui.contactList;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface ContactListMvpPresenter<V extends ContactListMvpView> extends MvpPresenter<V> {

   // void getContacts(ContentResolver contentResolver);

    void doShareList(int listId, String emailOrPhone, String contactName);
}

