package com.mcn.honeydew.ui.settings.editname;

import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 23/2/18.
 */

public interface EditNameDialogMvpPresenter<V extends EditNameDialogMvpView> extends MvpPresenter<V> {

    void onNameSubmitted(String s);

    void onCancelClicked();

    void onViewPrepared();
}
