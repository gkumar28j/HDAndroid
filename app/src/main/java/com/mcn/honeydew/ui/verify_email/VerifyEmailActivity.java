package com.mcn.honeydew.ui.verify_email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.BinderThread;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.response.LocateAccountResponse;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.forgotPassword.locateAccountFragment.LocateAccountFragment;
import com.mcn.honeydew.ui.forgotPassword.resetMethodFragment.ResetMethodFragment;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.verify_email.search_email.SearchEmailFragment;
import com.mcn.honeydew.ui.verify_email.verifyEmailOtpFragment.VerifyEmailOtpFragment;
import com.mcn.honeydew.utils.CommonUtils;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyEmailActivity extends BaseActivity implements VerifyEmailMvpView, SearchEmailFragment.OnLocateAccountListener, VerifyEmailOtpFragment.VerifyOtpListener {

    @Inject
    VerifyEmailMvpPresenter<VerifyEmailMvpView> mPresenter;

  /*  @BindView(R.id.text_sub_heading)
    TextView subHeadingTextView;

    @BindView(R.id.edit_email)
    EditText emailEditText;*/

//    @BindView(R.id.edit_code_email)
//    EditText  codeEditText;
//
//    @BindView(R.id.submit_button)
//    Button submitButton;

    private boolean isEmailVerified = false;

    String email = null;

  /*  @BindView(R.id.skip_button)
    TextView skipButton;*/

   /* @BindView(R.id.edit_email_lay)
    LinearLayout linearLayoutEmail;

    @BindView(R.id.enter_code_lay)
    LinearLayout otpLayout;

    @BindView(R.id.additional_sub_heading)
    TextView additionalOTPTextView;*/

    boolean isFromLogin = false;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, VerifyEmailActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_email);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.skip_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_skip);
        if(isFromLogin){
            item.setVisible(true);
        }else {
            item.setVisible(false);
        }

        return true;
    }

    private void showMainActivity() {

        Intent intent = MainActivity.getStartIntent(this);
        startActivity(intent);
        finishAffinity();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_skip:
                showMainActivity();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void setUp() {

        if (getIntent() != null && getIntent().hasExtra("from_account")) {

            String email = getIntent().getStringExtra("email_final");
            Fragment fragment = VerifyEmailOtpFragment.newInstance(email);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_account, fragment, VerifyEmailOtpFragment.TAG);
            fragmentTransaction.commit();

        } else if (getIntent() != null && getIntent().hasExtra("from_login")) {

            isFromLogin = true;
          //  skipButton.setVisibility(View.VISIBLE);
            String email = getIntent().getStringExtra("email_final");
            Fragment fragment = VerifyEmailOtpFragment.newInstance(email);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_account, fragment, VerifyEmailOtpFragment.TAG);
            fragmentTransaction.commit();

        } else {
            mPresenter.openSearchEmailFragment();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {

        mPresenter.onDetach();
        super.onDestroy();

    }

    private void showFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_account, fragment, tag);

        if (fragment instanceof SearchEmailFragment)
            fragmentTransaction.commit();
        else
            fragmentTransaction.addToBackStack(null).commit();
    }


 /*   @OnClick(R.id.submit_button)
    public void submitButtonClicked(){

        if(!isEmailVerified){

            if(emailEditText.getText().toString().trim().isEmpty()){
                showMessage("Email Can't be empty.");
                return;
            }


            if(!CommonUtils.isEmailValid(emailEditText.getText().toString().trim())){
                showMessage("Please enter a valid email.");
                return;
            }

            email = emailEditText.getText().toString().trim();


            mPresenter.verifyEmailToCode(email);


        }else {

            if(codeEditText.getText().toString().trim().isEmpty()){
                showMessage("OTP Can't be empty.");
                return;
            }


            if(emailEditText.getText().toString().trim().length()<6){
                showMessage("Please enter a valid OTP.");
                return;
            }


            mPresenter.verifyEmailToCode(emailEditText.getText().toString().trim());

        }


    }*/

   /* @Override
    public void emailVerifiedSuccessfully(boolean value) {

        if(value){

            linearLayoutEmail.setVisibility(View.GONE);
            otpLayout.setVisibility(View.VISIBLE);
            additionalOTPTextView.setVisibility(View.VISIBLE);

        }

    }
*/
   /* @Override
    public void otpVerifiedSuccess() {
        Intent intent = new Intent();
        intent.putExtra("VerifiedEmail",email);
        setResult(RESULT_OK);
        finish();
    }*/

    @Override
    public void emailVerifiedSuccessfully(boolean value) {

    }

    @Override
    public void otpVerifiedSuccess() {

    }

    @Override
    public void openSearchEmailFragments() {
        showFragment(SearchEmailFragment.newInstance(), SearchEmailFragment.TAG);
    }


    @Override
    public void onAccountLocated(String detail) {

        showFragment(VerifyEmailOtpFragment.newInstance(detail), VerifyEmailOtpFragment.TAG);
    }

    @Override
    public void onVerifySuccess(String mAuthentication) {

        if(isFromLogin){
            showMainActivity();
        }else {
            Intent intent = new Intent();
            intent.putExtra("VerifiedEmail", mAuthentication);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    @Override
    public void onResendCodeClicked() {

        onBackPressed();

    }

}
