package com.mcn.honeydew.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsActivity;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class NotificationsFragment extends BaseFragment implements NotificationsMvpView {

    private static final String TAG = "NotificationsFragment";
    private static final int NOTIFICATION_SETTINGS_REQUEST_CODE = 102;

    @Inject
    NotificationsMvpPresenter<NotificationsMvpView> mPresenter;

    @BindView(R.id.proximity_notification_text)
    TextView mProximityNotificationTextView;

    @BindView(R.id.bluetooth_heading_text)
    TextView mBluetoothNotificationHeadingTextView;

    @BindView(R.id.switch_bluetooth)
    SwitchCompat toggleBluetooth;

    public static NotificationsFragment newInstance() {
        Bundle args = new Bundle();
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

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
        mPresenter.toggleBluetoothSwitch();
        mPresenter.onViewPrepared();

        // Creating spannable string for Slogan text
        String fullText = getString(R.string.bluetooth_notifications_heading_text);
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(), R.color.colorMediumLightGray)), 24, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText = new RelativeSizeSpan(.9f);

        builder.setSpan( smallSizeText,
                24,
                fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBluetoothNotificationHeadingTextView.setText(builder);
    }

    @OnClick(R.id.proximity_layout)
    void onProximitySettingClick() {
        mPresenter.onProximitySettingClick();

    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NOTIFICATION_SETTINGS_REQUEST_CODE) {
            mPresenter.refreshSettings();

            if (data != null) {
                boolean isEnabled = data.getBooleanExtra("isEnabled", false);

                assert ((MainActivity) getActivity()) != null;
                if (isEnabled) {
                    // Get All proximity Items
                    synchronized (this) {
                        if (mPresenter.isItemAdded()) {
                            ((MainActivity) getActivity()).requestToClearProximityItems();
                        }
                        ((MainActivity) getActivity()).requestProximityItems();
                    }


                } else {
                    // Request to clear all geo fence items
                    synchronized (this) {
                        if (mPresenter.isItemAdded()) {
                            ((MainActivity) getActivity()).requestToClearProximityItems();
                        }

                    }

                }
            }

            // Check proximity settings and then add/remove items in geo fence
        }
    }

    @Override
    public void openProximitySettingView() {
        if (getActivity() == null) {
            return;
        }
        startActivityForResult(NotificationSettingsActivity.getStartIntent(getActivity()), NOTIFICATION_SETTINGS_REQUEST_CODE);
    }

    @Override
    public void setProximityNotification(NotificationSettingsResponse results) {
        if (results.getResults().get(0).isProximityNotification()) {
            mProximityNotificationTextView.setText(results.getResults().get(0).getProximityRange());
        } else {
            mProximityNotificationTextView.setText("off");
        }
    }

    @Override
    public void toggleBlueToothButton(boolean val) {

        toggleBluetooth.setChecked(val);
    }


    @OnClick(R.id.switch_bluetooth)
    public void toggleBluetoothSettings() {
        if (toggleBluetooth.isChecked()) {
            mPresenter.saveBluetoothData(true);
        } else {
            mPresenter.saveBluetoothData(false);
        }

    }
}
