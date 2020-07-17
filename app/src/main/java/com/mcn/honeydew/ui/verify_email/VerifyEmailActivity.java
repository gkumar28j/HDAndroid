package com.mcn.honeydew.ui.verify_email;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.BinderThread;

import com.mcn.honeydew.R;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.utils.CommonUtils;


import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyEmailActivity extends BaseActivity implements VerifyEmailMvpView {

    @Inject
    VerifyEmailMvpPresenter<VerifyEmailMvpView> mPresenter;

    @BindView(R.id.text_sub_heading)
    TextView subHeadingTextView;

    @BindView(R.id.edit_email)
    EditText emailEditText;

    @BindView(R.id.edit_code_email)
    EditText  codeEditText;

    @BindView(R.id.submit_button)
    Button submitButton;

    private boolean isEmailVerified = false;

    String email = null;

    @BindView(R.id.edit_email_lay)
    LinearLayout linearLayoutEmail;

    @BindView(R.id.enter_code_lay)
    LinearLayout otpLayout;

    @BindView(R.id.additional_sub_heading)
    TextView additionalOTPTextView;


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

        mPresenter.onAttach(this);

    }


    @Override
    protected void setUp() {

    }

    @Override
    protected void onDestroy() {

        mPresenter.onDetach();
        super.onDestroy();

    }


    @OnClick(R.id.submit_button)
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


    }

    @Override
    public void emailVerifiedSuccessfully(boolean value) {

        if(value){

            linearLayoutEmail.setVisibility(View.GONE);
            otpLayout.setVisibility(View.VISIBLE);
            additionalOTPTextView.setVisibility(View.VISIBLE);

        }



    }

    @Override
    public void otpVerifiedSuccess() {

        Intent intent = new Intent();
        intent.putExtra("VerifiedEmail",email);
        setResult(RESULT_OK);
        finish();


    }
}
