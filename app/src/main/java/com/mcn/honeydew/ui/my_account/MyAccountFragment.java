package com.mcn.honeydew.ui.my_account;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.facebook.login.LoginManager;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialog;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAccountFragment extends BaseFragment implements MyAccountMvpView, EditNameDialog.RefreshListener,
        EditEmailDialog.RefreshListener {
    public static final int REQUEST_CODE_PHONE_VERIFICATION = 112;
    @Inject
    MyAccountMvpPresenter<MyAccountMvpView> mPresenter;

    @BindView(R.id.name_edit_text)
    TextView nameTextView;

    @BindView(R.id.fb_email_edit_text)
    TextView fbEmailTextView;

    @BindView(R.id.normal_email_edit_text)
    TextView normalEmailTextView;

    @BindView(R.id.normal_email_layout)
    RelativeLayout normalEmailLayout;

    @BindView(R.id.facebook_email_layout)
    RelativeLayout facebookEmailLayout;

    @BindView(R.id.contact_edit_text)
    TextView contactTextView;


    public static MyAccountFragment newInstance() {
        Bundle args = new Bundle();
        MyAccountFragment fragment = new MyAccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);

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


    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }


    @OnClick(R.id.account_forward_imageview)
    public void onEditNameClicked() {

        mPresenter.onUpdateUserNameClicked();

    }

    @OnClick(R.id.facebook_email_edit_imageview)
    public void facebookEmailEditImageView() {

    }

    @OnClick(R.id.normal_email_edit_imageview)
    public void normalEmailEditImageView() {
        mPresenter.onUpdateEmail();
    }

    @OnClick(R.id.contact_edit_imageview)
    public void onEditContactImageView() {

        if (getBaseActivity() == null) {
            return;
        }

        Intent intent = new Intent(getBaseActivity(), PhoneVerificationActivity.class);
        intent.putExtra("isFromSettings", true);
        startActivityForResult(intent, REQUEST_CODE_PHONE_VERIFICATION);

    }


    @Override
    public void onLoadDataSuccess(UserDetailResponse userData, boolean facebookLogin) {

        if (userData == null) return;

        if (facebookLogin) {
            facebookEmailLayout.setVisibility(View.VISIBLE);
            normalEmailLayout.setVisibility(View.GONE);
            fbEmailTextView.setText(userData.getPrimaryEmail());
        } else {
            facebookEmailLayout.setVisibility(View.GONE);
            normalEmailLayout.setVisibility(View.VISIBLE);
            normalEmailTextView.setText(userData.getPrimaryEmail());
        }

        nameTextView.setText(userData.getUserName());

        contactTextView.setText(userData.getPrimaryMobile());

    }

    @Override
    public void showEditNameDialog(boolean facebookLogin) {
        EditNameDialog dialog = EditNameDialog.newInstance();
        dialog.setListener(this);
        dialog.show(getChildFragmentManager());
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION) {

            if (resultCode == Activity.RESULT_OK) {

                mPresenter.onViewPrepared();
            }
        }
    }


    @Override
    public void refreshData() {
        mPresenter.onViewPrepared();
    }


    public void openLoginActivity() {

        if (getActivity() == null) {
            return;
        }


        ((MainActivity) getActivity()).requestToClearProximityItems();
        startActivity(LoginActivity.getStartIntent(getActivity()));
        getActivity().finish();
    }


    @OnClick(R.id.button_sign_out)
    public void onSignOutClicked(){
        mPresenter.onLogoutClick();
    }
}
