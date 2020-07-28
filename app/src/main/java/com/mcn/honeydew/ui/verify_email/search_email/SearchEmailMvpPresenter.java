package com.mcn.honeydew.ui.verify_email.search_email;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 16/2/18.
 */

@PerActivity
public interface SearchEmailMvpPresenter<V extends SearchEmailMvpView> extends MvpPresenter<V> {
    void locateAccount(String emailOrPhone);
}

