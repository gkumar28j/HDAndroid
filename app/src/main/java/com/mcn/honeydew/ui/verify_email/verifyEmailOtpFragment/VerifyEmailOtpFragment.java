package com.mcn.honeydew.ui.verify_email.verifyEmailOtpFragment;

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

public class VerifyEmailOtpFragment extends BaseFragment implements VerifyEmailOtpMvpView {

    public static String TAG = VerifyEmailOtpFragment.class.getSimpleName();
    private VerifyOtpListener mListener;
    private String mAuthentication;

    @BindView(R.id.edit_otp)
    EditText otpEditText;

    @BindView(R.id.text_not_received)
    TextView notReceivedTextView;

    @BindView(R.id.text_title)
    TextView titleTextView;


    @Inject
    VerifyEmailOtpPresenter<VerifyEmailOtpMvpView> mPresenter;

    public VerifyEmailOtpFragment() {
        // Required empty public constructor
    }

    public static VerifyEmailOtpFragment newInstance(String authenticationDetail) {
        VerifyEmailOtpFragment fragment = new VerifyEmailOtpFragment();
        Bundle args = new Bundle();
        args.putString("only_email", authenticationDetail);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthentication = getArguments().getString("only_email");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_verify_email_otp, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);
        }

        setUp(view);

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
        titleTextView.setText("An OTP has been sent to "+mAuthentication +" Please enter OTP to verify email");
    }

    @OnClick(R.id.text_continue)
    void onContinueClicked() {
        String otp = otpEditText.getText().toString().trim();
        mPresenter.verifyOtp(mAuthentication, otp);
    }

    @OnClick(R.id.text_not_received)
    void onNotReceivedClicked() {
       /* if (mListener != null) {
            mListener.onResendCodeClicked();
        }*/

        mPresenter.resendOTP(mAuthentication);
    }

    @Override
    public void onVerified() {
        if (mListener != null) {
            mListener.onVerifySuccess(mAuthentication);
        }
    }

    @Override
    public void onOTPReceived() {

    }

    public interface VerifyOtpListener {
        void onVerifySuccess(String mAuthentication);

        void onResendCodeClicked();
    }
}
