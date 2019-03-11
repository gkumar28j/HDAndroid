package com.mcn.honeydew.ui.dummy;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;

/**
 * Created by amit on 7/3/18.
 */
@PerActivity
public interface DummyMvpPresenter<V extends DummyMvpView> extends MvpPresenter<V> {

}
