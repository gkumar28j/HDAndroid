package com.mcn.honeydew.ui.shareToContacts;

import com.mcn.honeydew.ui.base.MvpView;

/**
 * Created by amit on 7/3/18.
 */

public interface ShareToContactsMvpView extends MvpView {

    void onListSharedSuccess(int position);

    void onListSharedFailure(int position, String errorMessage);

    void onSharingFinished(String message);

}
