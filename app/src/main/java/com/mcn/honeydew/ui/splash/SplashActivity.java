
package com.mcn.honeydew.ui.splash;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.mcn.honeydew.R;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.login.LoginActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.phoneVerification.PhoneVerificationActivity;
import com.mcn.honeydew.ui.welcome.WelcomeTourActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;

import static com.mcn.honeydew.utils.AppConstants.KEY_COUNTRY_CODE;
import static com.mcn.honeydew.utils.AppConstants.KEY_PHONE;


public class SplashActivity extends BaseActivity implements SplashMvpView {

    @Inject
    SplashMvpPresenter<SplashMvpView> mPresenter;

    public static Intent getStartIntent(Context context) {
        Intent intent = new Intent(context, SplashActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getActivityComponent().inject(this);
        setUnBinder(ButterKnife.bind(this));
        mPresenter.onAttach(SplashActivity.this);

        // Add code to print out the key hash
     /*   try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.mcn.honeydew",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Timber.d("KeyHash: " + Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Timber.d("KeyHash: " + e.getMessage());
        } catch (NoSuchAlgorithmException e) {
            Timber.d("KeyHash: " + e.getMessage());
        }*/

    }


    @Override
    public void openLoginActivity() {
        Intent intent = LoginActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openMainActivity() {
   /*     Intent intent = MainActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();*/

        Intent intent = WelcomeTourActivity.getStartIntent(SplashActivity.this);
        startActivity(intent);
        finish();
    }

    @Override
    public void openPhoneVerificationActivity(String primaryMobile) {
        Intent intent = PhoneVerificationActivity.getStartIntent(this);
        if (primaryMobile != null && primaryMobile.contains("-")) {
            String mobileArray[] = primaryMobile.split("-");
            String mCountryCode = mobileArray[0];
            String mEmailOrPhone = mobileArray[1];
            intent.putExtra(KEY_COUNTRY_CODE, mCountryCode);
            intent.putExtra(KEY_PHONE, mEmailOrPhone);

        }
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

    }
}
