package com.mcn.honeydew.ui.settings.editEmail;

import com.mcn.honeydew.ui.base.MvpPresenter;


public interface EditEmailMvpPresenter<V extends EditEmailMvpView> extends MvpPresenter<V> {

    void onEmailSubmit(String s);

    void onCancelClicked();

    void onViewPrepared();
}
