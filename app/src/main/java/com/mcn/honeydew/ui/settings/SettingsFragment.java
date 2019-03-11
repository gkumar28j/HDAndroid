package com.mcn.honeydew.ui.settings;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.facebook.login.LoginManager;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.changePassword.ChangePasswordActivity;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by amit on 20/2/18.
 */

public class SettingsFragment extends BaseFragment implements SettingsMvpView, EditNameDialog.RefreshListener {

    private static final String TAG = "SettingsFragment";

    @Inject
    SettingsMvpPresenter<SettingsMvpView> mPresenter;

    @BindView(R.id.username_textView)
    TextView mUserNameTextView;

    @BindView(R.id.email_textView)
    TextView mEmailTextView;

    @BindView(R.id.phone_textView)
    TextView mPhoneTextView;


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

        mPresenter.onViewPrepared();
    }

    @OnClick(R.id.button_sign_out)
    void onLogOutClick() {

        mPresenter.onLogoutClick();

        NotificationManager notificationManager =
                (NotificationManager) getBaseActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null)
            notificationManager.cancelAll();

        //getBaseActivity().stopService(new Intent(getBaseActivity(), GeoFenceFilterService.class));


    }

    @OnClick(R.id.imageView_edit_username)
    void onUpdateUserClick() {
        mPresenter.onUpdateUserClick();
    }

    @OnClick(R.id.imageView_edit_password)
    void onUpdatePasswordClick() {
        mPresenter.onUpdatePasswordClicked();
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
    public void refreshData() {
        mPresenter.onViewPrepared();
    }
}
