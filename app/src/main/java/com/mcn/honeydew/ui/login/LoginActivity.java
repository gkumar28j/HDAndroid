package com.mcn.honeydew.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.ApiCall;
import com.mcn.honeydew.data.network.model.request.FacebookLoginRequest;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.forgotPassword.ForgotPasswordActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.register.RegisterActivity;
import com.mcn.honeydew.ui.welcome.WelcomeTourActivity;
import com.mcn.honeydew.utils.CommonUtils;
import com.mcn.honeydew.utils.KeyboardUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static com.mcn.honeydew.utils.AppConstants.KEY_COUNTRY_CODE;
import static com.mcn.honeydew.utils.AppConstants.KEY_EMAIL_OR_PHONE;
import static com.mcn.honeydew.utils.AppConstants.KEY_PASSWORD;
import static com.mcn.honeydew.utils.AppConstants.KEY_PHONE;
import static com.mcn.honeydew.utils.AppConstants.REQUEST_CODE_FORGOT_PASSWORD;
import static com.mcn.honeydew.utils.AppConstants.REQUEST_CODE_SIGN_UP;

/**
 * Created by amit on 16/2/18.
 */

public class LoginActivity extends BaseActivity implements LoginMvpView, FacebookCallback<LoginResult> {

    private CallbackManager mCallbackManager;
    private String mFacebookToken;
    private String mCountryCode;
    private String mEmailOrPhone;

    @Inject
    LoginMvpPresenter<LoginMvpView> mPresenter;

    @BindView(R.id.edit_email)
    EditText mEmailEditText;

    @BindView(R.id.edit_password)
    EditText mPasswordEditText;

    @BindView(R.id.text_sign_up)
    TextView mSignUpTextView;


    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(LoginActivity.this);

        // Creating spannable string for Sign Up text
        String fullText = getString(R.string.sign_up);
        SpannableStringBuilder builder = new SpannableStringBuilder(fullText);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.white)), 23, fullText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorLoginSignupText)), 0, 22, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        builder.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivityForResult(RegisterActivity.getRegisterIntent(LoginActivity.this), REQUEST_CODE_SIGN_UP);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(getResources().getColor(R.color.white));
            }
        }, 23, fullText.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);

        mSignUpTextView.setText(builder);
        mSignUpTextView.setMovementMethod(new LinkMovementMethod());

        // On Done pressed
        mPasswordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    mPresenter.onServerLoginClick(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
                }
                return false;
            }
        });
     //   printHashKey(LoginActivity.this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mCallbackManager != null)
            mCallbackManager.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            String password;

            switch (requestCode) {
                case REQUEST_CODE_SIGN_UP:
                    // Getting phone and password form data and showing
                    mEmailOrPhone = data.getStringExtra(KEY_PHONE);
                    password = data.getStringExtra(KEY_PASSWORD);
                    mCountryCode = data.getStringExtra(KEY_COUNTRY_CODE);

                    mEmailEditText.setText(mEmailOrPhone);
                    mPasswordEditText.setText(password);

                    onServerLoginClick();

                    break;
                case REQUEST_CODE_FORGOT_PASSWORD:
                    // Getting phone and password form data and showing
                    mEmailOrPhone = data.getStringExtra(KEY_EMAIL_OR_PHONE);
                    password = data.getStringExtra(KEY_PASSWORD);
                    mCountryCode = data.getStringExtra(KEY_COUNTRY_CODE);

                    mEmailEditText.setText(mEmailOrPhone);
                    mPasswordEditText.setText(password);

                    onServerLoginClick();

                    break;
            }
        }
    }

    @OnClick(R.id.btn_server_login)
    void onServerLoginClick() {
        KeyboardUtils.hideSoftInput(this);
        mPresenter.onServerLoginClick(mEmailEditText.getText().toString(), mPasswordEditText.getText().toString());
    }

    @OnClick(R.id.text_facebook_login)
    void onFacebookLoginClicked() {

        setUp();

    }


    @OnClick(R.id.text_forgot_password)
    void onForgotPasswordClicked() {

        KeyboardUtils.hideSoftInput(this);
        mPresenter.onForgotPasswordClicked();
    }


    @Override
    public void openMainActivity() {
        Intent intent = MainActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openPhoneVerificationActivity(String primaryMobile) {
        Intent intent = PhoneVerificationActivity.getStartIntent(LoginActivity.this);
        if (primaryMobile != null && primaryMobile.contains("-")) {
            String mobileArray[] = primaryMobile.split("-");
            mCountryCode = mobileArray[0];
            mEmailOrPhone = mobileArray[1];
            intent.putExtra(KEY_COUNTRY_CODE, mCountryCode);
            intent.putExtra(KEY_PHONE, mEmailOrPhone);

        }
        startActivity(intent);
        finish();


    }

    @Override
    public void openForgotPasswordActivity() {
        startActivityForResult(ForgotPasswordActivity.getStartIntent(this), REQUEST_CODE_FORGOT_PASSWORD);
    }

    @Override
    public void openTourActivity() {
        Intent intent = WelcomeTourActivity.getStartIntent(LoginActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDetach();
        super.onDestroy();
    }

    @Override
    protected void setUp() {

        // Facebook SDK setup
        mCallbackManager = CallbackManager.Factory.create();
        com.facebook.login.LoginManager.getInstance().registerCallback(mCallbackManager, this);
        com.facebook.login.LoginManager.getInstance().logInWithReadPermissions(this, Arrays.asList(/*public_profile,publish_actions*/"email"));
    }


    // Facebook
    @Override
    public void onSuccess(LoginResult loginResult) {

        mFacebookToken = loginResult.getAccessToken().getToken();

        Timber.d("Facebook Token: " + mFacebookToken);

        if (com.facebook.login.LoginManager.getInstance() != null) {
            com.facebook.login.LoginManager.getInstance().logOut();

        }

        if (TextUtils.isEmpty(mFacebookToken)) {
            return;
        }

        FacebookLoginRequest request = new FacebookLoginRequest();
        request.setAppVersion(CommonUtils.getAppVersion(this));
        request.setApiVersion(ApiCall.API_VERSION);
        request.setCountry(getResources().getConfiguration().locale.getCountry());
        request.setDeveiceId(CommonUtils.getDeviceId(this));
        request.setDeviceType("Android");
        request.setDeviceVersion(Build.VERSION.SDK_INT);
        request.setFacebookAccessToken(mFacebookToken);
        request.setPrimaryMobile("");

        mPresenter.onFacebookLoginClick(request);
    }

    @Override
    public void onCancel() {
        Timber.d("Facebook Login cancelled");
        //Toast.makeText(this, "You cancelled login with Facebook", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(FacebookException error) {
        Crashlytics.logException(error);
        Timber.d(error);
    }

    public  void printHashKey(Context pContext) {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.e("fb hash", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("fb hash", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("fb hash", "printHashKey()", e);
        }
    }
}
