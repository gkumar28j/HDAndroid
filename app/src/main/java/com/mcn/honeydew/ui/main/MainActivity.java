package com.mcn.honeydew.ui.main;


import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.services.ProximityJobIntentService;
import com.mcn.honeydew.ui.addItems.AddItemsFragment;
import com.mcn.honeydew.ui.addlist.AddListFragment;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.colorSettings.ColorSettingsFragment;
import com.mcn.honeydew.ui.home.HomeListFragment;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.myList.MyListFragment;
import com.mcn.honeydew.ui.notifications.NotificationsFragment;
import com.mcn.honeydew.ui.settings.SettingsFragment;
import com.mcn.honeydew.ui.sharelist.ShareListFragment;
import com.mcn.honeydew.ui.views.BottomNavigationViewHelper;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.mcn.honeydew.services.MyFirebaseMessagingService.IS_OWNER;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_LIST_ID;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_NOTIFICATION_TYPE;
import static com.mcn.honeydew.services.MyFirebaseMessagingService.KEY_SYNC_REQUIRED;
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


    private MenuItem menuItemSelected;
    private int mMenuItemSelected;

    Fragment fragment = null;

    private MyListResponseData mAddItemData = null;
    private MyListResponseData mEditItemData = null;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        return intent;
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getIntent() != null) {
            if (getIntent().getAction() != null && getIntent().getAction().equals(GeoFenceFilterService.ACTION_NOTIFICATION)) {
                navigation.inflateMenu(R.menu.navigation);
                menuItemSelected = navigation.getMenu().getItem(1);
                BottomNavigationViewHelper.disableShiftMode(navigation);
                navigation.setItemIconTintList(null);
                navigation.setSelectedItemId(menuItemSelected.getItemId());
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                selectFragment(menuItemSelected);
                setUp();

                mPresenter.checkLoginSession();

                return;
            }
        }

        if (savedInstanceState != null) {
            mMenuItemSelected = savedInstanceState.getInt(SELECTED_ITEM, 0);
            if (mMenuItemSelected == R.id.navigation_color_settings ||
                    mMenuItemSelected == R.id.navigation_share_list ||
                    mMenuItemSelected == R.id.navigation_add_item ||
                    mMenuItemSelected == R.id.navigation_my_list) {
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
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setItemIconTintList(null);
            navigation.setSelectedItemId(menuItemSelected.getItemId());
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        } else {

            navigation.inflateMenu(R.menu.navigation);
            menuItemSelected = navigation.getMenu().getItem(3);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setItemIconTintList(null);
            navigation.setSelectedItemId(menuItemSelected.getItemId());
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        }
        selectFragment(menuItemSelected);
        setUp();

        mPresenter.checkLoginSession();

        mPresenter.checkBluetoothConnectivity();

    }

    @Override
    protected void setUp() {
        mPresenter.onViewPrepared(this);
        setLocationCallback(this);

        if (mPresenter.isIsProximityNotification()) {

            startService(GeoFenceFilterService.getStartIntent(this));
            syncItems();
        }
    }

    @OnClick(R.id.logo)
    void onHomeLogoClick() {
        if (fragment != null && !(fragment instanceof HomeListFragment)) {
            onBackPressed();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        Gson gson = new Gson();
        String mAdddata = gson.toJson(mAddItemData);
        String mEditdata = gson.toJson(mEditItemData);
        outState.putInt(SELECTED_ITEM, mMenuItemSelected);
        outState.putString(LIST_ITEM_ADD, mAdddata);
        outState.putString(LIST_ITEM_EDIT, mEditdata);
        outState.putBoolean(LIST_ITEM_IS_EDIT, isEdit);
        super.onSaveInstanceState(outState);
    }

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

    }

    private void selectFragment(MenuItem item) {
        menuItemSelected = item;
        mMenuItemSelected = menuItemSelected.getItemId();

        switch (item.getItemId()) {
            case R.id.navigation_settings:
                fragment = SettingsFragment.newInstance();
                title.setText("Profile");
                title.setVisibility(View.VISIBLE);
                break;
            case R.id.navigation_notifications:
                title.setText("Notifications");
                title.setVisibility(View.VISIBLE);
                fragment = NotificationsFragment.newInstance();
                break;
            case R.id.navigation_add_list:
                title.setText("");
                title.setVisibility(View.VISIBLE);
                fragment = AddListFragment.newInstance();
                break;
            case R.id.navigation_home:
                title.setText("");
                title.setVisibility(View.GONE);
                fragment = HomeListFragment.newInstance();
                break;
            case R.id.navigation_list_detail_home:
                isEdit = false;
                navigation.getMenu().clear();
                navigation.inflateMenu(R.menu.navigation);
                navigation.setItemIconTintList(null);
                navigation.setSelectedItemId(R.id.navigation_home);
                BottomNavigationViewHelper.disableShiftMode(navigation);
                navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

                break;
            case R.id.navigation_my_list:
                if (isEdit) {
                    editItemsFromList(mEditItemData);
                } else {
                    title.setText(mAddItemData.getListName());
                    title.setVisibility(View.VISIBLE);
                    fragment = MyListFragment.newInstance(mAddItemData.getListId(), mAddItemData.getListName());
                }
                break;

            case R.id.navigation_add_item:
                title.setText(getResources().getString(R.string.recent_items_actionbar_heading));
                title.setVisibility(View.VISIBLE);
                MyListResponseData data = new MyListResponseData();
                data.setListName(this.mAddItemData.getListName());
                data.setListId(this.mAddItemData.getListId());
                fragment = AddItemsFragment.newInstance(data);

                break;
            case R.id.navigation_share_list:

                title.setText("Share list");
                title.setVisibility(View.VISIBLE);
                fragment = ShareListFragment.newInstance();
                break;
            case R.id.navigation_color_settings:
                title.setText("Color");
                title.setVisibility(View.VISIBLE);
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

        showMyListFragment(data);


    }


    // navigate to the addItemsFragment from myList when items are empty
    @Override
    public void onAddItemsClicked(MyListResponseData data) {
        mAddItemData = data;
        navigation.setSelectedItemId(R.id.navigation_add_item);

    }

    // coming from edit of mylist
    @Override
    public void editItemsFromList(MyListResponseData data) {

        mEditItemData = data;
        fragment = AddItemsFragment.newInstance(mEditItemData);
        title.setText("Edit Item");
        title.setVisibility(View.VISIBLE);
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


    // going back to my list when item created
    @Override
    public void onItemsAddedInList() {
        isEdit = false;
        menuItemSelected = navigation.getMenu().getItem(3);
        navigation.setSelectedItemId(menuItemSelected.getItemId());
    }

    @Override
    public void onEmptyViewClicked() {
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
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
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
        isEdit = false;
        if (mMenuItemSelected == R.id.navigation_color_settings ||
                mMenuItemSelected == R.id.navigation_share_list ||
                mMenuItemSelected == R.id.navigation_add_item ||
                mMenuItemSelected == R.id.navigation_my_list) {
            navigation.getMenu().clear();
            navigation.inflateMenu(R.menu.navigation);
            navigation.setItemIconTintList(null);
            BottomNavigationViewHelper.disableShiftMode(navigation);
            navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        }
        MenuItem homeItem = navigation.getMenu().getItem(3);
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

        MyListResponseData mData = new MyListResponseData();
        mData.setListHeaderColor(data.getListHeaderColor());
        mData.setListName(data.getListName());
        mData.setListId(data.getListId());
        mAddItemData = mData;
        mPresenter.saveSelectedList(data);
        navigation.getMenu().clear();
        navigation.inflateMenu(R.menu.navigation_list_detail);
        navigation.setItemIconTintList(null);
        navigation.setSelectedItemId(R.id.navigation_my_list);
        BottomNavigationViewHelper.disableShiftMode(navigation);
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

        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            showProximityAlert(R.string.location_disabled_message, android.R.string.ok, false);
        } else {

            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    @Override
    public void onBluetoothFoundConnected() {
        // Check if bluetooth notification is enabled? if enabled, call connect/disconnect API
        mPresenter.checkAndCallBluetoothApi();
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

        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.

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
                showProximityAlert(R.string.permission_denied_explanation, R.string.settings, true);

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

}
