package com.mcn.honeydew.ui.forgotPassword.verifyOtpFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VerifyOtpFragment extends BaseFragment implements VerifyOtpMvpView {

    public static String TAG = VerifyOtpFragment.class.getSimpleName();
    private VerifyOtpListener mListener;
    private String mAuthentication;
    private String mLastTwoDigit;
    private String mHiddenEmail;
    private int mIsEmail;

    @BindView(R.id.edit_otp)
    EditText otpEditText;

    @BindView(R.id.text_not_received)
    TextView notReceivedTextView;

    @BindView(R.id.text_title)
    TextView titleTextView;


    @Inject
    VerifyOtpPresenter<VerifyOtpMvpView> mPresenter;

    public VerifyOtpFragment() {
        // Required empty public constructor
    }

    public static VerifyOtpFragment newInstance(String authenticationDetail, int isEmail, String lastTwoDigit, String hiddenEmail) {
        VerifyOtpFragment fragment = new VerifyOtpFragment();
        Bundle args = new Bundle();
        args.putString("authentication", authenticationDetail);
        args.putString("lastTwoDigit", lastTwoDigit);
        args.putString("hiddenEmail", hiddenEmail);
        args.putInt("isEmail", isEmail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthentication = getArguments().getString("authentication");
            mLastTwoDigit = getArguments().getString("lastTwoDigit");
            mHiddenEmail = getArguments().getString("hiddenEmail");
            mIsEmail = getArguments().getInt("isEmail");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_verify_otp, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);


            if (mIsEmail == 1) {
                notReceivedTextView.setText("I didn't receive the email.");
                titleTextView.setText(getString(R.string.label_check_email).replace("@", mHiddenEmail));
                //titleSubTextView.setText("We've sent an email to "+55);
            } else {
                notReceivedTextView.setText("I didn't receive the code.");
                titleTextView.setText(getString(R.string.label_check_phone).replace("@", mLastTwoDigit));
                //titleSubTextView.setText("We've texted a code to the phone number ending in" + 55 + ". Once you received the code, enter it below to reset your password.");
            }
        }

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof VerifyOtpListener) {
            mListener = (VerifyOtpListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement VerifyOtpListener");
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
        String otp = otpEditText.getText().toString().trim();
        mPresenter.verifyOtp(mAuthentication, otp);
    }

    @OnClick(R.id.text_not_received)
    void onNotReceivedClicked() {
        if (mListener != null) {
            mListener.onResendCodeClicked();
        }
    }

    @Override
    public void onVerified() {
        if (mListener != null) {
            mListener.onVerifySuccess(mAuthentication);
        }
    }

    public interface VerifyOtpListener {
        void onVerifySuccess(String mAuthentication);

        void onResendCodeClicked();
    }
}
