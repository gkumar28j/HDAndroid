package com.mcn.honeydew.ui.register;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.data.network.model.UserModel;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 16/2/18.
 */

public class RegisterActivity extends BaseActivity implements RegisterMvpView {

    //private LocationCallback mLocationCallback;
    // private FusedLocationProviderClient mFusedLocationClient;
    // private Location mCurrentLocation;
    private ArrayList<Countries> mCountryList;
    private String mCountryCode;
    private int mCountryPosition;
    private UserModel mUserModel;

    // private final static int REQUEST_CHECK_SETTINGS_GPS = 101;
    // private final static int REQUEST_ID_MULTIPLE_PERMISSIONS = 102;


    @Inject
    RegisterMvpPresenter<RegisterMvpView> mPresenter;

    @BindView(R.id.text_sign_in)
    TextView signInTextView;

    @BindView(R.id.edit_first_name)
    EditText firstNameEditText;

    @BindView(R.id.edit_last_name)
    EditText lastNameEditText;

    @BindView(R.id.edit_email)
    EditText emailEditText;

    @BindView(R.id.edit_phone_number)
    EditText phoneNumberEditText;

    @BindView(R.id.edit_password)
    EditText passwordEditText;

    @BindView(R.id.edit_confirm_password)
    EditText confirmPasswordEditText;

    @BindView(R.id.text_country_code)
    TextView countryCodeTextView;

    @BindView(R.id.image_back)
    ImageView backImageView;

    public static Intent getRegisterIntent(Context context) {
        Intent intent = new Intent(context, RegisterActivity.class);
        return intent;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }*/

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        // mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        String fullText = getString(R.string.label_have_account);
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 17, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorLiteBlueText)), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                finish();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.white));

            }
        }, 17, fullText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        signInTextView.setText(builder);
        signInTextView.setMovementMethod(new LinkMovementMethod());


        // On Done pressed
        confirmPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((event != null && (actionId == EditorInfo.IME_ACTION_DONE))) {
                    getDataFromUiAndSubmit();
                }
                return false;
            }
        });


        // Checking location permission
        // checkPermission();
        createLocationCallback();

        mPresenter.onAttach(this);

        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case REQUEST_CHECK_SETTINGS_GPS:
//                switch (resultCode) {
//                    case Activity.RESULT_OK:
//                        getMyLocation();
//                        break;
//                    case Activity.RESULT_CANCELED:
//                        finish();
//                        break;
//                }
//                break;
//        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

