package com.mcn.honeydew.ui.addItems;


import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.addItems.addItemsRecentChild.AddRecentItemsChildFragment;
import com.mcn.honeydew.ui.addItems.addItemsWhen.AddItemsWhenFragment;
import com.mcn.honeydew.ui.addItems.addItemsWhere.AddItemsWhereFragment;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.utils.AppConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by gkumar on 5/3/18.
 */

public class AddItemsFragment extends BaseFragment implements AddItemsMvpView, BaseActivity.LocationSettingsCallback {

    @Inject
    AddItemsMvpPresenter<AddItemsMvpView> mPresenter;

    @BindView(R.id.when_button)
    TextView mWhenButton;

    @BindView(R.id.where_button)
    TextView mWhereButton;

    @BindView(R.id.done_button)
    TextView mDoneButton;

    @BindView(R.id.left_arrow_image)
    ImageView leftArrowImageView;

    @BindView(R.id.location_img)
    ImageView locationImageView;

    public Location mcurrent;
    private View view;
    private int listId;

    private String latitude = "";
    private String longitude = "";

    private String location = "";
    private String itemTime = "";
    private String listName = "";
    private int statusId = 0;
    private int itemId = 0;
    public String ItemName = "";

    public String photo;

    public String dateTimeText = "";

    public static String WHEN_TAG = "when";

    public static String WHERE_TAG = "where";

    public static String RECENT_TAG = "recent";

    public static String KEY_LATITUDE = "latitude";

    public static String KEY_LONGITUDE = "longitude";


    public Location CurrentLocation;


    SimpleDateFormat formatter = new SimpleDateFormat("M/dd/yyyy hh:mm:ss a");
    SimpleDateFormat newFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");

    MyListResponseData myListData = null;

    private String filePath;

    public static AddItemsFragment newInstance(MyListResponseData data) {
        Bundle args = new Bundle();
        args.putString(AppConstants.KEY_MY_LIST_DATA, new Gson().toJson(data));
        AddItemsFragment fragment = new AddItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.add_items_fragment_layout, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {

            if (getArguments() != null) {

                myListData = new Gson().fromJson(getArguments().getString(AppConstants.KEY_MY_LIST_DATA), MyListResponseData.class);
                listId = myListData.getListId();
                listName = myListData.getListName();
                if (myListData.getItemName() != null) {
                    setItemName(myListData.getItemName());
                }

                if (myListData.getLocation() != null) {
                    setLocation(myListData.getLocation());
                }
                if (myListData.getItemTime() != null) {
                    Date date = null;
                    try {
                        date = formatter.parse(myListData.getItemTime());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    String finalString = newFormat.format(date);
                    setDateTimeText(finalString);
                }

                if(myListData.getPhoto()!=null){
                    setPhoto(myListData.getPhoto());
                }else {
                    setPhoto("");
                }
            }

            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
            setUp(view);


        }
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();

        getBaseActivity().setLocationCallback(this);
        if (getBaseActivity().getCurrentLocation() == null) {
            getBaseActivity().fetchCurrentLocation(true);
            return;
        } else {
            mcurrent = getBaseActivity().getCurrentLocation();
            setCurrentLocation(mcurrent);
        }
    }

    @Override
    protected void setUp(View view) {


        if (myListData.getItemId() != 0) {
            leftArrowImageView.setVisibility(View.VISIBLE);
        } else {
            leftArrowImageView.setVisibility(View.GONE);
        }

        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.add_items_content_frame, AddRecentItemsChildFragment.newInstance(), RECENT_TAG);
        ft.commit();

    }

    /**
     * "when" button click
     */
    @OnClick(R.id.when_button)
    public void onWhenButtonClick() {

        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.add_items_content_frame);
        if (currentFragment.getTag().equalsIgnoreCase(WHERE_TAG)) {

            if (((AddItemsWhereFragment) currentFragment) != null && ((AddItemsWhereFragment) currentFragment).isVisible()) {
                if (getLocation().equals("")) {
                    showDialog(getResources().getString(R.string.location_selection_text_empty_message));
                    return;
                }
            }


        }


