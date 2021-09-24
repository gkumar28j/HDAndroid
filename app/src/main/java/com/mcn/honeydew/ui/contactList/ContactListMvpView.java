package com.mcn.honeydew.ui.contactList;

import com.mcn.honeydew.ui.base.MvpView;


/**
 * Created by amit on 16/2/18.
 **/

public interface ContactListMvpView extends MvpView {
    void onListSharedSuccess(int position);

    void onListSharedFailure(int position, String errorMessage);

    void onSharingFinished(String message);
}
