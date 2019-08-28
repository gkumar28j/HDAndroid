package com.mcn.honeydew.ui.settings.timePicker;

import com.mcn.honeydew.ui.base.MvpPresenter;

public interface TimePickerMvpPresenter<V extends TimePickerMvpView> extends MvpPresenter<V> {

    void onCancelClicked();

    void onViewPrepared();

    void updateTime(String date,int type);

}
