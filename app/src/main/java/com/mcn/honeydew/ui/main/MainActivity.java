package com.mcn.honeydew.ui.main;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.services.ProximityJobIntentService;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.addlist.AddListActivity;
import com.mcn.honeydew.ui.addlist.AddListFragment;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsFragment;
import com.mcn.honeydew.ui.common_app_settings.CommonAppSettingsFragment;
import com.mcn.honeydew.ui.home.HomeListFragment;
import com.mcn.honeydew.ui.list_settings.ListSettingsFragment;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.myList.MyListFragment;
import com.mcn.honeydew.ui.my_account.MyAccountFragment;
import com.mcn.honeydew.ui.notifications.NotificationsFragment;
import com.mcn.honeydew.ui.settings.SettingsFragment;
import com.mcn.honeydew.ui.sharelist.ShareListFragment;
import com.mcn.honeydew.utils.AppConstants;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.mcn.honeydew.services.MyFirebaseMessagingService.IS_IN_PROGRESS;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.IS_OWNER;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_LIST_ID;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_NOTIFICATION_TYPE;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.LIST_HEADER_COLOR;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.LIST_NAME;


public class MainActivity extends BaseActivity implements MainMvpView, BaseActivity.LocationSettingsCallback {
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private static final String SELECTED_ITEM = "selected_item";
    private static final String LIST_ITEM_ADD = "list_item_add";
    private static final String LIST_ITEM_EDIT = "list_item_edit";
    private static final String LIST_ITEM_IS_EDIT = "list_item_is_edit";

    private boolean isEdit = false;
    private boolean mIsJustLoggedIn = true;
    public String headerColor = "";

    @Inject
    MainMvpPresenter<MainMvpView> mPresenter;

    @BindView(R.id.title)
    public TextView title;

    @BindView(R.id.logo)
    ImageView logoImageView;

    @BindView(R.id.navigation)
    BottomNavigationView navigation;

    @BindView(R.id.setting_image)
    ImageView settingImageView;

    @BindView(R.id.list_setting_image)
    ImageView listSettingImageView;

    private MenuItem menuItemSelected;
    private int mMenuItemSelected;

    Fragment fragment = null;

    @BindView(R.id.back_image)
    ImageView backImageView;

    private MyListResponseData mAddItemData = null;
    private MyListResponseData mEditItemData = null;
    private Timer updatenotificationtimer;
    private UpdateNotificationTask myTimerTask;
    private static long UPDATE_START_TIME = 2 * 1000;
    private static long UPDATE_REFRESH_TIME = 10 * 1000;

    public static int ADD_LIST_ACTIVITY_REQUEST_CODE = 608;
    public static String ADD_LIST_ACTIVITY_DATA = "addlistdata";

    View badge;

    private boolean isComingFromNotifications = false;

