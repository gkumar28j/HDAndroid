package com.mcn.honeydew.ui.my_account;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.mcn.honeydew.BuildConfig;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.settings.editEmail.EditEmailDialog;
import com.mcn.honeydew.ui.settings.editname.EditNameDialog;
import com.mcn.honeydew.ui.verify_email.VerifyEmailActivity;

import org.json.JSONObject;

import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

public class MyAccountFragment extends BaseFragment implements MyAccountMvpView, EditNameDialog.RefreshListener,
        EditEmailDialog.RefreshListener, FacebookCallback<LoginResult> {
    public static final int REQUEST_CODE_PHONE_VERIFICATION = 112;
    public static final int REQUEST_CODE_EMAIL_VERIFICATION = 113;
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

    @BindView(R.id.verified_text_email)
    TextView verifiedEmailTextView;

    private CallbackManager mCallbackManager;

    UserDetailResponse finalUserData;

    String facebookEmail;

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

        mCallbackManager = CallbackManager.Factory.create();
        com.facebook.login.LoginManager.getInstance().registerCallback(mCallbackManager, this);
        com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(/*public_profile,publish_actions*/"email"));

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

        finalUserData = userData;

        if (facebookLogin) {
            facebookEmailLayout.setVisibility(View.VISIBLE);
            normalEmailLayout.setVisibility(View.GONE);
            fbEmailTextView.setText(userData.getPrimaryEmail());
        } else {
            facebookEmailLayout.setVisibility(View.GONE);
            normalEmailLayout.setVisibility(View.VISIBLE);
            normalEmailTextView.setText(userData.getPrimaryEmail());
            if (userData.isEmailVerified()) {
                verifiedEmailTextView.setText("Verified");
                verifiedEmailTextView.setTextColor(Color.parseColor("#00FF00"));
            } else {
                verifiedEmailTextView.setText("Not verified");
                verifiedEmailTextView.setTextColor(Color.parseColor("#FF0000"));
            }


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

        warningAlert();

    }

    @Override
    public void onEmailUpdatedSuccess() {
        mPresenter.onEmailChanged(facebookEmail, true); // from facebook always true
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PHONE_VERIFICATION) {

            if (resultCode == Activity.RESULT_OK) {

                mPresenter.onViewPrepared();
            }
        } else if (requestCode == REQUEST_CODE_EMAIL_VERIFICATION) {

            if (resultCode == Activity.RESULT_OK) {

                //  mPresenter.onViewPrepared();
                if (data != null && data.hasExtra("VerifiedEmail")) {

                    String email = data.getStringExtra("VerifiedEmail");
                    mPresenter.onEmailChanged(email, true);
                }


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
    public void onSignOutClicked() {
        mPresenter.onLogoutClick();
    }

    @Override
    public void onSuccess(LoginResult loginResult) {

        GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                if (response != null) {
                    String jsonresult = String.valueOf(object);
                    Log.e("JSON Result", jsonresult);
                    String fbUserEmail = object.optString("email");
                    facebookEmail = fbUserEmail;
                    mPresenter.onEmailSubmit(fbUserEmail);

                }

            }
        });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();


    }

    @Override
    public void onCancel() {
        Timber.d("Facebook Login cancelled");
    }

    @Override
    public void onError(FacebookException error) {
        Crashlytics.logException(error);
        Timber.d(error);
    }

    @Override
    public void onEmailEditedSuccessfully(String email) {
        if (getBaseActivity() == null) {
            return;
        }
        mPresenter.onEmailChanged(email, false);

        Intent intent = VerifyEmailActivity.getStartIntent(getBaseActivity());
        intent.putExtra("from_account", "1");
        intent.putExtra("email_final", email);
        startActivityForResult(intent, REQUEST_CODE_EMAIL_VERIFICATION);

    }


    void warningAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getBaseActivity());

        builder.setTitle("HoneyDew")
                .setCancelable(false)
                .setMessage("Warning: you are about to change your sign-in email address; would you like to continue?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        openEmailDialog();
                        dialog.dismiss();

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    void openEmailDialog() {

        EditEmailDialog dialogEmail = EditEmailDialog.newInstance(finalUserData.isEmailVerified());
        dialogEmail.setListener(this);
        dialogEmail.show(getChildFragmentManager());
    }
}
