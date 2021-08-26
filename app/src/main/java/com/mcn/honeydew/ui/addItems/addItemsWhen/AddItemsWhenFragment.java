package com.mcn.honeydew.ui.addItems.addItemsWhen;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.RecentLocationAddItemsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.ScreenUtils;
import com.weigan.loopview.DateTimePickerView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

    @BindView(R.id.choose_date_time_heading_text_view)
    TextView dateTimeHeadingTextView;

    @BindView(R.id.empty_space_view)
    View emptySpaceView;

    @BindView(R.id.date_time_loopView)
    DateTimePickerView dateTimePickerView;

    private int listId = 0;
    private String listName = "";
    @BindView(R.id.add_item_title_textview)
    TextView headingTextView;

    double screenInches;


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
        screenInches = ScreenUtils.getScreenSizeInInches(getBaseActivity());


       /* int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));
        emptySpaceView.getLayoutParams().height = (int) ((availiableHeight * 2.0) / 5.0);
        emptySpaceView.requestLayout();*/

        if(screenInches>=5.5){
            emptySpaceView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.add_items_when_fragment_empty_space_height_large_screen);
            emptySpaceView.requestLayout();
        }else {
            emptySpaceView.getLayoutParams().height = (int) getResources().getDimension(R.dimen.add_items_when_fragment_empty_space_height_small_screen);
            emptySpaceView.requestLayout();
        }

        dateTimePickerView.showDate(true);

     /*   int totalHeight = ScreenUtils.getScreenHeight(getActivity());
        int availiableHeight = (int) (totalHeight - (ScreenUtils.getStatusBarHeight(getActivity()) + (2 * (ScreenUtils.getActionBarHeight(getActivity())))));
        emptySpaceView.getLayoutParams().height = (int) ((availiableHeight * 2.0) / 5.0);
        emptySpaceView.requestLayout();
*/

        if (((AddItemsFragment) getParentFragment()).getMyListData().getItemName() != null) {
            headingTextView.setText("Edit Items");
        } else {
            headingTextView.setText(getString(R.string.recent_items_actionbar_heading));
        }
        if (((AddItemsFragment) getParentFragment()).getMyListData().getItemTime() != null) {
            // mEmptyTextView.setVisibility(View.GONE);
            // dateTextView.setVisibility(View.VISIBLE);
            // dateTextView.setText(((AddItemsFragment) getParentFragment()).getDateTimeText());

            if(!((AddItemsFragment) getParentFragment()).getMyListData().isShowExpired()){
                dateTimePickerView.setDateTime(((AddItemsFragment) getParentFragment()).getDateTimeText());
            }


        }
    }

    public void OnDoneWhenClicked(int listIds, String listNames) {

        AddItemsFragment frgment = ((AddItemsFragment) getParentFragment());
        listId = listIds;
        listName = listNames;


        String location = frgment.getLocation();
        int itemId = frgment.getMyListData().getItemId();

        String url = frgment.getFilePath();

        if (location == null || location.equals("")) {
            mPresenter.onAddItems(itemId, frgment.getItemName(), ((AddItemsFragment) getParentFragment()).getDateTimeText().toString().trim()
                    , "", listId, listName, "", "", frgment.getMyListData().getStatusId(), url);
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

        String url = fragment.getFilePath();

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
                location, lng, fragment.getMyListData().getStatusId(), url);
    }

    @Override
    public void addItemsCallFailed() {
        AddItemsFragment fragment = ((AddItemsFragment) getParentFragment());
        if (fragment != null) {
            fragment.enableDisableDoneButton(true);
        }
    }


    public String getStringDate() {
        if (dateTimePickerView != null) {

            String pickerTime =dateTimePickerView.getSelectedDate();

            String utcTime = convertToUTC(pickerTime);

            return utcTime;

        } else {
            return "";
        }
    }

    private String convertToUTC(String time) {

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);

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
