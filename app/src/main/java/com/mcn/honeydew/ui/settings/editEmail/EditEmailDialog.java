package com.mcn.honeydew.ui.settings.editEmail;

import android.os.Bundle;

import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.mcn.honeydew.R;
import com.mcn.honeydew.data.network.model.UserDetailResponse;
import com.mcn.honeydew.di.component.ActivityComponent;
import com.mcn.honeydew.ui.base.BaseDialog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditEmailDialog extends BaseDialog implements EditEmailMvpView {

    private static final String TAG = "EditEmailDialog";

    @Inject
    EditEmailMvpPresenter<EditEmailMvpView> mPresenter;

    @BindView(R.id.et_message)
    EditText mMessage;


    @BindView(R.id.btn_submit)
    Button mSubmitButton;

    @BindView(R.id.btn_cancel)
    Button mCancelButton;

    String finalEmail;

    String originalEmail;

    boolean isEmailVerified;

    private RefreshListener mListener;

    public void setListener(RefreshListener listener) {
        mListener = listener;
    }


    public static EditEmailDialog newInstance(boolean emailVerified) {
        EditEmailDialog fragment = new EditEmailDialog();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.edit_email_dialog, container, false);

        ActivityComponent component = getActivityComponent();
        if (component != null) {

            component.inject(this);

            setUnBinder(ButterKnife.bind(this, view));

            mPresenter.onAttach(this);
        }

        return view;
    }

    public void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }


    @Override
    protected void setUp(View view) {

        mPresenter.onViewPrepared();
        mSubmitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finalEmail = mMessage.getText().toString().trim();

                if (originalEmail.equals(finalEmail) && isEmailVerified) {
                    mMessage.setError("Email already verified.");
                    mMessage.requestFocus();
                }else if(originalEmail.equals(finalEmail) && !isEmailVerified){
                    mPresenter.onResendOTP(finalEmail);
                }else {
                    mPresenter.onEmailSubmit(finalEmail);
                }

            }
        });

    }

    @OnClick(R.id.btn_cancel)
    void onCancelClick() {
        mPresenter.onCancelClicked();
    }


    @Override
    public void dismissDialog() {
        super.dismissDialog(TAG);
        getBaseActivity().hideKeyboard();
    }

    @Override
    public void showUserEmail(UserDetailResponse userData) {

        originalEmail = userData.getPrimaryEmail();
        isEmailVerified = userData.isEmailVerified();

        mMessage.setText(userData.getPrimaryEmail());
    }


    @Override
    public void refreshData() {
        dismissDialog();
        mListener.onEmailEditedSuccessfully(finalEmail);
    }

    @Override
    public void onOTPReceived() {
        dismissDialog();
        mListener.onEmailEditedSuccessfully(finalEmail);

    }

    @Override
    public void onDestroyView() {
        mPresenter.onDetach();
        super.onDestroyView();
    }

    public static interface RefreshListener {
        void onEmailEditedSuccessfully(String email);
    }


}
