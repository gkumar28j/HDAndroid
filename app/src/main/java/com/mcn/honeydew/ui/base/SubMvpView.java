package com.mcn.honeydew.ui.base;

/**
 * Created by amit on 15/2/18.
 */

public interface SubMvpView extends MvpView {

    void onCreate();

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onDestroy();

    void attachParentMvpView(MvpView mvpView);
}

