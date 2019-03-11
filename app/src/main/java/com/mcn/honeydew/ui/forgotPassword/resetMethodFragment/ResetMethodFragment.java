package com.mcn.honeydew.ui.forgotPassword.resetMethodFragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.mcn.honeydew.utils.AppConstants.KEY_FRAGMENT_DATA;

public class ResetMethodFragment extends BaseFragment implements ResetMethodMvpView {

    public static String TAG = ResetMethodFragment.class.getSimpleName();
    private FragmentData mFragmentData;
    private OnResetMethodListener mListener;
    private String mEmail;
    private String mPhoneNumber;
    private int mIsEmail = 0;
    private String mAuthentication;
    private String mLastTwoDigit;
    private String mHiddenEmail;


    @Inject
    ResetMethodPresenter<ResetMethodMvpView> mPresenter;

    @BindView(R.id.radio_group_method)
    RadioGroup methodRadioGroup;

    @BindView(R.id.radio_phone)
    RadioButton phoneRadioButton;

    @BindView(R.id.radio_email)
    RadioButton emailRadioButton;


    public ResetMethodFragment() {
        // Required empty public constructor
    }

    public static ResetMethodFragment newInstance(FragmentData fragmentData) {
        ResetMethodFragment fragment = new ResetMethodFragment();
        Bundle args = new Bundle();
        args.putParcelable(KEY_FRAGMENT_DATA, fragmentData);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFragmentData = getArguments().getParcelable(KEY_FRAGMENT_DATA);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_method, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

        }

        if (mFragmentData != null) {

            if (mFragmentData.isEmailProvided) {
                mEmail = mFragmentData.getEmail();
                methodRadioGroup.check(R.id.radio_email);
                mAuthentication = mEmail;
                mIsEmail = 1;
            } else {
                mPhoneNumber = mFragmentData.getPhoneNumber();
                mEmail = mFragmentData.getEmail();
                methodRadioGroup.check(R.id.radio_phone);
                mAuthentication = mPhoneNumber;
                mIsEmail = 0;
            }

            if (TextUtils.isEmpty(mEmail)) {
                emailRadioButton.setVisibility(View.GONE);
            } else {
                emailRadioButton.setVisibility(View.VISIBLE);
                mHiddenEmail = hideEmail(mEmail);
                emailRadioButton.setText(mHiddenEmail);
            }


            if (mPhoneNumber != null) {
                phoneRadioButton.setVisibility(View.VISIBLE);
                mLastTwoDigit = mPhoneNumber.substring(mPhoneNumber.length() - 2, mPhoneNumber.length());
                phoneRadioButton.setText(getString(R.string.label_phone_ending).replace("@", mLastTwoDigit));
            } else {
                phoneRadioButton.setVisibility(View.INVISIBLE);
            }


        }

        methodRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {

                    case R.id.radio_email:
                        mIsEmail = 1;
                        mAuthentication = mEmail;
                        break;
                    case R.id.radio_phone:
                        mIsEmail = 0;
                        mAuthentication = mPhoneNumber;
                        break;
                }
            }
        });

        return view;
    }

  /*  // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onOtpSent(uri);
        }
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetMethodListener) {
            mListener = (OnResetMethodListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected void setUp(View view) {

    }

    @OnClick(R.id.text_continue)
    void onContinueClicked() {

        mPresenter.onContinueClicked(mAuthentication, mEmail, mIsEmail);
    }


    @Override
    public void onOtpSent(String verificationCode, String emailOrPhone) {
        if (mListener != null) {
            mListener.onOtpSent(verificationCode, emailOrPhone, mIsEmail, mLastTwoDigit, mHiddenEmail);
        }
    }


    public static class FragmentData implements Parcelable {
        private String userName;
        private String email;
        private String phoneNumber;
        private boolean isEmailProvided;

        public FragmentData(String userName, String email, String phoneNumber, boolean isEmailProvided) {
            this.userName = userName;
            this.email = email;
            this.phoneNumber = phoneNumber;
            this.isEmailProvided = isEmailProvided;
        }

        protected FragmentData(Parcel in) {
            userName = in.readString();
            email = in.readString();
            phoneNumber = in.readString();
            isEmailProvided = in.readByte() != 0;
        }

        public static final Creator<FragmentData> CREATOR = new Creator<FragmentData>() {
            @Override
            public FragmentData createFromParcel(Parcel in) {
                return new FragmentData(in);
            }

            @Override
            public FragmentData[] newArray(int size) {
                return new FragmentData[size];
            }
        };

        public String getUserName() {
            return userName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public boolean isEmailProvided() {
            return isEmailProvided;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(userName);
            dest.writeString(email);
            dest.writeString(phoneNumber);
            dest.writeByte((byte) (isEmailProvided ? 1 : 0));
        }
    }

    // Interface
    public interface OnResetMethodListener {

        void onOtpSent(String verificationCode, String emailOrPhone, int mIsEmail, String lastTwoDigit, String hiddenEmail);
    }

    private String hideEmail(String email) {
        int EMAIL_MIN_LENGTH = email.length() > 3 ? 3 : 0;
        StringBuilder emailBuilder = new StringBuilder();

        if (email.substring(0, email.indexOf("@")).length() > EMAIL_MIN_LENGTH) {
            emailBuilder.append(mEmail.substring(0, EMAIL_MIN_LENGTH));
            for (int i = EMAIL_MIN_LENGTH + 1; i <= mEmail.indexOf("@"); i++) {
                emailBuilder.append("*");
            }
            emailBuilder.append(mEmail.substring(mEmail.indexOf("@"), mEmail.length()));
        }

        return emailBuilder.toString();
    }


}
