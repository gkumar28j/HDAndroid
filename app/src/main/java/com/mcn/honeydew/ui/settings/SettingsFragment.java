package com.mcn.honeydew.ui.settings;

import android.app.NotificationManager;
import android.content.Context;
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
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.services.GeoFenceFilterService;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.changePassword.ChangePasswordActivity;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialog;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class SettingsFragment extends BaseFragment implements SettingsMvpView, EditNameDialog.RefreshListener, EditEmailDialog.RefreshListener {

    private static final String TAG = "SettingsFragment";

    @Inject
    SettingsMvpPresenter<SettingsMvpView> mPresenter;

    @BindView(R.id.username_textView)
    TextView mUserNameTextView;

    @BindView(R.id.email_textView)
    TextView mEmailTextView;

    @BindView(R.id.phone_textView)
    TextView mPhoneTextView;


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

        String fullText = getString(R.string.bluetooth_notifications_heading_text);
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(), R.color.colorMediumLightGray)), 24, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText = new RelativeSizeSpan(.9f);

        builder.setSpan( smallSizeText,
                24,
                fullText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mBluetoothNotificationHeadingTextView.setText(builder);

        String expiringText =  getString(R.string.daily_expiring_heading_text);
        SpannableStringBuilder builder1 = new SpannableStringBuilder(expiringText);
        builder1.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(),R.color.colorMediumLightGray)),16,expiringText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText1 = new RelativeSizeSpan(.9f);

        builder1.setSpan( smallSizeText1,
                16,
                expiringText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dailyReminderExpiringHeadingTextView.setText(builder1);


        String expiredText =  getString(R.string.daily_expired_heading_text);
        SpannableStringBuilder builder2 = new SpannableStringBuilder(expiringText);
        builder2.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getBaseActivity(),R.color.colorMediumLightGray)),16,expiringText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        RelativeSizeSpan smallSizeText2 = new RelativeSizeSpan(.9f);

        builder2.setSpan( smallSizeText2,
                16,
                expiredText.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        dailyReminderExpiredHeadingTextView.setText(builder2);


        mPresenter.onViewPrepared();
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
    void onUpdatePhoneNumber(){

    }

    @OnClick(R.id.imageView_edit_email)
    void onUpdateEmail(){

        mPresenter.onUpdateEmail();
    }


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    @Override
    public void setUserData(UserDetailResponse userData) {

        if (userData == null) return;

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
    public void showEditNameDialog() {
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

        EditEmailDialog dialog = EditEmailDialog.newInstance();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());
    }



    @Override
    public void refreshData() {
        mPresenter.onViewPrepared();
    }
}
