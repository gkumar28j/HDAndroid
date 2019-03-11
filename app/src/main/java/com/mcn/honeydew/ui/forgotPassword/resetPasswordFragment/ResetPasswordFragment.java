package com.mcn.honeydew.ui.forgotPassword.resetPasswordFragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mcn.honeydew.R;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseFragment;
import com.mcn.honeydew.utils.KeyboardUtils;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ResetPasswordFragment extends BaseFragment implements ResetPasswordMvpView {

    public static String TAG = ResetPasswordFragment.class.getSimpleName();

    private OnResetPasswordListener mListener;
    private String mAuthentication;

    @BindView(R.id.edit_new_password)
    EditText newPasswordEditText;

    @BindView(R.id.edit_confirm_password)
    EditText confirmPasswordEditText;


    @Inject
    ResetPasswordPresenter<ResetPasswordMvpView> mPresenter;

    public ResetPasswordFragment() {
        // Required empty public constructor
    }


    public static ResetPasswordFragment newInstance(String authentication) {
        ResetPasswordFragment fragment = new ResetPasswordFragment();
        Bundle args = new Bundle();
        args.putString("authentication", authentication);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mAuthentication = getArguments().getString("authentication");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reset_password, container, false);
        ActivityComponent component = getActivityComponent();
        if (component != null) {
            component.inject(this);
            setUnBinder(ButterKnife.bind(this, view));
            mPresenter.onAttach(this);

        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnResetPasswordListener) {
            mListener = (OnResetPasswordListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnResetPasswordListener");
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

    @Override
    public void onPasswordResetSuccess() {
        if (mListener != null) {
            String newPassword = newPasswordEditText.getText().toString();
            mListener.onResetPasswordSuccess(mAuthentication, newPassword);
        }
    }


    @OnClick(R.id.text_submit)
    void submitClicked() {

        KeyboardUtils.hideSoftInput(getActivity());

        String newPassword = newPasswordEditText.getText().toString();
        String confirmPassword = confirmPasswordEditText.getText().toString();

        mPresenter.resetPassword(mAuthentication, newPassword, confirmPassword);
    }

    public interface OnResetPasswordListener {

        void onResetPasswordSuccess(String emailOrPhone, String password);
    }
}
