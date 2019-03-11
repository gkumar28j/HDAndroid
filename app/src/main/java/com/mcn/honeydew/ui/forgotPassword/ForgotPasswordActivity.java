package com.mcn.honeydew.ui.forgotPassword;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountFragment;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodFragment;
import com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment.ResetPasswordFragment;
import com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment.VerifyOtpFragment;
import com.mcn.honeydew.utils.AppConstants;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ForgotPasswordActivity extends BaseActivity implements
        ForgotPasswordMvpView,
        LocateAccountFragment.OnLocateAccountListener,
        ResetMethodFragment.OnResetMethodListener,
        VerifyOtpFragment.VerifyOtpListener,
        ResetPasswordFragment.OnResetPasswordListener {

    @Inject
    ForgotPasswordMvpPresenter<ForgotPasswordMvpView> mPresenter;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, ForgotPasswordActivity.class);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

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
    protected void setUp() {
        mPresenter.openLocateAccountFragment();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    // ------------------------------ Clicks ----------------------------------------------


    // ------------------------------ PRIVATE METHOD ----------------------------------------------

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_account, fragment, tag);

        if (fragment instanceof LocateAccountFragment)
            fragmentTransaction.commit();
        else
            fragmentTransaction.addToBackStack(null).commit();
    }


    @Override
    public void showLocateAccountFragment() {
        showFragment(LocateAccountFragment.newInstance(), LocateAccountFragment.TAG);
    }

    @Override
    public void showResetMethodFragment(LocateAccountResponse.Detail detail) {
        ResetMethodFragment.FragmentData data = new ResetMethodFragment.FragmentData(detail.getUserName(), detail.getEmail(), detail.getMobileNumber(), detail.isIsEmail());

        showFragment(ResetMethodFragment.newInstance(data), ResetMethodFragment.TAG);
    }

    @Override
    public void showVerifyCodeFragment(String verificationCode, String emailOrPhone, int isEmail, String lastTwoDigit, String hiddenEmail) {

        showFragment(VerifyOtpFragment.newInstance(emailOrPhone, isEmail,lastTwoDigit,hiddenEmail), VerifyOtpFragment.TAG);
    }

    @Override
    public void showResetPasswordFragment(String mAuthentication) {
        showFragment(ResetPasswordFragment.newInstance(mAuthentication), ResetPasswordFragment.TAG);
    }

    @Override
    public void onAccountLocated(LocateAccountResponse.Detail detail) {
        mPresenter.openResetMethodFragment(detail);
    }

    @Override
    public void onOtpSent(String verificationCode, String emailOrPhone, int isEmail, String lastTwoDigit, String hiddenEmail) {
        mPresenter.openVerifyCodeFragment(verificationCode, emailOrPhone, isEmail, lastTwoDigit, hiddenEmail);
    }

    @Override
    public void onVerifySuccess(String mAuthentication) {
        // Open reset password
        Toast.makeText(this, "Verified", Toast.LENGTH_SHORT).show();
        mPresenter.openResetPasswordFragment(mAuthentication);
    }

    @Override
    public void onResendCodeClicked() {
        onBackPressed();
    }

    @Override
    public void onResetPasswordSuccess(String emailOrPhone, String password) {
        if (emailOrPhone.contains("-")) {
            emailOrPhone = emailOrPhone.substring(emailOrPhone.indexOf("-") + 1, emailOrPhone.length());
        }
        Intent intent = new Intent();
        intent.putExtra(AppConstants.KEY_EMAIL_OR_PHONE, emailOrPhone);
        intent.putExtra(AppConstants.KEY_PASSWORD, password);
        setResult(RESULT_OK, intent);
        finish();
    }
}
