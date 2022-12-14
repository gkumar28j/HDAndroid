package com.mcn.honeydew.ui.settings;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.content.ContextCompat;

import com.facebook.login.LoginManager;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.data.network.model.response.NotificationSettingsResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.changePassword.ChangePasswordActivity;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.notifications.settings.NotificationSettingsActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialog;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;
import com.mcn.honeydew.ui.settings.timePicker.TimeLoopPickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class SettingsFragment extends BaseFragment implements SettingsMvpView, EditNameDialog.RefreshListener,
        EditEmailDialog.RefreshListener, TimeLoopPickerDialog.RefreshListener {

    public static final String TAG = "SettingsFragment";

    public static final int REQUEST_CODE_PHONE_VERIFICATION = 112;
    private static final int NOTIFICATION_SETTINGS_REQUEST_CODE = 102;

    @Inject
    SettingsMvpPresenter<SettingsMvpView> mPresenter;

    @BindView(R.id.username_textView)
    TextView mUserNameTextView;

    @BindView(R.id.email_textView)
    TextView mEmailTextView;

    @BindView(R.id.phone_textView)
    TextView mPhoneTextView;

    @BindView(R.id.password_layout)
    LinearLayout passwordLayout;


    // proximity

    @BindView(R.id.proximity_notification_text)
    TextView mProximityNotificationTextView;


    // bluetooth items

    @BindView(R.id.bluetooth_heading_text)
    TextView mBluetoothNotificationHeadingTextView;

    @BindView(R.id.switch_bluetooth)
    SwitchCompat toggleBluetooth;

    // daily reminder

    @BindView(R.id.daily_reminder_expiring_heading_text)
    TextView dailyReminderExpiringHeadingTextView;

    @BindView(R.id.switch_daily_expiring_items)
    SwitchCompat dailyExpiringSwitch;

    @BindView(R.id.daily_reminder_expiring_time_textview)
    TextView dailyReminderExpiringTimeTextView;


    @BindView(R.id.daily_reminder_expired_heading_text)
    TextView dailyReminderExpiredHeadingTextView;

    @BindView(R.id.switch_daily_expired_items)
    SwitchCompat dailyExpiredSwitch;

    @BindView(R.id.daily_reminder_expired_time_textview)
    TextView dailyReminderExpiredTimeTextView;

    @BindView(R.id.imageView_edit_email)
    ImageView editEmailImageView;

    @BindView(R.id.user_imageView)
    ImageView userImageView;

    @BindView(R.id.email_imageView)
    ImageView emailImageView;

    @BindView(R.id.phone_imageView)
    ImageView phoneImageView;

    @BindView(R.id.password_imageView)
    ImageView passwordImageView;

    @BindView(R.id.textView2)
    TextView locationReminderTextView;


    public static SettingsFragment newInstance() {
        Bundle args = new Bundle();
        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        getBaseActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

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
        userImageView.setColorFilter(ContextCompat.getColor(getBaseActivity(), R.color.gray));
        emailImageView.setColorFilter(ContextCompat.getColor(getBaseActivity(), R.color.gray));
        phoneImageView.setColorFilter(ContextCompat.getColor(getBaseActivity(), R.color.gray));
        passwordImageView.setColorFilter(ContextCompat.getColor(getBaseActivity(), R.color.gray));

        mPresenter.toggleBluetoothSwitch();
        mPresenter.onViewPrepared();

        String fullText = getString(R.string.bluetooth_notifications_heading_text);
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(), R.color.colorMediumLightGray)), 20, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText = new RelativeSizeSpan(.9f);

        builder.setSpan(smallSizeText,
                20,
                fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new StyleSpan(Typeface.BOLD), 0, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBluetoothNotificationHeadingTextView.setText(builder);

        String expiringText = getString(R.string.daily_expiring_heading_text);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(expiringText);
        builder1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(), R.color.colorMediumLightGray)), 16, expiringText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText1 = new RelativeSizeSpan(.9f);

        builder1.setSpan(smallSizeText1,
                16,
                expiringText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder1.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dailyReminderExpiringHeadingTextView.setText(builder1);


        String expiredText = getString(R.string.daily_expired_heading_text);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(expiredText);
        builder2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(), R.color.colorMediumLightGray)), 16, expiredText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText2 = new RelativeSizeSpan(.9f);

        builder2.setSpan(smallSizeText2,
                16,
                expiredText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder2.setSpan(new StyleSpan(Typeface.BOLD), 0, 16, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dailyReminderExpiredHeadingTextView.setText(builder2);

        String locationText = getResources().getString(R.string.location_reminders);
        SpannableStringBuilder builder3 = new SpannableStringBuilder(locationText);
        builder3.setSpan(new StyleSpan(Typeface.BOLD), 0, locationText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        locationReminderTextView.setText(builder3);


    }

    @OnClick(R.id.button_sign_out)
    void onLogOutClick() {

        mPresenter.onLogoutClick();

        NotificationManager notificationManager =
                (NotificationManager) getBaseActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancelAll();

        getBaseActivity().stopService(new Intent(getBaseActivity(), GeoFenceFilterService.class));


    }

    @OnClick(R.id.imageView_edit_username)
    void onUpdateUserClick() {
        mPresenter.onUpdateUserClick();
    }

    @OnClick(R.id.imageView_edit_password)
    void onUpdatePasswordClick() {
        mPresenter.onUpdatePasswordClicked();
    }

    @OnClick(R.id.imageView_edit_phone)
    void onUpdatePhoneNumber() {

        if (getBaseActivity() == null) {
            return;
        }

        Intent intent = new Intent(getBaseActivity(), PhoneVerificationActivity.class);
        intent.putExtra("isFromSettings", true);
        startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION);

    }

    @OnClick(R.id.imageView_edit_email)
    void onUpdateEmail() {

        mPresenter.onUpdateEmail();
    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void setUserData(UserDetailResponse userData, boolean facebookLogin) {

        if (userData == null) return;

        if (facebookLogin) {
            editEmailImageView.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
        } else {
            editEmailImageView.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.VISIBLE);
        }

        mUserNameTextView.setText(userData.getUserName());
        mEmailTextView.setText(userData.getPrimaryEmail());
        mPhoneTextView.setText(userData.getPrimaryMobile());


    }

    @Override
    public void openLoginActivity() {

        if (getActivity() == null) {
            return;
        }


        ((MainActivity) getActivity()).requestToClearProximityItems();
        startActivity(LoginActivity.getStartIntent(getActivity()));
        getActivity().finish();
    }

    @Override
    public void showEditNameDialog(boolean facebookLogin) {

        EditNameDialog dialog = EditNameDialog.newInstance();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());

    }

    @Override
    public void showChangePasswordActivity() {
        startActivity(ChangePasswordActivity.getStartIntent(getActivity()));
    }

    @Override
    public void onLogoutSuccess() {
        if (LoginManager.getInstance() != null) {
            LoginManager.getInstance().logOut();
        }
        openLoginActivity();
    }

    @Override
    public void onLogoutFailed() {
        showMessage(R.string.some_error);
    }

    @Override
    public void showEditEmailDialog() {

        EditEmailDialog dialog = EditEmailDialog.newInstance(true);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());
    }


    @Override
    public void refreshData() {
        mPresenter.onViewPrepared();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION) {

            if (resultCode == Activity.RESULT_OK) {

                mPresenter.onViewPrepared();
            }
        } else if (requestCode == NOTIFICATION_SETTINGS_REQUEST_CODE) {
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

    // bluetooth and proximity

    @Override
    public void openProximitySettingView() {

      /*  if (!isNetworkConnected()) {
            return;
        }*/

        if (getActivity() == null) {
            return;
        }
        startActivityForResult(NotificationSettingsActivity.getStartIntent(getActivity()), NOTIFICATION_SETTINGS_REQUEST_CODE);
    }

    @Override
    public void setProximityNotification(NotificationSettingsResponse results) {

        if (results == null) {
            return;
        }

        NotificationSettingsResponse.NotificationSettings notificationSettings = results.getResults().get(0);

        if (notificationSettings.isProximityNotification()) {
            mProximityNotificationTextView.setText(notificationSettings.getProximityRange());
        } else {
            mProximityNotificationTextView.setText("off");
        }

        dailyExpiringSwitch.setChecked(notificationSettings.isBluetoothReminderForExpiring());
        dailyExpiredSwitch.setChecked(notificationSettings.isBluetoothReminderForExpired());

        dailyReminderExpiringTimeTextView.setEnabled(notificationSettings.isBluetoothReminderForExpiring());
        dailyReminderExpiredTimeTextView.setEnabled(notificationSettings.isBluetoothReminderForExpired());

        if (notificationSettings.getTimeOnExpiring() != null) {
            String expiringTime = convertTimeInLocal(notificationSettings.getTimeOnExpiring());
            dailyReminderExpiringTimeTextView.setText(expiringTime);
        } else {
            dailyReminderExpiringTimeTextView.setText("08:00 AM");
        }

        if (notificationSettings.getTimeOnExpired() != null) {

            String expiredTime = convertTimeInLocal(notificationSettings.getTimeOnExpired());
            dailyReminderExpiredTimeTextView.setText(expiredTime);

        } else {
            dailyReminderExpiredTimeTextView.setText("08:00 AM");
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

    @OnClick(R.id.proximity_layout)
    void onProximitySettingClick() {
        mPresenter.onProximitySettingClick();

    }

    @OnClick(R.id.switch_daily_expiring_items)
    public void toggleDailyExpiringItems() {

        if (dailyExpiringSwitch.isChecked()) {
            mPresenter.saveDailyExpiringItems(true);
            dailyReminderExpiringTimeTextView.setEnabled(true);

        } else {
            mPresenter.saveDailyExpiringItems(false);
            dailyReminderExpiringTimeTextView.setEnabled(false);
        }

    }


    @OnClick(R.id.switch_daily_expired_items)
    public void toggleDailyExpiredItems() {

        if (dailyExpiredSwitch.isChecked()) {

            mPresenter.saveDailyExpiredItems(true);
            dailyReminderExpiredTimeTextView.setEnabled(true);

        } else {
            mPresenter.saveDailyExpiredItems(false);
            dailyReminderExpiredTimeTextView.setEnabled(false);
        }

    }


    @OnClick(R.id.daily_reminder_expiring_time_textview)
    public void changeDailyReminderExpiringTime() {

        TimeLoopPickerDialog dialog = TimeLoopPickerDialog.newInstance(dailyReminderExpiringTimeTextView.getText().toString().trim(), 0);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());

    }


    @OnClick(R.id.daily_reminder_expired_time_textview)
    public void changeDailyReminderExpiredTime() {

        TimeLoopPickerDialog dialog = TimeLoopPickerDialog.newInstance(dailyReminderExpiredTimeTextView.getText().toString().trim(), 1);
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());

    }


    private String convertTimeInLocal(String time) {

        //2019-08-27 11:10:15 +0000

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z", Locale.ENGLISH);

        SimpleDateFormat toShow = new SimpleDateFormat("hh:mm aa", Locale.ENGLISH);

        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date date = null;
        try {
            date = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        toShow.setTimeZone(TimeZone.getDefault());


        String formattedDate = toShow.format(date);

        return formattedDate;

    }


    @Override
    public void onEmailEditedSuccessfully(String email) {

    }
}