//        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
//            getMyLocation();
//
//        }
    }


    @Override
    protected void setUp() {

        mPresenter.fetchCountries();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }


    @Override
    public void onRegistrationSuccess() {
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent();

        //String emailOrPhone = TextUtils.isEmpty(mUserModel.getEmail()) ? mUserModel.getPhoneNumber() : mUserModel.getEmail();

        intent.putExtra(AppConstants.KEY_PHONE, mUserModel.getPhoneNumber());
        intent.putExtra(AppConstants.KEY_PASSWORD, mUserModel.getPassword());
        intent.putExtra(AppConstants.KEY_COUNTRY_CODE, mUserModel.getDialCode());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onCountriesFetched(List<Countries> countries) {
        mCountryList = new ArrayList<>(countries);

        if (TextUtils.isEmpty(mCountryCode)) {
            TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            mCountryCode = getCountryIndexByName(manager.getNetworkCountryIso());
            countryCodeTextView.setText(mCountryCode);
        }
    }

    @OnClick(R.id.image_back)
    void onBackClicked() {
        finish();
    }

    @OnClick(R.id.text_sign_up)
    void onSignUpClicked() {
        getDataFromUiAndSubmit();
    }

    @OnClick(R.id.text_country_code)
    void onCountryCodeClicked() {
        showCountryDialog();
    }


    /* --------------------------------- PRIVATE METHODS ------------------------------------------*/
    private void createLocationCallback() {
//        mLocationCallback = new LocationCallback() {
//            @Override
//            public void onLocationResult(LocationResult locationResult) {
//                super.onLocationResult(locationResult);
//
//                mCurrentLocation = locationResult.getLastLocation();
//                mFusedLocationClient.removeLocationUpdates(mLocationCallback);
//                // updateLocationUI();
//
//            }
//        };
    }

    private void checkPermission() {
//        String[] permission = {android.Manifest.permission.ACCESS_FINE_LOCATION};
//
//        if (hasPermission(permission[0])) {
//            getMyLocation();
//        } else {
//            requestPermissionsSafely(permission, REQUEST_ID_MULTIPLE_PERMISSIONS);
//
//        }
    }

//    public void getMyLocation() {
//
//        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//            final LocationRequest mLocationRequest = new LocationRequest();
//            mLocationRequest.setInterval(10000);
//            mLocationRequest.setFastestInterval(5000);
//            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//
//            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
//                    .addLocationRequest(mLocationRequest);
//            SettingsClient client = LocationServices.getSettingsClient(this);
//            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
//
//            task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
//                @Override
//                public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
//                    // All location settings are satisfied. The client can initialize
//                    // location requests here.
//                    // ...
//                    Log.e("location response", locationSettingsResponse.toString());
//                    int permissionLocation = ContextCompat
//                            .checkSelfPermission(RegisterActivity.this,
//                                    Manifest.permission.ACCESS_FINE_LOCATION);
//                    if (permissionLocation == PackageManager.PERMISSION_GRANTED) {
//                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
//                                mLocationCallback, Looper.myLooper());
//                    }
//
//                }
//            });
//
//            task.addOnFailureListener(this, new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    if (e instanceof ResolvableApiException) {
//                        // Location settings are not satisfied, but this can be fixed
//                        // by showing the user a dialog.
//                        try {
//                            // Show the dialog by calling startResolutionForResult(),
//                            // and check the result in onActivityResult().
//                            ResolvableApiException resolvable = (ResolvableApiException) e;
//                            resolvable.startResolutionForResult(RegisterActivity.this,
//                                    REQUEST_CHECK_SETTINGS_GPS);
//                        } catch (IntentSender.SendIntentException sendEx) {
//                            // Ignore the error.
//                        }
//                    }
//                }
//            });
//        }
//
//    }

    private void getDataFromUiAndSubmit() {
        String firstName = firstNameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String phoneNumber = phoneNumberEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();
        String dialCode = countryCodeTextView.getText().toString();
        double latitude = 0;
        double longitude = 0;

//        if (mCurrentLocation != null) {
//            latitude = mCurrentLocation.getLatitude();
//            longitude = mCurrentLocation.getLongitude();
//        }

        UserModel userModel = new UserModel();
        userModel.setFirstName(firstName);
        userModel.setLastName(lastName);
        userModel.setEmail(email);
        userModel.setPhoneNumber(phoneNumber);
        userModel.setDialCode(dialCode);
        userModel.setPassword(password);
        userModel.setConfirmPassword(confirmPassword);

        userModel.setDeviceVersion(Build.VERSION.SDK_INT);
        userModel.setApiVersion(ApiCall.API_VERSION);
        try {
            userModel.setAppVersion(getPackageManager().getPackageInfo(getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            userModel.setAppVersion("");
            e.printStackTrace();
        }

        userModel.setDeviceId(CommonUtils.getDeviceId(this));
        userModel.setDeviceType(AppConstants.DEVICE_TYPE);
        userModel.setProfilePhoto("");
        userModel.setLatitude(latitude);
        userModel.setLongitude(longitude);

        userModel.setTimeZone(TimeZone.getDefault().getDisplayName());
        userModel.setOffsetTimeZone("");
        userModel.setTimeZoneOffsetName("");

        mUserModel = userModel;
        mPresenter.onSignUpClicked(mUserModel);
    }

    private void showCountryDialog() {

        CharSequence[] items = null;
        ArrayList<String> countryNames = new ArrayList<String>();
        ArrayList<String> countryCodes = new ArrayList<>();

        for (Countries country : mCountryList) {
            countryNames.add(country.getDialCode() + " (" + country.getName() + ")");
            countryCodes.add(country.getDialCode());
        }
        items = countryNames.toArray(new CharSequence[countryNames.size()]);

        // Getting position of the country based on country code.
        if (!TextUtils.isEmpty(mCountryCode) && countryCodes.contains(mCountryCode)) {
            mCountryPosition = countryCodes.indexOf(mCountryCode);
        }


        new AlertDialog.Builder(this).setTitle("Select Country")
                .setSingleChoiceItems(items, mCountryPosition, null)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        mCountryPosition = ((AlertDialog) dialog)
                                .getListView().getCheckedItemPosition();


                        if (mCountryPosition == AdapterView.INVALID_POSITION) {
                            return;
                        }

                        // Do something useful with the position of the
                        // selected radio button

                        String codeString = mCountryList.get(mCountryPosition).getDialCode();

                        countryCodeTextView.setText(codeString);

                    }
                }).show();
    }

    private String getCountryIndexByName(String isoCode) {
        Countries country;
        for (int i = 0; i < mCountryList.size(); i++) {
            if (mCountryList.get(i).getCode().equalsIgnoreCase(isoCode)) {
                country = mCountryList.get(i);
                mCountryCode = country.getDialCode();
                return mCountryCode;
            }
        }
        return "+1"; // index of country US
    }

}