    int count;
    NotificationCountBroadcastReceiver receiver;
    IntentFilter intentFilter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }

    TextView notificationTextView;


    private BottomNavigationView.OnNavigationItemSelectedListener   mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {


            if (item.getItemId() == R.id.navigation_add_list) {
                Intent intent = AddListActivity.getStartIntent(MainActivity.this);
                startActivityForResult(intent, ADD_LIST_ACTIVITY_REQUEST_CODE);
                return false;
            }
            selectFragment(item);
            if (item.getItemId() == R.id.navigation_list_detail_home) {
                return false;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);
        //   initFirebaseInstanceId();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            if (getIntent().getAction() != null && getIntent().getAction().equals(GeoFenceFilterService.ACTION_NOTIFICATION)) {
                navigation.inflateMenu(R.menu.navigation);
                menuItemSelected = navigation.getMenu().getItem(1);
                //    BottomNavigationViewHelper.disableShiftMode(navigation);
                navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                navigation.setItemIconTintList(null);
                navigation.setSelectedItemId(menuItemSelected.getItemId());
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                selectFragment(menuItemSelected);
                setUp();

                mPresenter.checkLoginSession();

                return;
            }
        }

       /* if (savedInstanceState != null) {
            mMenuItemSelected = savedInstanceState.getInt(SELECTED_ITEM, 0);
            if (mMenuItemSelected == R.id.navigation_color_settings ||
                    mMenuItemSelected == R.id.navigation_share_list ||
                    mMenuItemSelected == R.id.navigation_add_item ||
                    mMenuItemSelected == R.id.navigation_my_list) {
                removeBadge();
                navigation.inflateMenu(R.menu.navigation_list_detail);
            } else {
                navigation.inflateMenu(R.menu.navigation);

            }
            Gson gson = new Gson();
            isEdit = savedInstanceState.getBoolean(LIST_ITEM_IS_EDIT, false);
            String addData = savedInstanceState.getString(LIST_ITEM_ADD, null);
            String editData = savedInstanceState.getString(LIST_ITEM_EDIT, null);
            mAddItemData = gson.fromJson(addData, MyListResponseData.class);
            mEditItemData = gson.fromJson(editData, MyListResponseData.class);

            menuItemSelected = navigation.getMenu().findItem(mMenuItemSelected);
            //    BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            clippPadding(navigation);
            navigation.setItemIconTintList(null);
            navigation.setSelectedItemId(menuItemSelected.getItemId());
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        } else {*/

        navigation.inflateMenu(R.menu.navigation);
        clippPadding(navigation);
        menuItemSelected = navigation.getMenu().getItem(2);
        //    BottomNavigationViewHelper.disableShiftMode(navigation);

        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setItemIconTintList(null);
        navigation.setSelectedItemId(menuItemSelected.getItemId());
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        /*  }
         */

        selectFragment(menuItemSelected);
        //    initHomeFragment();
        setUp();

        mPresenter.checkLoginSession();

        mPresenter.checkBluetoothConnectivity();

        mPresenter.fetchNotificationCount();
    }

    @Override
    protected void setUp() {
        mPresenter.onViewPrepared(this);
        setLocationCallback(this);

        if (mPresenter.isIsProximityNotification()) {

            startService(GeoFenceFilterService.getStartIntent(this));
            syncItems();
        }
        createReceiver();
    }

    @OnClick(R.id.logo)
    void onHomeLogoClick() {
        if (fragment != null && !(fragment instanceof HomeListFragment)) {
            onBackPressed();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        //  stopUpdateNotificationTimer();
        unregisterNotifReceiver();
    }

    /*@Override
    protected void onSaveInstanceState(Bundle outState) {
     *//*   Gson gson = new Gson();
        String mAdddata = gson.toJson(mAddItemData);
        String mEditdata = gson.toJson(mEditItemData);
        outState.putInt(SELECTED_ITEM, mMenuItemSelected);
        outState.putString(LIST_ITEM_ADD, mAdddata);
        outState.putString(LIST_ITEM_EDIT, mEditdata);
        outState.putBoolean(LIST_ITEM_IS_EDIT, isEdit);*//*
        super.onSaveInstanceState(outState);
    }*/

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);

      /*  if (intent.getAction() != null && intent.getAction().equals(GeoFenceFilterService.ACTION_NOTIFICATION)) {

            if(menuItemSelected.getItemId() == R.id.sett )

            //navigation.inflateMenu(R.menu.navigation);
            menuItemSelected = navigation.getMenu().getItem(1);
            //BottomNavigationViewHelper.disableShiftMode(navigation);
            //navigation.setItemIconTintList(null);
            navigation.setSelectedItemId(menuItemSelected.getItemId());
            //navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

            selectFragment(menuItemSelected);
            //setUp();

            //mPresenter.checkLoginSession();
        }*/
    }


    @Override
    protected void onResume() {
        super.onResume();
        //  startUpdateNotificationTimer();
        registerNotifCountReceiver();
    }

    private void selectFragment(MenuItem item) {

        backImageView.setVisibility(View.GONE);
        menuItemSelected = item;
        mMenuItemSelected = menuItemSelected.getItemId();

        switch (item.getItemId()) {
        /*    case R.id.navigation_settings:
                fragment = SettingsFragment.newInstance();
                title.setText("Profile");
                title.setVisibility(View.VISIBLE);
                break;*/
            case R.id.navigation_notifications:
                title.setText("Notifications");
                title.setVisibility(View.VISIBLE);
                title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                fragment = NotificationsFragment.newInstance();
                break;
            case R.id.navigation_add_list:
                title.setText("");
                title.setVisibility(View.GONE);
                title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                fragment = AddListFragment.newInstance();
                break;
            case R.id.navigation_home:
                initCount();
                title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                title.setText("");
                title.setVisibility(View.GONE);
                settingImageView.setVisibility(View.VISIBLE);
                listSettingImageView.setVisibility(View.GONE);


                if (fragment instanceof HomeListFragment) {

                    ((HomeListFragment) fragment).scrollToTop();

                    return;

                }

                fragment = HomeListFragment.newInstance();

                break;
            case R.id.navigation_list_detail_home:
                getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                title.setText("");
                title.setVisibility(View.VISIBLE);
                isEdit = false;
                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                navigation.getMenu().clear();
                navigation.inflateMenu(R.menu.navigation);
                navigation.setItemIconTintList(null);
                clippPadding(navigation);
                if (isComingFromNotifications) {
                    navigation.setSelectedItemId(R.id.navigation_notifications);
                } else {
                    navigation.setSelectedItemId(R.id.navigation_home);
                }

                //    BottomNavigationViewHelper.disableShiftMode(navigation);
                navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                break;
            case R.id.navigation_my_list:
                listSettingImageView.setVisibility(View.VISIBLE);
                settingImageView.setVisibility(View.GONE);
                title.setText("'" + mAddItemData.getListName() + "'");
               /* if (isEdit) {
                    editItemsFromList(mEditItemData);
                } else {*/
                title.setVisibility(View.VISIBLE);
                title.setBackgroundColor(Color.parseColor(headerColor));
                if (fragment instanceof MyListFragment) {

                    ((MyListFragment) fragment).scrollToTop();

                    return;

                }
                fragment = MyListFragment.newInstance(mAddItemData.getListId(), mAddItemData.getListName());
                //    }
                break;

            case R.id.navigation_add_item:
                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                title.setText("");
                title.setText("'" + mAddItemData.getListName() + "'");
                //   title.setText(getResources().getString(R.string.recent_items_actionbar_heading));
                title.setVisibility(View.VISIBLE);
                title.setBackgroundColor(Color.parseColor(headerColor));
                MyListResponseData data = new MyListResponseData();
                data.setListName(this.mAddItemData.getListName());
                data.setListId(this.mAddItemData.getListId());
                fragment = AddItemsFragment.newInstance(data);

                break;
            case R.id.navigation_share_list:

                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                title.setText("");
                title.setText("'" + mAddItemData.getListName() + "'");
                title.setVisibility(View.VISIBLE);
                title.setBackgroundColor(Color.parseColor(headerColor));
                fragment = ShareListFragment.newInstance();
                break;
            case R.id.navigation_color_settings:
                settingImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.GONE);
                title.setText("");
                title.setText("'" + mAddItemData.getListName() + "'");
                title.setVisibility(View.VISIBLE);
                title.setBackgroundColor(Color.parseColor(headerColor));
                fragment = ColorSettingsFragment.newInstance(mAddItemData.getListId(), mAddItemData.getListHeaderColor());
                break;
        }


        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    @Override
    public void onListItemClick(MyHomeListData data) {

        if (!data.getListHeaderColor().startsWith("#")) {
            headerColor = "#".concat(data.getListHeaderColor());
        } else
            headerColor = data.getListHeaderColor();
        navigation = findViewById(R.id.navigation);
        isComingFromNotifications = false;
        showMyListFragment(data);


    }


    // navigate to the addItemsFragment from myList when items are empty
    @Override
    public void onAddItemsClicked(MyListResponseData data) {
        //  mAddItemData = data;
        navigation.setSelectedItemId(R.id.navigation_add_item);

    }

    // coming from edit of mylist
    @Override
    public void editItemsFromList(MyListResponseData data) {
        settingImageView.setVisibility(View.GONE);
        listSettingImageView.setVisibility(View.GONE);
        title.setText("'" + data.getListName() + "'");
        title.setVisibility(View.VISIBLE);
        mEditItemData = data;
        fragment = AddItemsFragment.newInstance(mEditItemData);
        isEdit = true;
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }


    }

    @Override
    public void onListSharedSuccess() {
        navigation.setSelectedItemId(R.id.navigation_my_list);
    }

    @Override
    public void onResetNotification() {
        this.count = 0;
        removeBadge();

    }

    @Override
    public void onNotificationClicked(String color, String listName, int listId, boolean isOwner, int inProgress) {
        if (!color.startsWith("#")) {
            headerColor = "#".concat(color);
        } else
            headerColor = color;

        isComingFromNotifications = true;
        navigateToListFragment(color, listName, listId, isOwner, inProgress);

    }


    // going back to my list when item created
    @Override
    public void onItemsAddedInList() {
        isEdit = false;
        menuItemSelected = navigation.getMenu().getItem(3);
        navigation.setSelectedItemId(menuItemSelected.getItemId());
    }

    @Override
    public void onEmptyViewClicked() {
        removeBadge();
        menuItemSelected = navigation.getMenu().getItem(2);
        navigation.setSelectedItemId(menuItemSelected.getItemId());
        fragment = AddListFragment.newInstance();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_LIST_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                if (data != null && data.hasExtra(ADD_LIST_ACTIVITY_DATA)) {

                    MyHomeListData newlyCreatedListData = new Gson().fromJson(data.getStringExtra(ADD_LIST_ACTIVITY_DATA), MyHomeListData.class);

                    onListItemClick(newlyCreatedListData);

                }


            }


        } else {
            if (fragment != null) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

    }

    @Override
    protected void onDestroy() {

        mPresenter.onDetach();
        super.onDestroy();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    public void onBackPressed() {
        Log.e("back pressed", "true");

        if (mMenuItemSelected == R.id.navigation_home) {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

                onBackClicked();
                return;

            }

        } else {
            getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        }

        isEdit = false;
        if (mMenuItemSelected == R.id.navigation_color_settings ||
                mMenuItemSelected == R.id.navigation_share_list ||
                mMenuItemSelected == R.id.navigation_add_item ||
                mMenuItemSelected == R.id.navigation_my_list) {
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.navigation);
            navigation.setItemIconTintList(null);
            clippPadding(navigation);
            //    BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }

        MenuItem homeItem = navigation.getMenu().getItem(2);

        if (mMenuItemSelected != homeItem.getItemId()) {
            navigation.setSelectedItemId(homeItem.getItemId());
        } else {
            super.onBackPressed();
        }
    }

    public void showTabs() {
        navigation.setVisibility(View.VISIBLE);

    }

    public void updateColorCode(String colorCode) {
        mAddItemData.setListHeaderColor(colorCode);
        if (!colorCode.startsWith("#")) {
            headerColor = "#".concat(colorCode);
        } else {
            headerColor = colorCode;
        }

        title.setBackgroundColor(Color.parseColor(headerColor));
    }

    public void hideTabs() {
        navigation.setVisibility(View.GONE);
    }

    public void setTextToMain(String message) {

        title.setText(message);

    }


    public void requestProximityItems() {
        syncItems();
    }

    public void requestToClearProximityItems() {
//        Log.e("amit", "main activity r");
//        Intent intent = new Intent();
//        intent.putExtra(ProximityJobIntentService.KEY_DELETE_REQUIRED, true);
//        ProximityJobIntentService.enqueueWork(this, intent);
    }

    @Override
    public void showMyListFragment(MyHomeListData data) {
        //    stopUpdateNotificationTimer();
        removeBadge();
        if (!data.getListHeaderColor().startsWith("#")) {
            headerColor = "#".concat(data.getListHeaderColor());
        } else
            headerColor = data.getListHeaderColor();

        mPresenter.saveInProgressValue(data.isInProgress());

        MyListResponseData mData = new MyListResponseData();
        mData.setListHeaderColor(data.getListHeaderColor());
        mData.setListName(data.getListName());
        mData.setListId(data.getListId());
        mData.setOwner(data.isIsOwner());
        mAddItemData = mData;
        mPresenter.saveSelectedList(data);
        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.navigation_list_detail);
        navigation.setItemIconTintList(null);
        clippPadding(navigation);
        navigation.setSelectedItemId(R.id.navigation_my_list);
        //    BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


    }

    public void navigateToListFragment(String listHeadingColor, String listName, int listId, boolean isOwner, int inProgress) {
        //    stopUpdateNotificationTimer();
        removeBadge();
        if (inProgress == 1) {
            mPresenter.saveInProgressValue(true);
        } else {
            mPresenter.saveInProgressValue(false);
        }
        MyListResponseData mData = new MyListResponseData();
        mData.setListHeaderColor(listHeadingColor);
        mData.setListName(listName);
        mData.setListId(listId);
        mData.setOwner(isOwner);
        mAddItemData = mData;
        //  mPresenter.saveSelectedList(data);
        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.navigation_list_detail);
        navigation.setItemIconTintList(null);
        clippPadding(navigation);
        navigation.setSelectedItemId(R.id.navigation_my_list);
        //    BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(MainActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void onResumeSuccess(boolean isJustLoggedIn) {
        mIsJustLoggedIn = isJustLoggedIn;

        if (mPresenter.isIsProximityNotification()) {
            this.checkProximityPermission();
        }

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle bundle = getIntent().getExtras();
            if (bundle.containsKey(KEY_NOTIFICATION_TYPE)) {

                MyHomeListData mData = new MyHomeListData();
                mData.setListHeaderColor(bundle.getString(LIST_HEADER_COLOR));
                mData.setListName(bundle.getString(LIST_NAME));
                mData.setListId(Integer.valueOf(bundle.getString(KEY_LIST_ID)));
                if (bundle.containsKey(IS_OWNER))
                    mData.setIsOwner(bundle.getString(IS_OWNER).equals("1"));

                if (bundle.containsKey(IS_IN_PROGRESS))
                    mData.setIsOwner(bundle.getString(IS_IN_PROGRESS).equals("1"));
                showMyListFragment(mData);
                getIntent().getExtras().remove(KEY_NOTIFICATION_TYPE);

            }

            // Clearing intent data:
            // Modified by Ashish Tiwari
            getIntent().replaceExtras(new Bundle());
            getIntent().setData(null);
        }
    }

    @Override
    public void checkProximityPermission() {

        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED){

            fetchCurrentLocation(false);
         //   Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();

        }else {

           /*  boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

             if(shouldProvideRationale){

                 // if true than never asks for permission before



                 ActivityCompat.requestPermissions(MainActivity.this,
                         new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                         REQUEST_PERMISSIONS_REQUEST_CODE);

             }else {

                 Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();


             }*/

            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);


        }

       /* boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);*/

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
       /* if (shouldProvideRationale) {

            // user is confused and denied the permission
            *//*showProximityAlert(R.string.location_disabled_message, android.R.string.ok, false);*//*

        } else {

            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
        *//*    ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);*//*

        /// previously denied
     //   mPresenter.disableLocationReminders();
            mPresenter.updateProximitySettings(0, 2);



        }*/




    }

    @Override
    public void onBluetoothFoundConnected() {
        // Check if bluetooth notification is enabled? if enabled, call connect/disconnect API
        mPresenter.checkAndCallBluetoothApi();
    }

    @Override
    public void onNotificationFetched(int count) {

        if (count == 0) {
            removeBadge();

        } else {
            showBadge(count);
        }


    }


    public void syncItems() {
        if (!mPresenter.isIsProximityNotification()) {
            return;
        }

        Timber.log(1, "main activity s");
        ProximityJobIntentService.enqueueWork(this, new Intent());
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     */
    private void showProximityAlert(final int mainTextStringId, final int actionStringId, final boolean isSetting) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.app_title))
                .setMessage(mainTextStringId)
                .setPositiveButton(actionStringId, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (isSetting) {
                            Intent intent = new Intent();
                            intent.setAction(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package",
                                    BuildConfig.APPLICATION_ID, null);
                            intent.setData(uri);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    }
                })
                .show();
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

                showLocationAlertDialog();


            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                fetchCurrentLocation(false);


            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
               /* showProximityAlert(R.string.permission_denied_explanation, R.string.settings, true);*/

                 boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                 if(shouldProvideRationale){

                     showLocationAlertDialog();

                 }else {
                     turnProximityOFFDialog();
                 }


            }
        }
    }


    @Override
    public void onLocationEnabled() {
        syncItems();

        if (mPresenter.isIsProximityNotification()) {
            startService(GeoFenceFilterService.getStartIntent(this));
        }
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onLocationFetched(Location location) {

    }

    @Override
    public void onLocationEnabledAlready() {
        if (mIsJustLoggedIn) {
            syncItems();

            if (mPresenter.isIsProximityNotification()) {
                startService(GeoFenceFilterService.getStartIntent(this));
            }
        }
    }


    @OnClick(R.id.setting_image)
    void onSettingImageClicked() {

        settingImageView.setVisibility(View.GONE);
        backImageView.setVisibility(View.VISIBLE);
        //fragment = SettingsFragment.newInstance();
        fragment = CommonAppSettingsFragment.newInstance();
        title.setText("App Settings");
        title.setVisibility(View.VISIBLE);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, "setting_fragment").addToBackStack(null).commit();
        }

    }

    @OnClick(R.id.list_setting_image)
    void onListSettingImageClicked() {

        settingImageView.setVisibility(View.GONE);
        listSettingImageView.setVisibility(View.GONE);

        backImageView.setVisibility(View.VISIBLE);
        //fragment = InProgressFragment.newInstance();
        fragment = ListSettingsFragment.newInstance(mAddItemData.getListId(), mAddItemData.getListHeaderColor(), mAddItemData.isOwner());
        //  title.setText("InProgress");
        //  title.setVisibility(View.VISIBLE);
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, "progress_fragment").addToBackStack(null).commit();
        }

    }


    @OnClick(R.id.back_image)
    public void onBackClicked() {
       /* backImageView.setVisibility(View.GONE);

        if (menuItemSelected.getItemId() == R.id.navigation_my_list) {
            settingImageView.setVisibility(View.GONE);
            listSettingImageView.setVisibility(View.VISIBLE);
        } else {
            title.setText("");
            title.setVisibility(View.GONE);
            settingImageView.setVisibility(View.VISIBLE);
            listSettingImageView.setVisibility(View.GONE);
        }*/


        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {

            if (getSupportFragmentManager().getBackStackEntryCount() == 1 && menuItemSelected.getItemId() == R.id.navigation_home) {
                title.setText("");
                title.setVisibility(View.GONE);
                backImageView.setVisibility(View.GONE);
                settingImageView.setVisibility(View.VISIBLE);

            } else if (getSupportFragmentManager().getBackStackEntryCount() == 1 && menuItemSelected.getItemId() == R.id.navigation_my_list) {
                settingImageView.setVisibility(View.GONE);
                backImageView.setVisibility(View.GONE);
                listSettingImageView.setVisibility(View.VISIBLE);
            }
            getSupportFragmentManager().popBackStack();
        } else {
            onBackPressed();
        }

    }


    public void removeBadge() {

      /*  if (notificationTextView == null) {
            return;
        }
        notificationTextView.setVisibility(View.GONE);
*/

        navigation.removeBadge(R.id.navigation_notifications);
    }

    private void showBadge(int count) {


        this.count = count;

        //    removeBadge();

        if (mMenuItemSelected == R.id.navigation_color_settings ||
                mMenuItemSelected == R.id.navigation_share_list ||
                mMenuItemSelected == R.id.navigation_add_item ||
                mMenuItemSelected == R.id.navigation_my_list) {

            return;

        }
     /*   if (notificationTextView == null) {
            return;
        }
        notificationTextView.setVisibility(View.VISIBLE);
        notificationTextView.setText(String.valueOf(count));*/

        setBadgeCount(count);
    }


    private void startUpdateNotificationTimer() {
        if (updatenotificationtimer != null) {
            updatenotificationtimer.cancel();
        }
        updatenotificationtimer = new Timer();
        myTimerTask = new UpdateNotificationTask();
        updatenotificationtimer.schedule(myTimerTask, UPDATE_START_TIME,
                UPDATE_REFRESH_TIME);
    }

    private class UpdateNotificationTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    mPresenter.fetchNotificationCount();

                }
            });
        }
    }

    private void stopUpdateNotificationTimer() {
        if (updatenotificationtimer != null) {
            updatenotificationtimer.cancel();
        }
        myTimerTask = null;

    }


    private void initCount() {

     /*   Log.e("init","showingg");

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) navigation.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(1);


        View badge = LayoutInflater.from(this).inflate(R.layout.notification_count_badge, itemView, false);

        itemView.addView(badge);
        notificationTextView = (TextView) badge.findViewById(R.id.badge_text_view);


        if (count > 0) {
            notificationTextView.setText(String.valueOf(count));
            notificationTextView.setVisibility(View.VISIBLE);
        }*/


        if (count > 0) {
            setBadgeCount(count);
        } else {
            navigation.removeBadge(R.id.navigation_notifications);
        }


    }


    void clippPadding(BottomNavigationView view) {

        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);

        for (int i = 0; i < menuView.getChildCount(); i++) {
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(i);
            View activeLabel = itemView.findViewById(R.id.largeLabel);
            if (activeLabel != null && activeLabel instanceof TextView) {
                ((TextView) activeLabel).setPadding(0, 0, 0, 0);
            }
        }

    }


    private void initFirebaseInstanceId() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FirebaseInstanceTask", "getInstanceId failed", task.getException());
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        Log.e("fcm token main : ", token);
                    }
                });
    }


    public void showHideTitle(boolean show) {

        if (show) {

            title.setVisibility(View.VISIBLE);

        } else {
            title.setVisibility(View.GONE);
        }

    }


    void setBadgeCount(int count) {

        BadgeDrawable drawable = navigation.getOrCreateBadge(R.id.navigation_notifications);
        drawable.setBackgroundColor(getResources().getColor(R.color.in_progress_color_background));
        drawable.setBadgeTextColor(getResources().getColor(R.color.white));
        drawable.setNumber(count);

    }

    void initHomeFragment() {
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        title.setText("");
        title.setVisibility(View.GONE);
        fragment = HomeListFragment.newInstance();
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).commit();
        }
    }

    private class NotificationCountBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(AppConstants.ACTION_REFRESH_NOTIF_COUNT)) {
                mPresenter.fetchNotificationCount();
            }

        }
    }


    private void createReceiver() {
        receiver = new NotificationCountBroadcastReceiver();
        intentFilter = new IntentFilter();
        intentFilter.addAction(AppConstants.ACTION_REFRESH_NOTIF_COUNT);
    }

    private void registerNotifCountReceiver() {

        if (receiver == null) {
            createReceiver();
        }

        this.registerReceiver(receiver, intentFilter);

    }

    private void unregisterNotifReceiver() {
        if (receiver != null) {
            this.unregisterReceiver(receiver);
        }

    }

    public void onAccountSettingclicked() {

        fragment = MyAccountFragment.newInstance();
        title.setText("Account");
        title.setVisibility(View.VISIBLE);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, "account_fragment").addToBackStack(null).commit();
        }


    }

    public void onRemindersClicked() {

        fragment = SettingsFragment.newInstance();
        title.setText("Reminders");
        title.setVisibility(View.VISIBLE);
        title.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        if (fragment != null) {

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, "account_fragment").addToBackStack(null).commit();
        }

    }

    public void changeTitle(String heading) {
        if (title != null) {
            title.setText("");
            title.setText(heading);
        }

    }


    void showLocationAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Warning")
                .setMessage("Turning off location access will disable ‘Location Reminders’")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();
                       /* if (isSetting) {

                        } else {
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }*/
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package",
                                BuildConfig.APPLICATION_ID, null);
                        intent.setData(uri);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    void turnProximityOFFDialog(){

        showMessage("Turning Location Reminders Off");
        mPresenter.updateProximitySettings(0,2);

    }


}
