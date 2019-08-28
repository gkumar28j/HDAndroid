package com.mcn.honeydew.ui.addItems.addItemsWhen;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.DatePickerFragment;
import com.mcn.honeydew.utils.ScreenUtils;
import com.mcn.honeydew.utils.TimePickerFragment;
import com.weigan.loopview.DateTimePickerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gkumar on 9/3/18.
 */

public class AddItemsWhenFragment extends BaseFragment implements AddItemsWhenMvpView {

    public static AddItemsWhenFragment newInstance() {
        Bundle args = new Bundle();
        AddItemsWhenFragment fragment = new AddItemsWhenFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Inject
    AddItemsWhenMvpPresenter<AddItemsWhenMvpView> mPresenter;

    View view;

//    @BindView(R.id.date_time_text_view)
//    TextView dateTextView;

    @BindView(R.id.choose_date_time_heading_text_view)
    TextView dateTimeHeadingTextView;

    @BindView(R.id.empty_space_view)
    View emptySpaceView;

    @BindView(R.id.date_time_loopView)
    DateTimePickerView dateTimePickerView;

    private String itemTime = "";
    int year, month, day;
    private int listId = 0;
    private String listName = "";

//    @BindView(R.id.empty_date_time_text_view)
//    TextView mEmptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_items_when_fragment_lay, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            setUp(view);
        }
        return view;
    }

    @Override
    protected void setUp(View view) {

        dateTimePickerView.showDate(true);

        int totalHeight = ScreenUtils.getScreenHeight(getActivity());
        int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));
        emptySpaceView.getLayoutParams().height = (int) ((availiableHeight * 2.0) / 5.0);
        emptySpaceView.requestLayout();
        if (((AddItemsFragment) getParentFragment()).getMyListData().getItemTime() != null) {
            // mEmptyTextView.setVisibility(View.GONE);
            // dateTextView.setVisibility(View.VISIBLE);
            // dateTextView.setText(((AddItemsFragment) getParentFragment()).getDateTimeText());

            dateTimePickerView.setDateTime(((AddItemsFragment) getParentFragment()).getDateTimeText());
        }
        // showDatePicker();
    }


//    @OnClick(R.id.date_time_text_view)
//    public void onDateClicked() {
//        showDatePicker();
//    }
//
//    @OnClick(R.id.empty_date_time_text_view)
//    public void onEmptyViewSelected(){
//        showDatePicker();
//    }

    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        date.setCallBack(ondate);
        date.show(getChildFragmentManager(), "Date Picker");
    }

    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int years, int monthOfYear,
                              int dayOfMonth) {

            year = years;
            month = monthOfYear;
            day = dayOfMonth;

            showTimePicker();
        }
    };


    private void showTimePicker() {

        TimePickerFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setCallBack(onTimeSet);
        timePickerFragment.show(getChildFragmentManager(), "Time Picker");

    }

    TimePickerDialog.OnTimeSetListener onTimeSet = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month, day, hourOfDay, minute);
            SimpleDateFormat formatToShow = new SimpleDateFormat("d MMM yyyy, hh:mm a");

            SimpleDateFormat formatToSend = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
            //   ItemTime = "2018-03-13 03:10:00 PM";

            String date = formatToSend.format(cal.getTime());
            String dateToShow = formatToShow.format(cal.getTime());

            String dateToSend = null;
            if (date.contains("pm")) {
                dateToSend = date.replace("pm", "PM");
            } else if (date.contains("am")) {
                dateToSend = date.replace("am", "AM");
            }
            //   dateTextView.setVisibility(View.VISIBLE);
            //  dateTextView.setText(dateToShow);

            //  mEmptyTextView.setVisibility(View.GONE);
            if (dateToSend != null) {

                if (((AddItemsFragment) getParentFragment()) != null) {
                    ((AddItemsFragment) getParentFragment()).setDateTimeText(dateToSend);
                }
            } else {
                if (((AddItemsFragment) getParentFragment()) != null) {
                    ((AddItemsFragment) getParentFragment()).setDateTimeText(date);
                }
            }

        }
    };

    public void OnDoneWhenClicked(int listIds, String listNames) {

//        if (dateTextView.getText().toString().trim().equals("")) {
//            showLocationDialog();
//            return;
//        }
        AddItemsFragment frgment = ((AddItemsFragment) getParentFragment());
        //  frgment.enableDisableDoneButton(false);
        listId = listIds;
        listName = listNames;


        String location = frgment.getLocation();
        int itemId = frgment.getMyListData().getItemId();

        if (location == null || location.equals("")) {
            mPresenter.onAddItems(itemId, frgment.getItemName(), ((AddItemsFragment) getParentFragment()).getDateTimeText().toString().trim()
                    , "", listId, listName, "", "", frgment.getMyListData().getStatusId());
        } else {
            // Location loc = ((AddItemsFragment) getParentFragment()).getCurrentLocation();
            Location loc = ((AddItemsFragment) getParentFragment()).getCurrentRequestedLocation();
            if (loc != null) {
                mPresenter.fetchLatLng(location, loc.getLatitude(), loc.getLatitude(), 1000, AppConstants.PLACE_KEY);
            } else {
                //  Toast.makeText(getActivity(),"Please start location first then try adding/editing item.",Toast.LENGTH_LONG).show();
                ((AddItemsFragment) getParentFragment()).requestForCurrentLocation();
            }

        }

    }

    @Override
    public void itemSuccesfullyAdded() {

        ((AddItemsFragment) getParentFragment()).itemSuccesfullyAdded();
        ((MainActivity) getActivity()).syncItems();

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AddItemsFragment) getParentFragment()).hideLocationState();
        ((AddItemsFragment) getParentFragment()).setActionbarTitle(getResources().getString(R.string.recent_items_actionbar_heading));
    }

    @Override
    public void receiveLatLngs(List<RecentLocationAddItemsResponse.LocationResult> results) {
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        if (results.isEmpty()) {
            showMessage("We are not getting address on google map. Please enter valid address.");
            fragment.enableDisableDoneButton(true);
            return;
        }

        String DateTimeText = fragment.getDateTimeText();

        String itemName = fragment.getItemName();

        StringBuilder latitude = new StringBuilder();
        StringBuilder longitude = new StringBuilder();

        for (int i = 0; i < results.size(); i++) {
            latitude.append(results.get(i).getGeometry().getLocation().getLat());
            latitude.append(",");
            longitude.append(results.get(i).getGeometry().getLocation().getLng());
            longitude.append(",");


        }
        String lat = latitude.substring(0, latitude.length() - 1);
        String lng = longitude.substring(0, longitude.length() - 1);
        String location = fragment.getLocation();
        int itemId = fragment.getMyListData().getItemId();
        mPresenter.onAddItems(itemId, itemName, DateTimeText, lat, listId, listName,
                location, lng, fragment.getMyListData().getStatusId());
    }

    @Override
    public void addItemsCallFailed() {
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        if (fragment != null) {
            fragment.enableDisableDoneButton(true);
        }
    }

    private void showLocationDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage(getResources().getString(R.string.date_empty_message));
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();


    }

    public String getStringDate() {
        if (dateTimePickerView != null) {
            return dateTimePickerView.getSelectedDate();
        } else {
            return "";
        }
    }
}
