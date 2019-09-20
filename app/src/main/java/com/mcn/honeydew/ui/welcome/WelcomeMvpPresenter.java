package com.mcn.honeydew.ui.welcome;

import com.mcn.honeydew.di.PerActivity;
import com.mcn.honeydew.ui.base.MvpPresenter;
import com.mcn.honeydew.ui.base.MvpView;

@PerActivity
public interface WelcomeMvpPresenter<V extends WelcomeMvpView> extends MvpPresenter<V> {


    void openFinishTour();

}
