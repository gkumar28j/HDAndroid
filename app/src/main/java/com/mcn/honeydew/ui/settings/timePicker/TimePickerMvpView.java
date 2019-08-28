package com.mcn.honeydew.ui.settings.timePicker;


import com.mcn.honeydew.ui.base.MvpView;

public interface TimePickerMvpView extends MvpView {

    void dismissDialog();

    void setTimeInTimePicker();

    void refreshData();
}