        if (getItemName().equals("")) {
            showDialog(getResources().getString(R.string.popular_recent_text_empty_message));
            return;

        }

        mWhenButton.setVisibility(View.GONE);
        mWhereButton.setVisibility(View.VISIBLE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        locationImageView.setVisibility(View.GONE);
        Fragment fragment = AddItemsWhenFragment.newInstance();
        onFragmentWhereTransaction(fragment, WHEN_TAG);

    }


    /**
     * "done" button click
     */
    @OnClick(R.id.done_button)
    public void onDoneButtonClicked() {

        Log.e("done clicked", "yes");

        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.add_items_content_frame);
        if (currentFragment.getTag().equalsIgnoreCase(WHERE_TAG)) {

            if (((AddItemsWhereFragment) currentFragment) != null && ((AddItemsWhereFragment) currentFragment).isVisible()) {
                ((AddItemsWhereFragment) currentFragment).onDoneClicked();
            }


        } else if (currentFragment.getTag().equalsIgnoreCase(RECENT_TAG)) {
            if (((AddRecentItemsChildFragment) currentFragment) != null && ((AddRecentItemsChildFragment) currentFragment).isVisible()) {
                ((AddRecentItemsChildFragment) currentFragment).onDoneRecentClicked();
            }

        } else if (currentFragment.getTag().equalsIgnoreCase(WHEN_TAG)) {

            if (((AddItemsWhenFragment) currentFragment) != null && ((AddItemsWhenFragment) currentFragment).isVisible()) {
                String date = ((AddItemsWhenFragment) currentFragment).getStringDate();
                setDateTimeText(date);
                ((AddItemsWhenFragment) currentFragment).OnDoneWhenClicked(listId, listName);
            }
        }


    }

    /**
     * "where" button click
     */
    @OnClick(R.id.where_button)
    public void onWhereButtonClicked() {

        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.add_items_content_frame);
        if (currentFragment.getTag().equalsIgnoreCase(WHEN_TAG)) {

            if (((AddItemsWhenFragment) currentFragment) != null && ((AddItemsWhenFragment) currentFragment).isVisible()) {
                String date = ((AddItemsWhenFragment) currentFragment).getStringDate();
                setDateTimeText(date);

                if (getDateTimeText().equals("")) {
                    showDialog(getResources().getString(R.string.date_empty_message));
                    return;
                }
            }


        }

        if (getItemName().equals("")) {
            showDialog(getResources().getString(R.string.popular_recent_text_empty_message));
            return;

        }

        if (((BaseActivity) getActivity()).getCurrentLocation() == null) {
            ((BaseActivity) getActivity()).fetchCurrentLocation(true);
            return;
        } else {
            mcurrent = ((BaseActivity) getActivity()).getCurrentLocation();
            setCurrentLocation(mcurrent);
        }

        mWhenButton.setVisibility(View.VISIBLE);
        locationImageView.setVisibility(View.VISIBLE);
        leftArrowImageView.setVisibility(View.VISIBLE);
        mWhereButton.setVisibility(View.GONE);


        Fragment fragment = AddItemsWhereFragment.newInstance(mcurrent.getLatitude(), mcurrent.getLongitude(), listId, listName);
        onFragmentWhereTransaction(fragment, WHERE_TAG);

    }

    /**
     * "Left arrow" button click
     */
    @OnClick(R.id.left_arrow_image)
    public void onLeftArrowImageClicked() {

        Fragment currentFragment = getChildFragmentManager().findFragmentById(R.id.add_items_content_frame);
        if (currentFragment.getTag().equalsIgnoreCase(WHERE_TAG)) {

            if (((AddItemsWhereFragment) currentFragment) != null && ((AddItemsWhereFragment) currentFragment).isVisible()) {

                if (myListData.getLocation() == null) {
                    location = "";
                    setLocation("");

                }
                locationImageView.setVisibility(View.GONE);

            }


        } else if (currentFragment.getTag().equalsIgnoreCase(WHEN_TAG)) {

            if (((AddItemsWhenFragment) currentFragment) != null && ((AddItemsWhenFragment) currentFragment).isVisible()) {

                if (myListData.getItemTime() == null) {
                    dateTimeText = "";
                    setDateTimeText(dateTimeText);
                }

            }
        } else if (currentFragment.getTag().equalsIgnoreCase(RECENT_TAG)) {

            itemSuccesfullyAdded();
            return;

        }


        if (getChildFragmentManager().getBackStackEntryCount() > 1) {
            getChildFragmentManager().popBackStack();
        } else {
            getChildFragmentManager().popBackStack();
            if (myListData.getItemId() != 0) {
                leftArrowImageView.setVisibility(View.VISIBLE);
            } else {
                leftArrowImageView.setVisibility(View.GONE);
            }

            locationImageView.setVisibility(View.GONE);
            mWhereButton.setVisibility(View.VISIBLE);
            mWhenButton.setVisibility(View.VISIBLE);
        }

    }

    /**
     * "Location" button click
     */
    @OnClick(R.id.location_img)
    public void onLocationImageViewClicked() {

        if (mcurrent != null) {
            AddItemsWhereFragment fragment = (AddItemsWhereFragment) getChildFragmentManager().findFragmentByTag(WHERE_TAG);
            if (fragment != null && fragment.isVisible()) {
                fragment.onLocationImageViewClicked(mcurrent);
            }
        } else {
            ((BaseActivity) getActivity()).fetchCurrentLocation(true);
        }


    }


    private void toggleState(boolean var) {

        if (var) {
            mWhenButton.setVisibility(View.VISIBLE);
            // mRecyclerView.setVisibility(View.VISIBLE);
        } else {
            mWhenButton.setVisibility(View.GONE);
            //mRecyclerView.setVisibility(View.GONE);
        }
    }

    private void onFragmentWhereTransaction(Fragment fragment, String tag) {
        final FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        ft.replace(R.id.add_items_content_frame, fragment, tag);
        ft.addToBackStack(null);
        ft.commit();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    public void itemSuccesfullyAdded() {
        if (getBaseActivity() != null) {
            getBaseActivity().onItemsAddedInList();
        }
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getDateTimeText() {
        return dateTimeText;
    }

    public void setDateTimeText(String dateTimeText) {
        this.dateTimeText = dateTimeText;
    }


    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    private void showDialog(String Message) {
        //  "Please type or choose an item from most popular items list."

        AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
        dialog.setCancelable(false);
        dialog.setTitle(getActivity().getResources().getString(R.string.app_title));
        dialog.setMessage(Message);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });


        final AlertDialog alert = dialog.create();
        alert.show();


    }


    public Location getCurrentLocation() {
        return CurrentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        CurrentLocation = currentLocation;
    }

    public MyListResponseData getMyListData() {
        return myListData;
    }

    public void setMyListData(MyListResponseData myListData) {
        this.myListData = myListData;
    }

    public void setActionbarTitle(String title) {
        ((MainActivity) getActivity()).setTextToMain("");
        ((MainActivity) getActivity()).setTextToMain(title);
    }

    public void showLocationState() {

        locationImageView.setVisibility(View.VISIBLE);
        mWhenButton.setVisibility(View.VISIBLE);
        mWhereButton.setVisibility(View.GONE);
    }

    public void hideLocationState() {

        locationImageView.setVisibility(View.GONE);
        mWhenButton.setVisibility(View.GONE);
        mWhereButton.setVisibility(View.VISIBLE);
    }

    //---------------------------------
    @Override
    public void onLocationEnabled() {

    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onLocationFetched(Location location) {
        mcurrent = location;
    }

    @Override
    public void onLocationEnabledAlready() {

    }


    public void enableDisableDoneButton(boolean state) {
        if (mDoneButton != null) {
            mDoneButton.setEnabled(state);
        }

    }

    public void requestForCurrentLocation() {
        getBaseActivity().fetchCurrentLocation(true);
    }

    public Location getCurrentRequestedLocation(){
        mcurrent = getBaseActivity().getCurrentLocation();
        setCurrentLocation(mcurrent);
        return mcurrent;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
