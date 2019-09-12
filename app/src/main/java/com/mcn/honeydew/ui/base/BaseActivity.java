package com.mcn.honeydew.ui.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.mcn.honeydew.HoneyDewApp;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.MyHomeListData;
import com.mcn.honeydew.data.network.model.response.MyListResponseData;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.di.component.DaggerActivityComponent;
import com.mcn.honeydew.di.module.ActivityModule;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.NetworkUtils;

import butterknife.Unbinder;
import timber.log.Timber;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;

/**
 * Created by amit on 15/2/18.
 */

public abstract class BaseActivity extends AppCompatActivity implements MvpView, BaseFragment.Callback {

    private final static int REQUEST_CHECK_SETTINGS_GPS = 101;
    private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 102;
    private ProgressDialog progressDialog;
    private ActivityComponent mActivityComponent;
    private Unbinder mUnBinder;

    private LocationCallback mLocationCallback;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location mCurrentLocation;

    private LocationSettingsCallback callback;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityComponent = DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(HoneyDewApp.get(this).getComponent())
                .build();

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    public ActivityComponent getActivityComponent() {
        return mActivityComponent;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @TargetApi(Build.VERSION_CODES.M)
    public void requestPermissionsSafely(String[] permissions, int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, requestCode);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean hasPermission(String permission) {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.M ||
                checkSelfPermission(permission) == PERMISSION_GRANTED;
    }

    @Override
    public void showLoading() {
        hideLoading();
        progressDialog = CommonUtils.showLoadingDialog(this);
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.cancel();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS_GPS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        callback.onLocationEnabled();
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case RESULT_CANCELED:
                        callback.onCancelled();
                        //updateUI();
                        break;
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onError(String message) {
        if (message != null) {
            showSnackBar(message);
        } else {
            showSnackBar(getString(R.string.some_error));
        }
    }

    @Override
    public void showMessage(String message) {
        if (message != null) {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, getString(R.string.some_error), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showMessage(@StringRes int resId) {
        showMessage(getString(resId));
    }


    private void showSnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT);
        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
        textView.setTextColor(ContextCompat.getColor(this, R.color.white));
        snackbar.show();
    }

    @Override
    public void onError(@StringRes int resId) {
        onError(getString(resId));
    }

    @Override
    public boolean isNetworkConnected() {
        return NetworkUtils.isNetworkConnected(getApplicationContext());
    }

    @Override
    public void onFragmentAttached() {

    }

    @Override
    public void onFragmentDetached(String tag) {

    }

    @Override
    public void onListItemClick(MyHomeListData data) {

    }

    @Override
    public void onEmptyViewClicked() {

    }

    @Override
    public void onItemsAddedInList() {


    }

    @Override
    public void onAddItemsClicked(MyListResponseData data) {

    }


    public void editItemsFromList(MyListResponseData data) {


    }

    @Override
    public void onListSharedSuccess() {

    }

    @Override
    public void onResetNotification() {

    }


    @Override
    public void onNotificationClicked(String color, String listName, int listId, boolean isOwner, int inProgress) {

    }

    public void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public void openActivityOnTokenExpire() {
        startActivity(LoginActivity.getStartIntent(this));
        finish();
    }

    public void setUnBinder(Unbinder unBinder) {
        mUnBinder = unBinder;
    }

    @Override
    protected void onDestroy() {

        if (mUnBinder != null) {
            mUnBinder.unbind();
        }
        super.onDestroy();
    }

    protected abstract void setUp();

    // ------------------------------------------

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (callback != null) {

                    callback.onLocationFetched(locationResult.getLastLocation());
                    //  mCurrentLocation = locationResult.getLastLocation();
                    setCurrentLocation(locationResult.getLastLocation());

                    mFusedLocationClient.removeLocationUpdates(mLocationCallback);
                }


            }
        };
    }

    private void checkPermission(boolean toRequestLocationUpdate) {
        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION};

        if (hasPermission(permission[0])) {
            getMyLocation(toRequestLocationUpdate);
        } else {
            ActivityCompat.requestPermissions(this, permission, REQUEST_ID_MULTIPLE_PERMISSIONS);

        }
    }

    public void getMyLocation(final boolean toRequestLocationUpdate) {

        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {

            final LocationRequest mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(10000);
            mLocationRequest.setFastestInterval(5000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setSmallestDisplacement(100);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(this);

            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                @Override
                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    // All location settings are satisfied. The client can initialize
                    // location requests here.
                    // ...
                    Timber.e("location response " + locationSettingsResponse.toString());

                    // If need location data (lat long) on then request for
                    if (toRequestLocationUpdate) {
                        int permissionLocation = ContextCompat
                                .checkSelfPermission(BaseActivity.this,
                                        Manifest.permission.ACCESS_FINE_LOCATION);
                        if (permissionLocation == PERMISSION_GRANTED) {
                            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                    mLocationCallback, Looper.myLooper());
                        }
                    } else {
                        callback.onLocationEnabledAlready();
                    }


                }
            });

            task.addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (e instanceof ResolvableApiException) {
                        // Location settings are not satisfied, but this can be fixed
                        // by showing the user a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(BaseActivity.this,
                                    REQUEST_CHECK_SETTINGS_GPS);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }

                }
            });


        }

    }

    public Location getCurrentLocation() {
        return mCurrentLocation;
    }

    public void setCurrentLocation(Location mCurrentLocation) {
        this.mCurrentLocation = mCurrentLocation;
    }

    public void fetchCurrentLocation(boolean toRequestLocationUpdate) {
        checkPermission(toRequestLocationUpdate);
        createLocationCallback();
    }

    public void setLocationCallback(LocationSettingsCallback callback) {
        this.callback = callback;
    }

    public interface LocationSettingsCallback {
        void onLocationEnabled();

        void onCancelled();

        void onLocationFetched(Location location);

        void onLocationEnabledAlready();
    }
}

