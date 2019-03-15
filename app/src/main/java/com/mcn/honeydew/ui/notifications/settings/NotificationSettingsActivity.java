package com.mcn.honeydew.ui.notifications.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.ui.base.BaseActivity;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by amit on 28/2/18.
 */

public class NotificationSettingsActivity extends BaseActivity implements NotificationSettingsMvpView, CompoundButton.OnCheckedChangeListener, BaseActivity.LocationSettingsCallback {

    @Inject
    NotificationSettingsPresenter<NotificationSettingsMvpView> mPresenter;

    String[] proximity_range = new String[]{
            "1/8 mile",
            "1/4 mile",
            "1/2 mile",
            "3/4 mile",
            "1 mile"
    };

    @BindView(R.id.listview)
    ListView listView;

    @BindView(R.id.proximity_notification_switch)
    Switch proximityNotificationSwitch;

    private boolean isRefresh = false;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, NotificationSettingsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_settings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Proximity Settings");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);
        setUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (isRefresh) {
            Intent data = new Intent();
            data.putExtra("isEnabled", proximityNotificationSwitch.isChecked());
            setResult(RESULT_OK, data);
        }
        super.onBackPressed();
    }

    @Override
    protected void setUp() {

        setLocationCallback(this);
        // Instantiating array adapter to populate the listView
        // The layout android.R.layout.simple_list_item_single_choice creates radio button for each listview item
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_single_choice, proximity_range);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String range = listView.getItemAtPosition(position).toString();
                mPresenter.updateProximityRange(range, position);
                isRefresh = true;
            }
        });
        mPresenter.onViewPrepared();


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            fetchCurrentLocation(true);
            listView.setAlpha(1f);
            listView.setEnabled(true);
            mPresenter.updateProximitySettings(1, 2);

            // Starting Service
            if (!GeoFenceFilterService.isRunning)
                startService(new Intent(NotificationSettingsActivity.this, GeoFenceFilterService.class));
        } else {
            //fetchCurrentLocation(true);
            proximityNotificationSwitch.setChecked(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (b) {

            int permissionLocation = ContextCompat
                    .checkSelfPermission(NotificationSettingsActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION);

            if (permissionLocation == PackageManager.PERMISSION_GRANTED) {

                fetchCurrentLocation(true);
                listView.setAlpha(1f);
                listView.setEnabled(true);
                mPresenter.updateProximitySettings(1, 2);

                // Starting Service
                if (!GeoFenceFilterService.isRunning)
                    startService(new Intent(NotificationSettingsActivity.this, GeoFenceFilterService.class));

            } else {
                fetchCurrentLocation(true);
            }


        } else {
            listView.setEnabled(false);
            listView.setAlpha(0.5f);
            mPresenter.updateProximitySettings(0, 2);

            // Stopping Service
            if (GeoFenceFilterService.isRunning)
                stopService(new Intent(NotificationSettingsActivity.this, GeoFenceFilterService.class));

        }
        isRefresh = true;

    }

    @Override
    public void setProximitySettings(NotificationSettingsResponse proximitySettings) {
        if (proximitySettings.getResults().get(0).isProximityNotification()) {
            proximityNotificationSwitch.setChecked(true);
            listView.setEnabled(true);
            int index = Arrays.asList(proximity_range).indexOf(proximitySettings.getResults().get(0).getProximityRange());
            if (index != -1) {
                listView.setItemChecked(index, true);
            }
            listView.setAlpha(1.0f);


        } else {
            proximityNotificationSwitch.setChecked(false);
            listView.setAlpha(0.5f);
            listView.setEnabled(false);


        }

        proximityNotificationSwitch.setOnCheckedChangeListener(this);
    }

    @Override
    public void updateProximityRange(String proximityRange) {
        listView.setEnabled(true);
        int index = Arrays.asList(proximity_range).indexOf(proximityRange);
        if (index != -1) {
            listView.setItemChecked(index, true);
        }
        listView.setAlpha(1.0f);
    }

    @Override
    public void onProximityRangeUpdated() {
        Intent intent = new Intent(NotificationSettingsActivity.this, GeoFenceFilterService.class);
        stopService(intent);
        startService(intent);

    }


    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    // Location settings callback

    @Override
    public void onLocationEnabled() {
        getMyLocation(true);

        //mPresenter.getProximityItems();
    }

    @Override
    public void onCancelled() {
        proximityNotificationSwitch.setChecked(false);
        showMessage(R.string.location_disabled_message);
    }

    @Override
    public void onLocationFetched(Location location) {
        if (location == null)
            return;

        /*setResult(RESULT_OK);
        finish();*/

        // mPresenter.getProximityItems();
    }

    @Override
    public void onLocationEnabledAlready() {

    }
}
