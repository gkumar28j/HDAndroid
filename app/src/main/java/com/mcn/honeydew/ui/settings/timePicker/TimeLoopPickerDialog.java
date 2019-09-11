package com.mcn.honeydew.ui.settings.timePicker;

import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseDialog;
import com.weigan.loopview.DateTimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimeLoopPickerDialog extends BaseDialog implements TimePickerMvpView {

    private static final String TAG = "TimeLoopPickerDialog";

    @Inject
    TimePickerMvpPresenter<TimePickerMvpView> mPresenter;

    @BindView(R.id.btn_submit)
    TextView mSubmitButton;

    @BindView(R.id.btn_cancel)
    TextView mCancelButton;

    @BindView(R.id.date_time_loopView)
    DateTimePickerView timePickerView;

    String reminderTime = "";
    int typeExpired;

    private RefreshListener mListener;

    public void setListener(RefreshListener listener) {
        mListener = listener;
    }

    public static TimeLoopPickerDialog newInstance(String time, int expired) {
        TimeLoopPickerDialog fragment = new TimeLoopPickerDialog();
        Bundle bundle = new Bundle();
        bundle.putString("reminder_time", time);
        bundle.putInt("expiration_type", expired);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.time_picker_fragment, container, false);

        if (getArguments() != null) {

            reminderTime = getArguments().getString("reminder_time");
            typeExpired = getArguments().getInt("expiration_type"); //  For Expiring : 0 and For Expired : 1


        }

        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);

            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }


    @Override
    protected void setUp(View view) {
        timePickerView.showDate(false);
        mPresenter.onViewPrepared();
    }

    @OnClick(R.id.btn_cancel)
    void onCancelClick() {
        mPresenter.onCancelClicked();
    }


    @OnClick(R.id.btn_submit)
    void setReminderTime() {

        String date = timePickerView.getSelectedDate();


        reminderTime = convertToUTC(date);

        mPresenter.updateTime(reminderTime, typeExpired);

    }


    @Override
    public void dismissDialog() {
        super.dismissDialog(TAG);
        getBaseActivity().hideKeyboard();
    }

    @Override
    public void setTimeInTimePicker() {

        SimpleDateFormat COMING_FORMAT = new SimpleDateFormat("hh:mm a");

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

        Date date = null;

        try {
            date = COMING_FORMAT.parse(reminderTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        String convertedDate = DATE_FORMAT.format(date);


        timePickerView.setDateTime(convertedDate);

    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void refreshData() {
        mListener.refreshData();
    }

    public static interface RefreshListener {
        void refreshData();
    }

    private String convertToUTC(String time){

        //2019-08-27 11:10:15 +0000

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);



        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = DATE_FORMAT.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        String formattedDate = df.format(date);

        return formattedDate;



    }

}
