package com.mcn.honeydew.ui.phoneVerification;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.Countries;
import com.mcn.honeydew.ui.base.BaseActivity;
import com.mcn.honeydew.ui.main.MainActivity;
import com.mcn.honeydew.ui.welcome.WelcomeTourActivity;
import com.mcn.honeydew.utils.AppConstants;
import com.mcn.honeydew.utils.AppLogger;
import com.mcn.honeydew.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneVerificationActivity extends BaseActivity implements PhoneVerificationMvpView,
        View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    private static final String OTP_REGEX = "[0-9]{1,5}";
    public static int REQUEST_ID_MULTIPLE_PERMISSIONS = 102;
    private static int METHOD_CALL = 0;
    private static int METHOD_TEXT = 1;
    private ArrayList<Countries> mCountryList;
    private String mCountryCode;
    private String mPhone;
    private int mCountryPosition;
    private boolean hasSmsReadPermission = false;
    private String mVerificationCode;
    private boolean mVerificationStatus = false;

    @Inject
    PhoneVerificationMvpPresenter<PhoneVerificationMvpView> mPresenter;

  /*  @Inject
    SmsReceiver mSmsReceiver;*/

    @BindView(R.id.text_country_code)
    TextView countryCodeTextView;

    @BindView(R.id.edit_first_digit)
    EditText firstDigitEditText;

    @BindView(R.id.edit_second_digit)
    EditText secondDigitEditText;

    @BindView(R.id.edit_third_digit)
    EditText thirdDigitEditText;

    @BindView(R.id.edit_fourth_digit)
    EditText fourthDigitEditText;

    @BindView(R.id.edit_fifth_digit)
    EditText fifthDigitEditText;

    @BindView(R.id.edit_phone_number)
    EditText phoneEditText;

    @BindView(R.id.edit_hidden)
    EditText hiddenEditText;

    private boolean isComingFromSettings = false;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, PhoneVerificationActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_verification);

        if (getIntent() != null) {

            isComingFromSettings = getIntent().getBooleanExtra("isFromSettings", false);

        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getActivityComponent().inject(this);

        setUnBinder(ButterKnife.bind(this));

        mPresenter.onAttach(this);

        setUp();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void setUp() {
        mPresenter.fetchCountries();

        setPINListeners();
    }

    @Override
    public void onCountriesFetched(List<Countries> countries) {
        mCountryList = new ArrayList<>(countries);
        Intent intent = getIntent();
        if (intent != null) {

            mCountryCode = intent.getStringExtra(AppConstants.KEY_COUNTRY_CODE);
            mPhone = intent.getStringExtra(AppConstants.KEY_PHONE);
            if (TextUtils.isEmpty(mCountryCode) && TextUtils.isEmpty(mPhone)) {
                TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                mCountryCode = getCountryIndexByName(manager.getNetworkCountryIso());
                countryCodeTextView.setText(mCountryCode);
                phoneEditText.setText(mPhone);
            }

            countryCodeTextView.setText(mCountryCode);
            phoneEditText.setText(mPhone);

        }

    }

    @Override
    public void onVerificationCodeSent(String code, boolean verificationStatus) {
        mVerificationCode = code;
        mVerificationStatus = verificationStatus;
        AppLogger.d("Verification Code ", code);
    }

    @Override
    public void openMainActivity(boolean isFirstTimeLogin) {

        if (isComingFromSettings) {
            setResult(RESULT_OK);
            finish();
        } else {

            if(isFirstTimeLogin){
                startActivity(WelcomeTourActivity.getStartIntent(this));
                finish();
            }else {
                startActivity(MainActivity.getStartIntent(this));
                finish();
            }


        }

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.edit_first_digit:
                if (hasFocus) {
                    setFocus(hiddenEditText);
                    KeyboardUtils.showSoftInput(hiddenEditText, this);
                }
                break;

            case R.id.edit_second_digit:
                if (hasFocus) {
                    setFocus(hiddenEditText);
                    KeyboardUtils.showSoftInput(hiddenEditText, this);
                }
                break;

            case R.id.edit_third_digit:
                if (hasFocus) {
                    setFocus(hiddenEditText);
                    KeyboardUtils.showSoftInput(hiddenEditText, this);
                }
                break;

            case R.id.edit_fourth_digit:
                if (hasFocus) {
                    setFocus(hiddenEditText);
                    KeyboardUtils.showSoftInput(hiddenEditText, this);
                }
                break;

            case R.id.edit_fifth_digit:
                if (hasFocus) {
                    setFocus(hiddenEditText);
                    KeyboardUtils.showSoftInput(hiddenEditText, this);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.edit_hidden:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (hiddenEditText.getText().length() == 5)
                            fifthDigitEditText.setText("");
                        else if (hiddenEditText.getText().length() == 4)
                            fourthDigitEditText.setText("");
                        else if (hiddenEditText.getText().length() == 3)
                            thirdDigitEditText.setText("");
                        else if (hiddenEditText.getText().length() == 2)
                            secondDigitEditText.setText("");
                        else if (hiddenEditText.getText().length() == 1)
                            firstDigitEditText.setText("");

                        if (hiddenEditText.length() > 0)
                            hiddenEditText.setText(hiddenEditText.getText().subSequence(0, hiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }
        return false;
    }

    // Text Watcher
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (s.length() == 0) {
            //setFocusedPinBackground(mPinFirstDigitEditText);
            firstDigitEditText.setText("");
        } else if (s.length() == 1) {
            //setFocusedPinBackground(mPinSecondDigitEditText);
            firstDigitEditText.setText(s.charAt(0) + "");
            secondDigitEditText.setText("");
            thirdDigitEditText.setText("");
            fourthDigitEditText.setText("");
            fifthDigitEditText.setText("");
        } else if (s.length() == 2) {
            //setFocusedPinBackground(mPinThirdDigitEditText);
            secondDigitEditText.setText(s.charAt(1) + "");
            thirdDigitEditText.setText("");
            fourthDigitEditText.setText("");
            fifthDigitEditText.setText("");
        } else if (s.length() == 3) {
            // setFocusedPinBackground(mPinForthDigitEditText);
            thirdDigitEditText.setText(s.charAt(2) + "");
            fourthDigitEditText.setText("");
            fifthDigitEditText.setText("");
        } else if (s.length() == 4) {
            //setFocusedPinBackground(mPinFifthDigitEditText);
            fourthDigitEditText.setText(s.charAt(3) + "");
            fifthDigitEditText.setText("");
        } else if (s.length() == 5) {
            //setDefaultPinBackground(mPinFifthDigitEditText);
            fifthDigitEditText.setText(s.charAt(4) + "");

            KeyboardUtils.hideSoftInput(PhoneVerificationActivity.this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    /*------------------------------ Clicks -------------------------------------------------------*/

    @OnClick(R.id.text_country_code)
    void onCountryCodeClicked() {
        showCountryDialog();
    }

    @OnClick(R.id.text_by_text)
    void onByTextClicked() {

        KeyboardUtils.hideSoftInput(this);
        String phoneNumber = phoneEditText.getText().toString().trim();
        mPresenter.getVerificationCode(METHOD_TEXT, phoneNumber, mCountryCode);
    }

    @OnClick(R.id.text_by_call)
    void onByCallClicked() {

        KeyboardUtils.hideSoftInput(this);
        String phoneNumber = phoneEditText.getText().toString().trim();
        mPresenter.getVerificationCode(METHOD_CALL, phoneNumber, mCountryCode);
    }

    @OnClick(R.id.text_resend)
    void onResendClicked() {

        String phoneNumber = phoneEditText.getText().toString().trim();
        mPresenter.getVerificationCode(METHOD_TEXT, phoneNumber, mCountryCode);
    }

    @OnClick(R.id.text_confirm)
    void onConfirmClicked() {

        mCountryCode = countryCodeTextView.getText().toString();
        mPhone = phoneEditText.getText().toString();

        if (isValidCode()) {
            mPresenter.updateVerificationStatus(1, mPhone, mCountryCode,isComingFromSettings);
        }

    }

    /* -----------------------------  Private Methods -------------------------------------------- */
    private String getCountryIndexByName(String isoCode) {
        Countries country;
        for (int i = 0; i < mCountryList.size(); i++) {
            if (mCountryList.get(i).getCode().equalsIgnoreCase(isoCode)) {
                country = mCountryList.get(i);
                mCountryCode = country.getDialCode();
                return mCountryCode;
            }
        }
        return "+1"; // index of country US
    }

    private void showCountryDialog() {

        CharSequence[] items = null;
        ArrayList<String> countryNames = new ArrayList<String>();
        ArrayList<String> countryCodes = new ArrayList<>();

        for (Countries country : mCountryList) {
            countryNames.add(country.getDialCode() + " (" + country.getName() + ")");
            countryCodes.add(country.getDialCode());
        }
        items = countryNames.toArray(new CharSequence[countryNames.size()]);

        // Getting position of the country based on country code.
        if (!TextUtils.isEmpty(mCountryCode) && countryCodes.contains(mCountryCode)) {
            mCountryPosition = countryCodes.indexOf(mCountryCode);
        }


        new AlertDialog.Builder(this).setTitle("Select Country")
                .setSingleChoiceItems(items, mCountryPosition, null)

                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                        mCountryPosition = ((AlertDialog) dialog)
                                .getListView().getCheckedItemPosition();


                        if (mCountryPosition == AdapterView.INVALID_POSITION) {
                            return;
                        }

                        // Do something useful with the position of the
                        // selected radio button

                        mCountryCode = mCountryList.get(mCountryPosition).getDialCode();

                        countryCodeTextView.setText(mCountryCode);

                    }
                }).show();
    }

    private boolean isValidCode() {
        String firstDigit = firstDigitEditText.getText().toString().trim();
        String secondDigit = secondDigitEditText.getText().toString().trim();
        String thirdDigit = thirdDigitEditText.getText().toString().trim();
        String fourthDigit = fourthDigitEditText.getText().toString().trim();
        String fifthDigit = fifthDigitEditText.getText().toString().trim();

        StringBuilder builder = new StringBuilder();
        builder.append(firstDigit)
                .append(secondDigit)
                .append(thirdDigit)
                .append(fourthDigit)
                .append(fifthDigit);
        if (TextUtils.isEmpty(firstDigit) && TextUtils.isEmpty(secondDigit)
                && TextUtils.isEmpty(thirdDigit) && TextUtils.isEmpty(fourthDigit)
                && TextUtils.isEmpty(fifthDigit)) {
            Toast.makeText(this, "Please enter authorization code.", Toast.LENGTH_SHORT).show();
            return false;

        } else if (TextUtils.isEmpty(firstDigit) || TextUtils.isEmpty(secondDigit) || TextUtils.isEmpty(thirdDigit)
                || TextUtils.isEmpty(fourthDigit) || TextUtils.isEmpty(fifthDigit) || TextUtils.isEmpty(mVerificationCode) || !mVerificationCode.equals(builder.toString())) {
            Toast.makeText(this, "Please enter valid code.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        hiddenEditText.addTextChangedListener(this);

        firstDigitEditText.setOnFocusChangeListener(this);
        secondDigitEditText.setOnFocusChangeListener(this);
        thirdDigitEditText.setOnFocusChangeListener(this);
        fourthDigitEditText.setOnFocusChangeListener(this);
        fifthDigitEditText.setOnFocusChangeListener(this);

        firstDigitEditText.setOnKeyListener(this);
        secondDigitEditText.setOnKeyListener(this);
        thirdDigitEditText.setOnKeyListener(this);
        fourthDigitEditText.setOnKeyListener(this);
        fifthDigitEditText.setOnKeyListener(this);
        hiddenEditText.setOnKeyListener(this);
    }


}
